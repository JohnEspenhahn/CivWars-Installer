/*     */ package cpw.mods.fml.installer;
/*     */ 
/*     */ import argo.format.PrettyJsonFormatter;
/*     */ import argo.jdom.JdomParser;
/*     */ import argo.jdom.JsonField;
/*     */ import argo.jdom.JsonNode;
/*     */ import argo.jdom.JsonNodeFactories;
/*     */ import argo.jdom.JsonRootNode;
/*     */ import argo.saj.InvalidSyntaxException;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ public class ClientInstall
/*     */   implements ActionType
/*     */ {
/*     */   public boolean run(File target)
/*     */   {
/*  29 */     if (!target.exists())
/*     */     {
/*  31 */       JOptionPane.showMessageDialog(null, "There is no minecraft installation at this location!", "Error", 0);
/*  32 */       return false;
/*     */     }
/*  34 */     File launcherProfiles = new File(target, "launcher_profiles.json");
/*  35 */     if (!launcherProfiles.exists())
/*     */     {
/*  37 */       JOptionPane.showMessageDialog(null, "There is no minecraft launcher profile at this location, you need to run the launcher first!", "Error", 0);
/*  38 */       return false;
/*     */     }
/*     */ 
/*  41 */     File versionRootDir = new File(target, "versions");
/*  42 */     File versionTarget = new File(versionRootDir, VersionInfo.getVersionTarget());
/*  43 */     if ((!versionTarget.mkdirs()) && (!versionTarget.isDirectory()))
/*     */     {
/*  45 */       if (!versionTarget.delete())
/*     */       {
/*  47 */         JOptionPane.showMessageDialog(null, "There was a problem with the launcher version data. You will need to clear " + versionTarget.getAbsolutePath() + " manually", "Error", 0);
/*     */       }
/*     */       else
/*     */       {
/*  52 */         versionTarget.mkdirs();
/*     */       }
/*     */     }
/*     */ 
/*  56 */     File versionJsonFile = new File(versionTarget, VersionInfo.getVersionTarget() + ".json");
/*  57 */     File clientJarFile = new File(versionTarget, VersionInfo.getVersionTarget() + ".jar");
/*  58 */     File minecraftJarFile = VersionInfo.getMinecraftFile(versionRootDir);
/*     */     try
/*     */     {
/*  61 */       Files.copy(minecraftJarFile, clientJarFile);
/*     */     }
/*     */     catch (IOException e1)
/*     */     {
/*  65 */       JOptionPane.showMessageDialog(null, "You need to run the version " + VersionInfo.getMinecraftVersion() + " manually at least once", "Error", 0);
/*  66 */       return false;
/*     */     }

/*  68 */     File targetLibraryFile = VersionInfo.getLibraryPath(new File(target, "libraries"));
/*  69 */     if ((!targetLibraryFile.getParentFile().mkdirs()) && (!targetLibraryFile.getParentFile().isDirectory()))
/*     */     {
/*  71 */       if (!targetLibraryFile.getParentFile().delete())
/*     */       {
/*  73 */         JOptionPane.showMessageDialog(null, "There was a problem with the launcher version data. You will need to clear " + targetLibraryFile.getAbsolutePath() + " manually", "Error", 0);
/*  74 */         return false;
/*     */       }
/*     */ 
/*  78 */       targetLibraryFile.getParentFile().mkdirs();
/*     */     }
/*     */ 
/*  83 */     JsonRootNode versionJson = JsonNodeFactories.object(VersionInfo.getVersionInfo().getFields());
/*     */     try
/*     */     {
/*  87 */       BufferedWriter newWriter = Files.newWriter(versionJsonFile, Charsets.UTF_8);
/*  88 */       PrettyJsonFormatter.fieldOrderPreservingPrettyJsonFormatter().format(versionJson, newWriter);
/*  89 */       newWriter.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  93 */       JOptionPane.showMessageDialog(null, "There was a problem writing the launcher version data,  is it write protected?", "Error", 0);
/*  94 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  99 */       VersionInfo.extractFile(targetLibraryFile);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 103 */       JOptionPane.showMessageDialog(null, "There was a problem writing the system library file", "Error", 0);
/* 104 */       return false;
/*     */     }
/*     */ 
			  JsonRootNode jsonProfileData;
/* 107 */     JdomParser parser = new JdomParser();
/*     */     try
/*     */     {
/* 112 */       jsonProfileData = parser.parse(Files.newReader(launcherProfiles, Charsets.UTF_8));
/*     */     }
/*     */     catch (InvalidSyntaxException e)
/*     */     {
/* 116 */       JOptionPane.showMessageDialog(null, "The launcher profile file is corrupted. Re-run the minecraft launcher to fix it!", "Error", 0);
/* 117 */       return false;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 121 */       throw Throwables.propagate(e);
/*     */     }

		      File modsFolder = new File(target, "mods");
			  try
			  {
				  if (!modsFolder.isDirectory() || !modsFolder.exists()) {
					  modsFolder.mkdir();
				  } else {
					  for (File f : modsFolder.listFiles()) {
						  if (f.getName().toLowerCase().contains("civwars")) {
							  f.delete();
						  }
					  }
				  }
			  } catch (Exception e) {
				  JOptionPane.showMessageDialog(null, "There was a problem removing old CivWars versions.", "Error", 0);
				  return false;
			  }
			  
			  try {
				  VersionInfo.extractCivWarsFile(modsFolder);
			  } catch (Exception e) {
				  JOptionPane.showMessageDialog(null, "There was a problem extracting CivWars to the mods folder.", "Error", 0);
				  return false;
			  }
/*     */ 
/* 125 */     JsonField[] fields = { JsonNodeFactories.field("name", JsonNodeFactories.string(VersionInfo.getProfileName())), JsonNodeFactories.field("lastVersionId", JsonNodeFactories.string(VersionInfo.getVersionTarget())) };
/*     */ 
/* 130 */     HashMap profileCopy = Maps.newHashMap(jsonProfileData.getNode(new Object[] { "profiles" }).getFields());
/* 131 */     HashMap rootCopy = Maps.newHashMap(jsonProfileData.getFields());
/* 132 */     profileCopy.put(JsonNodeFactories.string(VersionInfo.getProfileName()), JsonNodeFactories.object(fields));
/* 133 */     JsonRootNode profileJsonCopy = JsonNodeFactories.object(profileCopy);
/*     */ 
/* 135 */     rootCopy.put(JsonNodeFactories.string("profiles"), profileJsonCopy);
/*     */ 
/* 137 */     jsonProfileData = JsonNodeFactories.object(rootCopy);
/*     */     try
/*     */     {
/* 141 */       BufferedWriter newWriter = Files.newWriter(launcherProfiles, Charsets.UTF_8);
/* 142 */       PrettyJsonFormatter.fieldOrderPreservingPrettyJsonFormatter().format(jsonProfileData, newWriter);
/* 143 */       newWriter.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 147 */       JOptionPane.showMessageDialog(null, "There was a problem writing the launch profile,  is it write protected?", "Error", 0);
/* 148 */       return false;
/*     */     }
/*     */ 
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isPathValid(File targetDir)
/*     */   {
/* 157 */     if (targetDir.exists())
/*     */     {
/* 159 */       File launcherProfiles = new File(targetDir, "launcher_profiles.json");
/* 160 */       return launcherProfiles.exists();
/*     */     }
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   public String getFileError(File targetDir)
/*     */   {
/* 168 */     if (targetDir.exists())
/*     */     {
/* 170 */       return "The directory is missing a launcher profile. Please run the minecraft launcher first";
/*     */     }
/*     */ 
/* 174 */     return "There is no minecraft directory set up. Either choose an alternative, or run the minecraft launcher to create one";
/*     */   }
/*     */ 
/*     */   public String getSuccessMessage()
/*     */   {
/* 181 */     return String.format("Successfully installed client profile %s for version %s into launcher", new Object[] { VersionInfo.getProfileName(), VersionInfo.getVersion() });
/*     */   }
/*     */ }

/* Location:           C:\Users\User\Desktop\minecraftforge-installer-1.6.2-9.10.0.804.jar
 * Qualified Name:     cpw.mods.fml.installer.ClientInstall
 * JD-Core Version:    0.6.2
 */