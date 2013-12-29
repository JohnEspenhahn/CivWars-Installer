/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ class NoArgumentOptionSpec extends AbstractOptionSpec<Void>
/*    */ {
/*    */   NoArgumentOptionSpec(Collection<String> options, String description)
/*    */   {
/* 44 */     super(options, description);
/*    */   }
/*    */ 
/*    */   void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument)
/*    */   {
/* 51 */     detectedOptions.add(this);
/*    */   }
/*    */ 
/*    */   public boolean acceptsArguments() {
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean requiresArgument() {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean isRequired() {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public String argumentDescription() {
/* 67 */     return "";
/*    */   }
/*    */ 
/*    */   public String argumentTypeIndicator() {
/* 71 */     return "";
/*    */   }
/*    */ 
/*    */   public List<Void> defaultValues()
/*    */   {
/* 80 */     return Collections.emptyList();
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.NoArgumentOptionSpec
 * JD-Core Version:    0.6.2
 */