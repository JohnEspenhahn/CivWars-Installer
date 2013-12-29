/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class OptionSpecBuilder extends NoArgumentOptionSpec
/*    */ {
/*    */   private final OptionParser parser;
/*    */ 
/*    */   OptionSpecBuilder(OptionParser parser, Collection<String> options, String description)
/*    */   {
/* 64 */     super(options, description);
/*    */ 
/* 66 */     this.parser = parser;
/* 67 */     attachToParser();
/*    */   }
/*    */ 
/*    */   private void attachToParser() {
/* 71 */     this.parser.recognize(this);
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.OptionSpecBuilder
 * JD-Core Version:    0.6.2
 */