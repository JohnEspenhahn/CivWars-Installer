/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
/*    */ 
/*    */ final class ParserRules
/*    */ {
/* 39 */   static final String HYPHEN = String.valueOf('-');
/*    */ 
/*    */   static boolean isShortOptionToken(String argument)
/*    */   {
/* 49 */     return (argument.startsWith(HYPHEN)) && (!HYPHEN.equals(argument)) && (!isLongOptionToken(argument));
/*    */   }
/*    */ 
/*    */   static boolean isLongOptionToken(String argument)
/*    */   {
/* 55 */     return (argument.startsWith("--")) && (!isOptionTerminator(argument));
/*    */   }
/*    */ 
/*    */   static boolean isOptionTerminator(String argument) {
/* 59 */     return "--".equals(argument);
/*    */   }
/*    */ 
/*    */   static void ensureLegalOption(String option) {
/* 63 */     if (option.startsWith(HYPHEN)) {
/* 64 */       throw new IllegalOptionSpecificationException(String.valueOf(option));
/*    */     }
/* 66 */     for (int i = 0; i < option.length(); i++)
/* 67 */       ensureLegalOptionCharacter(option.charAt(i));
/*    */   }
/*    */ 
/*    */   static void ensureLegalOptions(Collection<String> options) {
/* 71 */     for (String each : options)
/* 72 */       ensureLegalOption(each);
/*    */   }
/*    */ 
/*    */   private static void ensureLegalOptionCharacter(char option) {
/* 76 */     if ((!Character.isLetterOrDigit(option)) && (!isAllowedPunctuation(option)))
/* 77 */       throw new IllegalOptionSpecificationException(String.valueOf(option));
/*    */   }
/*    */ 
/*    */   private static boolean isAllowedPunctuation(char option) {
/* 81 */     String allowedPunctuation = "?.-";
/* 82 */     return allowedPunctuation.indexOf(option) != -1;
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.ParserRules
 * JD-Core Version:    0.6.2
 */