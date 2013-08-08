/*    */ package cpw.mods.fml.installer;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import javax.swing.JOptionPane;
/*    */ 
/*    */ public class SimpleInstaller
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws IOException
/*    */   {
/* 24 */     String userHomeDir = System.getProperty("user.home", ".");
/* 25 */     String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
/* 26 */     File targetDir = null;
/* 27 */     String mcDir = ".minecraft";
/* 28 */     if ((osType.contains("win")) && (System.getenv("APPDATA") != null))
/*    */     {
/* 30 */       targetDir = new File(System.getenv("APPDATA"), mcDir);
/*    */     }
/* 32 */     else if (osType.contains("mac"))
/*    */     {
/* 34 */       targetDir = new File(new File(new File(userHomeDir, "Library"), "Application Support"), "minecraft");
/*    */     }
/*    */     else
/*    */     {
/* 38 */       targetDir = new File(userHomeDir, mcDir);
/*    */     }
/*    */ 
/*    */     try
/*    */     {
/* 43 */       VersionInfo.getVersionTarget();
/*    */     }
/*    */     catch (Throwable e)
/*    */     {
/* 47 */       e.printStackTrace();
/* 48 */       JOptionPane.showMessageDialog(null, "Corrupt download detected, cannot install", "Error", 0);
/* 49 */       return;
/*    */     }
/*    */ 
/* 52 */     InstallerPanel panel = new InstallerPanel(targetDir);
/* 53 */     panel.run();
/*    */   }
/*    */ }

/* Location:           C:\Users\User\Desktop\minecraftforge-installer-1.6.2-9.10.0.804.jar
 * Qualified Name:     cpw.mods.fml.installer.SimpleInstaller
 * JD-Core Version:    0.6.2
 */