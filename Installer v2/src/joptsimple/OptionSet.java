/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class OptionSet
/*     */ {
/*     */   private final List<OptionSpec<?>> detectedSpecs;
/*     */   private final Map<String, AbstractOptionSpec<?>> detectedOptions;
/*     */   private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
/*     */   private final Map<String, List<?>> defaultValues;
/*     */ 
/*     */   OptionSet(Map<String, List<?>> defaults)
/*     */   {
/*  53 */     this.detectedSpecs = new ArrayList();
/*  54 */     this.detectedOptions = new HashMap();
/*  55 */     this.optionsToArguments = new IdentityHashMap();
/*  56 */     this.defaultValues = new HashMap(defaults);
/*     */   }
/*     */ 
/*     */   public boolean has(OptionSpec<?> option)
/*     */   {
/*  93 */     return this.optionsToArguments.containsKey(option);
/*     */   }
/*     */ 
/*     */   public List<OptionSpec<?>> specs()
/*     */   {
/* 234 */     List specs = this.detectedSpecs;
/* 235 */     specs.remove(this.detectedOptions.get("[arguments]"));
/*     */ 
/* 237 */     return Collections.unmodifiableList(specs);
/*     */   }
/*     */ 
/*     */   void add(AbstractOptionSpec<?> spec)
/*     */   {
/* 248 */     addWithArgument(spec, null);
/*     */   }
/*     */ 
/*     */   void addWithArgument(AbstractOptionSpec<?> spec, String argument) {
/* 252 */     this.detectedSpecs.add(spec);
/*     */ 
/* 254 */     for (String each : spec.options()) {
/* 255 */       this.detectedOptions.put(each, spec);
/*     */     }
/* 257 */     List optionArguments = (List)this.optionsToArguments.get(spec);
/*     */ 
/* 259 */     if (optionArguments == null) {
/* 260 */       optionArguments = new ArrayList();
/* 261 */       this.optionsToArguments.put(spec, optionArguments);
/*     */     }
/*     */ 
/* 264 */     if (argument != null)
/* 265 */       optionArguments.add(argument);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 270 */     if (this == that) {
/* 271 */       return true;
/*     */     }
/* 273 */     if ((that == null) || (!getClass().equals(that.getClass()))) {
/* 274 */       return false;
/*     */     }
/* 276 */     OptionSet other = (OptionSet)that;
/* 277 */     Map thisOptionsToArguments = new HashMap(this.optionsToArguments);
/*     */ 
/* 279 */     Map otherOptionsToArguments = new HashMap(other.optionsToArguments);
/*     */ 
/* 281 */     return (this.detectedOptions.equals(other.detectedOptions)) && (thisOptionsToArguments.equals(otherOptionsToArguments));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 287 */     Map thisOptionsToArguments = new HashMap(this.optionsToArguments);
/*     */ 
/* 289 */     return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.OptionSet
 * JD-Core Version:    0.6.2
 */