/*    */ package joptsimple;
/*    */ 
/*    */ class ArgumentList
/*    */ {
/*    */   private final String[] arguments;
/*    */   private int currentIndex;
/*    */ 
/*    */   ArgumentList(String[] arguments)
/*    */   {
/* 40 */     this.arguments = ((String[])arguments.clone());
/*    */   }
/*    */ 
/*    */   boolean hasMore() {
/* 44 */     return this.currentIndex < this.arguments.length;
/*    */   }
/*    */ 
/*    */   String next() {
/* 48 */     return this.arguments[(this.currentIndex++)];
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.ArgumentList
 * JD-Core Version:    0.6.2
 */