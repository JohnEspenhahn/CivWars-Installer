package cpw.mods.fml.installer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;

public class VersionInfo {
    public static final VersionInfo INSTANCE = new VersionInfo();
    public final JsonRootNode       versionData;

    public VersionInfo() {
        InputStream installProfile = getClass().getResourceAsStream("/install_profile.json");
        JdomParser parser = new JdomParser();
        try {
            this.versionData = parser.parse(new InputStreamReader(installProfile, Charsets.UTF_8));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static String getProfileName() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "profileName" });
    }

    public static String getVersionTarget() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "versionInfo", "id" });
    }

    public static File getLibraryPath(File root) {
        String path = VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "path" });
        String[] split = Iterables.toArray(Splitter.on(':').omitEmptyStrings().split(path), String.class);
        File dest = root;
        Iterable<String> subSplit = Splitter.on('.').omitEmptyStrings().split(split[0]);
        for (String part : subSplit) {
            dest = new File(dest, part);
        }
        dest = new File(new File(dest, split[1]), split[2]);
        String fileName = split[1] + "-" + split[2] + ".jar";
        return new File(dest, fileName);
    }

    public static String getVersion() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "version" });
    }

    public static String getWelcomeMessage() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "welcome" });
    }

    public static String getLogoFileName() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "logo" });
    }

    public static boolean getStripMetaInf() {
        try {
            return VersionInfo.INSTANCE.versionData.getBooleanValue(new Object[] { "install", "stripMeta" }).booleanValue();
        } catch (Exception e) {}
        return false;
    }

    public static JsonNode getVersionInfo() {
        return VersionInfo.INSTANCE.versionData.getNode(new Object[] { "versionInfo" });
    }

    public static File getMinecraftFile(File path) {
        return new File(new File(path, VersionInfo.getMinecraftVersion()), VersionInfo.getMinecraftVersion() + ".jar");
    }

    public static String getContainedFile() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "filePath" });
    }

    public static void extractContainedFile(File path) throws IOException {
        VersionInfo.extractFile(VersionInfo.getContainedFile(), path);
    }

    public static void extractFile(String source, File path) throws IOException {
        VersionInfo.INSTANCE.doFileExtract(source, path);
    }

    private void doFileExtract(String source, File target) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/" + source);
        OutputSupplier<FileOutputStream> outputSupplier = Files.newOutputStreamSupplier(target);
        ByteStreams.copy(inputStream, outputSupplier);
    }

    public static String getMinecraftVersion() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "minecraft" });
    }

    public static String getMirrorListURL() {
        return VersionInfo.INSTANCE.versionData.getStringValue(new Object[] { "install", "mirrorList" });
    }

    public static boolean hasMirrors() {
        return VersionInfo.INSTANCE.versionData.isStringValue(new Object[] { "install", "mirrorList" });
    }
}

/*
 * Location: C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar Qualified Name: cpw.mods.fml.installer.VersionInfo JD-Core Version: 0.6.2
 */