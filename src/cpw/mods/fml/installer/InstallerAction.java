/*    */ package cpw.mods.fml.installer;
/*    */ 
/*    */ import com.google.common.base.Throwables;
/*    */ import java.io.File;
/*    */ 
/*    */ public enum InstallerAction
/*    */ {
/* 11 */   CLIENT("Install client", "Install a new profile to the Mojang client launcher", ClientInstall.class);
/*    */ 
/*    */   private String label;
/*    */   private String tooltip;
/*    */   private ActionType action;
/*    */ 
/* 21 */   private InstallerAction(String label, String tooltip, Class<? extends ActionType> action) { this.label = label;
/* 22 */     this.tooltip = tooltip;
/*    */     try
/*    */     {
/* 25 */       this.action = ((ActionType)action.newInstance());
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 29 */       throw Throwables.propagate(e);
/*    */     } }
/*    */ 
/*    */   public String getButtonLabel()
/*    */   {
/* 34 */     return this.label;
/*    */   }
/*    */ 
/*    */   public String getTooltip()
/*    */   {
/* 39 */     return this.tooltip;
/*    */   }
/*    */ 
/*    */   public boolean run(File path)
/*    */   {
/* 44 */     return this.action.run(path);
/*    */   }
/*    */ 
/*    */   public boolean isPathValid(File targetDir) {
/* 48 */     return this.action.isPathValid(targetDir);
/*    */   }
/*    */ 
/*    */   public String getFileError(File targetDir)
/*    */   {
/* 53 */     return this.action.getFileError(targetDir);
/*    */   }
/*    */ 
/*    */   public String getSuccessMessage() {
/* 57 */     return this.action.getSuccessMessage();
/*    */   }
/*    */ }

/* Location:           C:\Users\User\Desktop\minecraftforge-installer-1.6.2-9.10.0.804.jar
 * Qualified Name:     cpw.mods.fml.installer.InstallerAction
 * JD-Core Version:    0.6.2
 */