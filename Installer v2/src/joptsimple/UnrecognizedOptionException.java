/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collections;
/*    */ 
/*    */ class UnrecognizedOptionException extends OptionException
/*    */ {
/*    */   UnrecognizedOptionException(String option)
/*    */   {
/* 39 */     super(Collections.singletonList(option));
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 44 */     return singleOptionMessage() + " is not a recognized option";
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.UnrecognizedOptionException
 * JD-Core Version:    0.6.2
 */