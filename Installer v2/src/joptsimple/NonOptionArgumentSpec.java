/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class NonOptionArgumentSpec<V> extends AbstractOptionSpec<V>
/*     */ {
/*     */   private ValueConverter<V> converter;
/*  57 */   private String argumentDescription = "";
/*     */ 
/*     */   NonOptionArgumentSpec() {
/*  60 */     this("");
/*     */   }
/*     */ 
/*     */   NonOptionArgumentSpec(String description) {
/*  64 */     super(Arrays.asList(new String[] { "[arguments]" }), description);
/*     */   }
/*     */ 
/*     */   void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument)
/*     */   {
/* 140 */     detectedOptions.addWithArgument(this, detectedArgument);
/*     */   }
/*     */ 
/*     */   public List<?> defaultValues() {
/* 144 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   public boolean isRequired() {
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean acceptsArguments() {
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean requiresArgument() {
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   public String argumentDescription() {
/* 160 */     return this.argumentDescription;
/*     */   }
/*     */ 
/*     */   public String argumentTypeIndicator() {
/* 164 */     return argumentTypeIndicatorFrom(this.converter);
/*     */   }
/*     */ 
/*     */   public boolean representsNonOptions() {
/* 168 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.NonOptionArgumentSpec
 * JD-Core Version:    0.6.2
 */