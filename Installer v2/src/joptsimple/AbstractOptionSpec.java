/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class AbstractOptionSpec<V>
/*     */   implements OptionDescriptor, OptionSpec<V>
/*     */ {
/*  44 */   private final List<String> options = new ArrayList();
/*     */   private final String description;
/*     */   private boolean forHelp;
/*     */ 
/*     */   protected AbstractOptionSpec(Collection<String> options, String description)
/*     */   {
/*  53 */     arrangeOptions(options);
/*     */ 
/*  55 */     this.description = description;
/*     */   }
/*     */ 
/*     */   public final Collection<String> options() {
/*  59 */     return Collections.unmodifiableList(this.options);
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  71 */     return this.description;
/*     */   }
/*     */ 
/*     */   public final boolean isForHelp()
/*     */   {
/*  80 */     return this.forHelp;
/*     */   }
/*     */ 
/*     */   public boolean representsNonOptions() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   protected String argumentTypeIndicatorFrom(ValueConverter<V> converter)
/*     */   {
/* 102 */     if (converter == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     String pattern = converter.valuePattern();
/* 106 */     return pattern == null ? converter.valueType().getName() : pattern;
/*     */   }
/*     */ 
/*     */   abstract void handleOption(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet, String paramString);
/*     */ 
/*     */   private void arrangeOptions(Collection<String> unarranged)
/*     */   {
/* 113 */     if (unarranged.size() == 1) {
/* 114 */       this.options.addAll(unarranged);
/* 115 */       return;
/*     */     }
/*     */ 
/* 118 */     List shortOptions = new ArrayList();
/* 119 */     List longOptions = new ArrayList();
/*     */ 
/* 121 */     for (String each : unarranged) {
/* 122 */       if (each.length() == 1)
/* 123 */         shortOptions.add(each);
/*     */       else {
/* 125 */         longOptions.add(each);
/*     */       }
/*     */     }
/* 128 */     Collections.sort(shortOptions);
/* 129 */     Collections.sort(longOptions);
/*     */ 
/* 131 */     this.options.addAll(shortOptions);
/* 132 */     this.options.addAll(longOptions);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 137 */     if (!(that instanceof AbstractOptionSpec)) {
/* 138 */       return false;
/*     */     }
/* 140 */     AbstractOptionSpec other = (AbstractOptionSpec)that;
/* 141 */     return this.options.equals(other.options);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 146 */     return this.options.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     return this.options.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.AbstractOptionSpec
 * JD-Core Version:    0.6.2
 */