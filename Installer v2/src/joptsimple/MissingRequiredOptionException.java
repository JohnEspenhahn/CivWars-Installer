/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
/*    */ 
/*    */ class MissingRequiredOptionException extends OptionException
/*    */ {
/*    */   protected MissingRequiredOptionException(Collection<String> options)
/*    */   {
/* 39 */     super(options);
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 44 */     return "Missing required option(s) " + multipleOptionMessage();
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.MissingRequiredOptionException
 * JD-Core Version:    0.6.2
 */