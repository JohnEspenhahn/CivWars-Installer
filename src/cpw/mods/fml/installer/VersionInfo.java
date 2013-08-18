/*     */ package cpw.mods.fml.installer;
/*     */ 
/*     */ import argo.jdom.JdomParser;
/*     */ import argo.jdom.JsonNode;
/*     */ import argo.jdom.JsonRootNode;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.common.io.OutputSupplier;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ 
/*     */ public class VersionInfo
/*     */ {
/*  24 */   public static final VersionInfo INSTANCE = new VersionInfo();
/*     */   public final JsonRootNode versionData;
/*     */ 
/*     */   public VersionInfo()
/*     */   {
/*  29 */     InputStream installProfile = getClass().getResourceAsStream("/install_profile.json");
/*  30 */     JdomParser parser = new JdomParser();
/*     */     try
/*     */     {
/*  34 */       this.versionData = parser.parse(new InputStreamReader(installProfile, Charsets.UTF_8));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  38 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getProfileName()
/*     */   {
/*  44 */     return INSTANCE.versionData.getStringValue(new Object[] { "install", "profileName" });
/*     */   }
/*     */ 
/*     */   public static String getVersionTarget()
/*     */   {
/*  49 */     return INSTANCE.versionData.getStringValue(new Object[] { "versionInfo", "id" });
/*     */   }
/*     */ 
/*     */   public static File getLibraryPath(File root) {
/*  53 */     String path = INSTANCE.versionData.getStringValue(new Object[] { "install", "path" });
/*  54 */     String[] split = (String[])Iterables.toArray(Splitter.on(':').omitEmptyStrings().split(path), String.class);
/*  55 */     File dest = root;
/*  56 */     Iterable<String> subSplit = Splitter.on('.').omitEmptyStrings().split(split[0]);
/*  57 */     for (String part : subSplit)
/*     */     {
/*  59 */       dest = new File(dest, part);
/*     */     }
/*  61 */     dest = new File(new File(dest, split[1]), split[2]);
/*  62 */     String fileName = split[1] + "-" + split[2] + ".jar";
/*  63 */     return new File(dest, fileName);
/*     */   }
/*     */ 
/*     */   public static String getVersion()
/*     */   {
/*  68 */     return INSTANCE.versionData.getStringValue(new Object[] { "install", "version" });
/*     */   }
/*     */ 
/*     */   public static String getWelcomeMessage()
/*     */   {
/*  73 */     return INSTANCE.versionData.getStringValue(new Object[] { "install", "welcome" });
/*     */   }
/*     */ 
/*     */   public static String getLogoFileName()
/*     */   {
/*  78 */     return INSTANCE.versionData.getStringValue(new Object[] { "install", "logo" });
/*     */   }
/*     */ 
/*     */   public static JsonNode getVersionInfo()
/*     */   {
/*  83 */     return INSTANCE.versionData.getNode(new Object[] { "versionInfo" });
/*     */   }
/*     */ 
/*     */   public static File getMinecraftFile(File path)
/*     */   {
/*  88 */     return new File(new File(path, getMinecraftVersion()), getMinecraftVersion() + ".jar");
/*     */   }
/*     */ 
/*     */   public static String getContainedFile() {
/*  92 */     return INSTANCE.versionData.getStringValue(new Object[] { "install", "filePath" });
/*     */   }

			public static String getCivWarsFile() {
			  return INSTANCE.versionData.getStringValue(new Object[] { "install", "civwarsFile" });
			}
/*     */ 
/*     */   public static void extractFile(File path) throws IOException {
/*  96 */     INSTANCE.doFileExtract(path);
/*     */   }
	
			public static void extractCivWarsFile(File path) throws IOException {
			  INSTANCE.doCivWarsExtract(path);
			}
/*     */ 
/*     */   private void doFileExtract(File path) throws IOException
/*     */   {
/* 101 */     InputStream inputStream = getClass().getResourceAsStream("/" + getContainedFile());
/* 102 */     OutputSupplier outputSupplier = Files.newOutputStreamSupplier(path);
/* 103 */     ByteStreams.copy(inputStream, outputSupplier);
/*     */   }

			public void doCivWarsExtract(File path) throws IOException
			{
			  InputStream inputStream = getClass().getResourceAsStream("/" + getCivWarsFile());
			  OutputSupplier outputSupplier = Files.newOutputStreamSupplier(new File(path, getCivWarsFile()));
			  ByteStreams.copy(inputStream, outputSupplier);
			}
/*     */ 
/*     */   public static String getMinecraftVersion()
/*     */   {
/* 108 */     return INSTANCE.versionData.getStringValue(new Object[] { "install", "minecraft" });
/*     */   }
/*     */ }

/* Location:           C:\Users\User\Desktop\minecraftforge-installer-1.6.2-9.10.0.804.jar
 * Qualified Name:     cpw.mods.fml.installer.VersionInfo
 * JD-Core Version:    0.6.2
 */