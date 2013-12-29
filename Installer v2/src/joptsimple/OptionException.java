/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class OptionException extends RuntimeException
/*    */ {
/* 45 */   private final List<String> options = new ArrayList();
/*    */ 
/*    */   protected OptionException(Collection<String> options) {
/* 48 */     this.options.addAll(options);
/*    */   }
/*    */ 
/*    */   protected final String singleOptionMessage()
/*    */   {
/* 67 */     return singleOptionMessage((String)this.options.get(0));
/*    */   }
/*    */ 
/*    */   protected final String singleOptionMessage(String option) {
/* 71 */     return "'" + option + "'";
/*    */   }
/*    */ 
/*    */   protected final String multipleOptionMessage() {
/* 75 */     StringBuilder buffer = new StringBuilder("[");
/*    */ 
/* 77 */     for (Iterator iter = this.options.iterator(); iter.hasNext(); ) {
/* 78 */       buffer.append(singleOptionMessage((String)iter.next()));
/* 79 */       if (iter.hasNext()) {
/* 80 */         buffer.append(", ");
/*    */       }
/*    */     }
/* 83 */     buffer.append(']');
/*    */ 
/* 85 */     return buffer.toString();
/*    */   }
/*    */ 
/*    */   static OptionException unrecognizedOption(String option) {
/* 89 */     return new UnrecognizedOptionException(option);
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.OptionException
 * JD-Core Version:    0.6.2
 */