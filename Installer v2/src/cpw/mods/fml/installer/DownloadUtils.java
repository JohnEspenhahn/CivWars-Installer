package cpw.mods.fml.installer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

import javax.swing.ProgressMonitor;

import org.tukaani.xz.XZInputStream;

import argo.jdom.JsonNode;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class DownloadUtils {
    public static int downloadInstalledLibraries(String jsonMarker, File librariesDir, IMonitor monitor, List<JsonNode> libraries, int progress, List<String> grabbed, List<String> bad) {
        for (JsonNode library : libraries) {
            String libName = library.getStringValue(new Object[] { "name" });
            List<String> checksums = null;
            if (library.isArrayNode(new Object[] { "checksums" })) {
                checksums = Lists.newArrayList(Lists.transform(library.getArrayNode(new Object[] { "checksums" }), new Function<JsonNode, String>() {
                    @Override
                    public String apply(JsonNode node) {
                        return node.getText();
                    }
                }));
            }
            
            monitor.setNote(String.format("Considering library %s", new Object[] { libName }));
            if (library.isBooleanValue(new Object[] { jsonMarker })) {
                if (library.getBooleanValue(new Object[] { jsonMarker }).booleanValue()) {
                    String[] nameparts = Iterables.toArray(Splitter.on(':').split(libName), String.class);
                    nameparts[0] = nameparts[0].replace('.', '/');
                    String jarName = nameparts[1] + '-' + nameparts[2] + ".jar";
                    String pathName = nameparts[0] + '/' + nameparts[1] + '/' + nameparts[2] + '/' + jarName;
                    File libPath = new File(librariesDir, pathName.replace('/', File.separatorChar));
                    String libURL = "https://libraries.minecraft.net/";
                    if (MirrorData.INSTANCE.hasMirrors()) { 
                        if (library.isStringValue(new Object[] { "url" })) {
                            libURL = MirrorData.INSTANCE.getMirrorURL();
                        }
                    }
                    
                    if (library.isStringValue(new Object[] { "url" })) {
                        libURL = library.getStringValue(new Object[] { "url" }); // + "/";
                    }
                    if ((libPath.exists()) && (DownloadUtils.checksumValid(libPath, checksums))) {
                        monitor.setProgress(progress++);
                        continue;
                    }

                    libPath.getParentFile().mkdirs();
                    monitor.setNote(String.format("Downloading library %s", new Object[] { libName }));
                    libURL = libURL + pathName;
                    File packFile = new File(libPath.getParentFile(), libPath.getName() + ".pack.xz");
                    if (!DownloadUtils.downloadFile(libName, packFile, libURL + ".pack.xz", null)) {
                        if (library.isStringValue(new Object[] { "url" })) {
                            monitor.setNote(String.format("Trying unpacked library %s", new Object[] { libName }));
                        }
                        if (!DownloadUtils.downloadFile(libName, libPath, libURL, checksums)) {
                            if ((!libURL.startsWith("https://libraries.minecraft.net/")) || (!jsonMarker.equals("clientreq"))) {
                                bad.add(libName);
                            } else {
                                monitor.setNote("Unmrriored file failed, Mojang launcher should download at next run, non fatal");
                            }
                        } else {
                            grabbed.add(libName);
                        }
                    } else {
                        try {
                            monitor.setNote(String.format("Unpacking packed file %s", new Object[] { packFile.getName() }));
                            DownloadUtils.unpackLibrary(libPath, Files.toByteArray(packFile));
                            monitor.setNote(String.format("Successfully unpacked packed file %s", new Object[] { packFile.getName() }));
                            packFile.delete();

                            if (DownloadUtils.checksumValid(libPath, checksums)) {
                                grabbed.add(libName);
                            } else {
                                bad.add(libName);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            bad.add(libName);
                        }
                    }
                }
            } else
                monitor.setProgress(progress++);
        }

        return progress;
    }

    private static boolean checksumValid(File libPath, List<String> checksums) {
        try {
            byte[] fileData = Files.toByteArray(libPath);
            boolean valid = (checksums == null) || (checksums.isEmpty()) || (checksums.contains(Hashing.sha1().hashBytes(fileData).toString()));
            if ((!valid) && (libPath.getName().endsWith(".jar"))) ;
            return DownloadUtils.validateJar(libPath, fileData, checksums);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void unpackLibrary(File output, byte[] data) throws IOException {
        if (output.exists()) {
            output.delete();
        }

        byte[] decompressed = DownloadUtils.readFully(new XZInputStream(new ByteArrayInputStream(data)));

        String end = new String(decompressed, decompressed.length - 4, 4);
        if (!end.equals("SIGN")) {
            System.out.println("Unpacking failed, signature missing " + end);
            return;
        }

        int x = decompressed.length;
        int len = decompressed[(x - 8)] & 0xFF | (decompressed[(x - 7)] & 0xFF) << 8 | (decompressed[(x - 6)] & 0xFF) << 16 | (decompressed[(x - 5)] & 0xFF) << 24;

        byte[] checksums = Arrays.copyOfRange(decompressed, decompressed.length - len - 8, decompressed.length - 8);

        FileOutputStream jarBytes = new FileOutputStream(output);
        JarOutputStream jos = new JarOutputStream(jarBytes);

        Pack200.newUnpacker().unpack(new ByteArrayInputStream(decompressed), jos);

        jos.putNextEntry(new JarEntry("checksums.sha1"));
        jos.write(checksums);
        jos.closeEntry();

        jos.close();
        jarBytes.close();
    }

    public static boolean validateJar(File libPath, byte[] data, List<String> checksums) throws IOException {
        System.out.println("Checking \"" + libPath.getAbsolutePath() + "\" internal checksums");

        HashMap<String, String> files = new HashMap<String, String>();
        String[] hashes = null;
        JarInputStream jar = new JarInputStream(new ByteArrayInputStream(data));
        JarEntry entry = jar.getNextJarEntry();
        while (entry != null) {
            byte[] eData = DownloadUtils.readFully(jar);

            if (entry.getName().equals("checksums.sha1")) {
                hashes = new String(eData, Charset.forName("UTF-8")).split("\n");
            }

            if (!entry.isDirectory()) {
                files.put(entry.getName(), Hashing.sha1().hashBytes(eData).toString());
            }
            entry = jar.getNextJarEntry();
        }
        jar.close();

        if (hashes != null) {
            boolean failed = !checksums.contains(files.get("checksums.sha1"));
            if (failed) {
                System.out.println("    checksums.sha1 failed validation");
            } else {
                System.out.println("    checksums.sha1 validated successfully");
                for (String hash : hashes) {
                    if ((!hash.trim().equals("")) && (hash.contains(" "))) {
                        String[] e = hash.split(" ");
                        String validChecksum = e[0];
                        String target = e[1];
                        String checksum = (String) files.get(target);

                        if ((!files.containsKey(target)) || (checksum == null)) {
                            System.out.println("    " + target + " : missing");
                            failed = true;
                        } else if (!checksum.equals(validChecksum)) {
                            System.out.println("    " + target + " : failed (" + checksum + ", " + validChecksum + ")");
                            failed = true;
                        }
                    }
                }
            }
            if (!failed) {
                System.out.println("    Jar contents validated successfully");
            }

            return !failed;
        }

        System.out.println("    checksums.sha1 was not found, assuming good");
        return true;
        
        /*
        System.out.println("    checksums.sha1 was not found, validation failed");
        return false;
        */
    }

    public static List<String> downloadList(String libURL) {
        try {
            URL url = new URL(libURL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            URLISSupplier urlSupplier = new URLISSupplier(connection);
            return CharStreams.readLines(CharStreams.newReaderSupplier(urlSupplier, Charsets.UTF_8));
        } catch (Exception e) {}
        return Collections.emptyList();
    }

    public static boolean downloadFile(String libName, File libPath, String libURL, List<String> checksums) {
        try {
            URL url = new URL(libURL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            URLISSupplier urlSupplier = new URLISSupplier(connection);
            Files.copy(urlSupplier, libPath);
           
            if (DownloadUtils.checksumValid(libPath, checksums)) { return true; }
            return false;
        } catch (FileNotFoundException fnf) {
            if (!libURL.endsWith(".pack.xz")) {
                fnf.printStackTrace();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] readFully(InputStream stream) throws IOException {
        byte[] data = new byte[4096];
        ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
        int len;
        do {
            len = stream.read(data);
            if (len > 0) {
                entryBuffer.write(data, 0, len);
            }
        } while (len != -1);

        return entryBuffer.toByteArray();
    }

    public static IMonitor buildMonitor() {
        return new IMonitor() {
            private ProgressMonitor monitor = new ProgressMonitor(null, "Progress...", "", 0, 2);

            @Override
            public void setMaximum(int max) {
                this.monitor.setMaximum(max);
            }

            @Override
            public void setNote(String note) {
                System.out.println(note);
                this.monitor.setNote(note);
            }

            @Override
            public void setProgress(int progress) {
                this.monitor.setProgress(progress);
            }

            @Override
            public void close() {
                this.monitor.close();
            }
        };
    }

    static class URLISSupplier implements InputSupplier<InputStream> {
        private final URLConnection connection;

        private URLISSupplier(URLConnection connection) {
            this.connection = connection;
        }

        @Override
        public InputStream getInput() throws IOException {
            return this.connection.getInputStream();
        }
    }
}

/*
 * Location: C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar Qualified Name: cpw.mods.fml.installer.DownloadUtils JD-Core Version:
 * 0.6.2
 */