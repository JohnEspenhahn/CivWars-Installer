/*    */ package joptsimple;
/*    */ 
/*    */ abstract class OptionParserState
/*    */ {
/*    */   static OptionParserState noMoreOptions()
/*    */   {
/* 38 */     return new OptionParserState()
/*    */     {
/*    */       protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
/* 41 */         parser.handleNonOptionArgument(arguments.next(), arguments, detectedOptions);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   static OptionParserState moreOptions(final boolean posixlyCorrect) {
/* 47 */     return new OptionParserState()
/*    */     {
/*    */       protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
/* 50 */         String candidate = arguments.next();
/*    */         try {
/* 52 */           if (ParserRules.isOptionTerminator(candidate)) {
/* 53 */             parser.noMoreOptions();
/* 54 */             return;
/* 55 */           }if (ParserRules.isLongOptionToken(candidate)) {
/* 56 */             parser.handleLongOptionToken(candidate, arguments, detectedOptions);
/* 57 */             return;
/* 58 */           }if (ParserRules.isShortOptionToken(candidate)) {
/* 59 */             parser.handleShortOptionToken(candidate, arguments, detectedOptions);
/* 60 */             return;
/*    */           }
/*    */         } catch (UnrecognizedOptionException e) {
/* 63 */           if (!parser.doesAllowsUnrecognizedOptions()) {
/* 64 */             throw e;
/*    */           }
/*    */         }
/* 67 */         if (posixlyCorrect) {
/* 68 */           parser.noMoreOptions();
/*    */         }
/* 70 */         parser.handleNonOptionArgument(candidate, arguments, detectedOptions);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   protected abstract void handleArgument(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet);
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.OptionParserState
 * JD-Core Version:    0.6.2
 */