package cpw.mods.fml.installer;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VersionInfo {
    public static final VersionInfo INSTANCE = new VersionInfo();
    public final JsonRootNode versionData;

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
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "profileName" });
    }

    public static String getVersionTarget() {
	return INSTANCE.versionData.getStringValue(new Object[] { "versionInfo", "id" });
    }

    public static File getLibraryPath(File root) {
	String path = INSTANCE.versionData.getStringValue(new Object[] { "install", "path" });
	String[] split = (String[]) Iterables.toArray(Splitter.on(':').omitEmptyStrings().split(path), String.class);
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
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "version" });
    }

    public static String getWelcomeMessage() {
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "welcome" });
    }

    public static String getLogoFileName() {
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "logo" });
    }

    public static JsonNode getVersionInfo() {
	return INSTANCE.versionData.getNode(new Object[] { "versionInfo" });
    }

    public static File getMinecraftFile(File path) {
	return new File(new File(path, getMinecraftVersion()), getMinecraftVersion() + ".jar");
    }

    public static String getContainedFile() {
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "filePath" });
    }

    public static String getCivWarsFile() {
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "civwarsFile" });
    }

    public static void extractFile(File path) throws IOException {
	INSTANCE.doFileExtract(getContainedFile(), path);
    }
    
    public static void extractCivWarsFile(File path) throws IOException {
	INSTANCE.doFileExtract(getCivWarsFile(), path);
    }

    private void doFileExtract(String src, File path) throws IOException {
	InputStream inputStream = getClass().getResourceAsStream("/" + src);
	OutputSupplier outputSupplier = Files.newOutputStreamSupplier(path);
	ByteStreams.copy(inputStream, outputSupplier);
    }

    public static String getMinecraftVersion() {
	return INSTANCE.versionData.getStringValue(new Object[] { "install", "minecraft" });
    }
}

/*
 * Location: C:\Users\User\Desktop\minecraftforge-installer-1.6.2-9.10.0.804.jar
 * Qualified Name: cpw.mods.fml.installer.VersionInfo JD-Core Version: 0.6.2
 */