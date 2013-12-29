/*    */ package joptsimple.internal;
/*    */ 
/*    */ public final class Strings
/*    */ {
/* 40 */   public static final String LINE_SEPARATOR = System.getProperty("line.separator");
/*    */ 
/*    */   public static String repeat(char ch, int count)
/*    */   {
/* 54 */     StringBuilder buffer = new StringBuilder();
/*    */ 
/* 56 */     for (int i = 0; i < count; i++) {
/* 57 */       buffer.append(ch);
/*    */     }
/* 59 */     return buffer.toString();
/*    */   }
/*    */ 
/*    */   public static boolean isNullOrEmpty(String target)
/*    */   {
/* 69 */     return (target == null) || ("".equals(target));
/*    */   }
/*    */ 
/*    */   public static String surround(String target, char begin, char end)
/*    */   {
/* 82 */     return begin + target + end;
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.internal.Strings
 * JD-Core Version:    0.6.2
 */