/*     */ package joptsimple;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import joptsimple.internal.AbbreviationMap;
/*     */ import joptsimple.util.KeyValuePair;
/*     */ 
/*     */ public class OptionParser
/*     */ {
/*     */   private final AbbreviationMap<AbstractOptionSpec<?>> recognizedOptions;
/*     */   private final Map<Collection<String>, Set<OptionSpec<?>>> requiredIf;
/*     */   private OptionParserState state;
/*     */   private boolean posixlyCorrect;
/*     */   private boolean allowsUnrecognizedOptions;
/* 200 */   private HelpFormatter helpFormatter = new BuiltinHelpFormatter();
/*     */ 
/*     */   public OptionParser()
/*     */   {
/* 207 */     this.recognizedOptions = new AbbreviationMap();
/* 208 */     this.requiredIf = new HashMap();
/* 209 */     this.state = OptionParserState.moreOptions(false);
/*     */ 
/* 211 */     recognize(new NonOptionArgumentSpec());
/*     */   }
/*     */ 
/*     */   public OptionSpecBuilder accepts(String option, String description)
/*     */   {
/* 265 */     return acceptsAll(Collections.singletonList(option), description);
/*     */   }
/*     */ 
/*     */   public OptionSpecBuilder acceptsAll(Collection<String> options, String description)
/*     */   {
/* 294 */     if (options.isEmpty()) {
/* 295 */       throw new IllegalArgumentException("need at least one option");
/*     */     }
/* 297 */     ParserRules.ensureLegalOptions(options);
/*     */ 
/* 299 */     return new OptionSpecBuilder(this, options, description);
/*     */   }
/*     */ 
/*     */   boolean doesAllowsUnrecognizedOptions()
/*     */   {
/* 358 */     return this.allowsUnrecognizedOptions;
/*     */   }
/*     */ 
/*     */   void recognize(AbstractOptionSpec<?> spec)
/*     */   {
/* 374 */     this.recognizedOptions.putAll(spec.options(), spec);
/*     */   }
/*     */ 
/*     */   public void printHelpOn(OutputStream sink)
/*     */     throws IOException
/*     */   {
/* 388 */     printHelpOn(new OutputStreamWriter(sink));
/*     */   }
/*     */ 
/*     */   public void printHelpOn(Writer sink)
/*     */     throws IOException
/*     */   {
/* 402 */     sink.write(this.helpFormatter.format(this.recognizedOptions.toJavaUtilMap()));
/* 403 */     sink.flush();
/*     */   }
/*     */ 
/*     */   public OptionSet parse(String[] arguments)
/*     */   {
/* 428 */     ArgumentList argumentList = new ArgumentList(arguments);
/* 429 */     OptionSet detected = new OptionSet(defaultValues());
/* 430 */     detected.add((AbstractOptionSpec)this.recognizedOptions.get("[arguments]"));
/*     */ 
/* 432 */     while (argumentList.hasMore()) {
/* 433 */       this.state.handleArgument(this, argumentList, detected);
/*     */     }
/* 435 */     reset();
/*     */ 
/* 437 */     ensureRequiredOptions(detected);
/*     */ 
/* 439 */     return detected;
/*     */   }
/*     */ 
/*     */   private void ensureRequiredOptions(OptionSet options) {
/* 443 */     Collection missingRequiredOptions = missingRequiredOptions(options);
/* 444 */     boolean helpOptionPresent = isHelpOptionPresent(options);
/*     */ 
/* 446 */     if ((!missingRequiredOptions.isEmpty()) && (!helpOptionPresent))
/* 447 */       throw new MissingRequiredOptionException(missingRequiredOptions);
/*     */   }
/*     */ 
/*     */   private Collection<String> missingRequiredOptions(OptionSet options) {
/* 451 */     Collection missingRequiredOptions = new HashSet();
/*     */ 
/* 453 */     for (AbstractOptionSpec each : this.recognizedOptions.toJavaUtilMap().values()) {
/* 454 */       if ((each.isRequired()) && (!options.has(each))) {
/* 455 */         missingRequiredOptions.addAll(each.options());
/*     */       }
/*     */     }
/* 458 */     for (Map.Entry eachEntry : this.requiredIf.entrySet()) {
/* 459 */       AbstractOptionSpec required = specFor((String)((Collection)eachEntry.getKey()).iterator().next());
/*     */ 
/* 461 */       if ((optionsHasAnyOf(options, (Collection)eachEntry.getValue())) && (!options.has(required))) {
/* 462 */         missingRequiredOptions.addAll(required.options());
/*     */       }
/*     */     }
/*     */ 
/* 466 */     return missingRequiredOptions;
/*     */   }
/*     */ 
/*     */   private boolean optionsHasAnyOf(OptionSet options, Collection<OptionSpec<?>> specs) {
/* 470 */     for (OptionSpec each : specs) {
/* 471 */       if (options.has(each)) {
/* 472 */         return true;
/*     */       }
/*     */     }
/* 475 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isHelpOptionPresent(OptionSet options) {
/* 479 */     boolean helpOptionPresent = false;
/* 480 */     for (AbstractOptionSpec each : this.recognizedOptions.toJavaUtilMap().values()) {
/* 481 */       if ((each.isForHelp()) && (options.has(each))) {
/* 482 */         helpOptionPresent = true;
/* 483 */         break;
/*     */       }
/*     */     }
/* 486 */     return helpOptionPresent;
/*     */   }
/*     */ 
/*     */   void handleLongOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
/* 490 */     KeyValuePair optionAndArgument = parseLongOptionWithArgument(candidate);
/*     */ 
/* 492 */     if (!isRecognized(optionAndArgument.key)) {
/* 493 */       throw OptionException.unrecognizedOption(optionAndArgument.key);
/*     */     }
/* 495 */     AbstractOptionSpec optionSpec = specFor(optionAndArgument.key);
/* 496 */     optionSpec.handleOption(this, arguments, detected, optionAndArgument.value);
/*     */   }
/*     */ 
/*     */   void handleShortOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
/* 500 */     KeyValuePair optionAndArgument = parseShortOptionWithArgument(candidate);
/*     */ 
/* 502 */     if (isRecognized(optionAndArgument.key)) {
/* 503 */       specFor(optionAndArgument.key).handleOption(this, arguments, detected, optionAndArgument.value);
/*     */     }
/*     */     else
/* 506 */       handleShortOptionCluster(candidate, arguments, detected);
/*     */   }
/*     */ 
/*     */   private void handleShortOptionCluster(String candidate, ArgumentList arguments, OptionSet detected) {
/* 510 */     char[] options = extractShortOptionsFrom(candidate);
/* 511 */     validateOptionCharacters(options);
/*     */ 
/* 513 */     for (int i = 0; i < options.length; i++) {
/* 514 */       AbstractOptionSpec optionSpec = specFor(options[i]);
/*     */ 
/* 516 */       if ((optionSpec.acceptsArguments()) && (options.length > i + 1)) {
/* 517 */         String detectedArgument = String.valueOf(options, i + 1, options.length - 1 - i);
/* 518 */         optionSpec.handleOption(this, arguments, detected, detectedArgument);
/* 519 */         break;
/*     */       }
/*     */ 
/* 522 */       optionSpec.handleOption(this, arguments, detected, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   void handleNonOptionArgument(String candidate, ArgumentList arguments, OptionSet detectedOptions) {
/* 527 */     specFor("[arguments]").handleOption(this, arguments, detectedOptions, candidate);
/*     */   }
/*     */ 
/*     */   void noMoreOptions() {
/* 531 */     this.state = OptionParserState.noMoreOptions();
/*     */   }
/*     */ 
/*     */   boolean isRecognized(String option)
/*     */   {
/* 539 */     return this.recognizedOptions.contains(option);
/*     */   }
/*     */ 
/*     */   private AbstractOptionSpec<?> specFor(char option)
/*     */   {
/* 563 */     return specFor(String.valueOf(option));
/*     */   }
/*     */ 
/*     */   private AbstractOptionSpec<?> specFor(String option) {
/* 567 */     return (AbstractOptionSpec)this.recognizedOptions.get(option);
/*     */   }
/*     */ 
/*     */   private void reset() {
/* 571 */     this.state = OptionParserState.moreOptions(this.posixlyCorrect);
/*     */   }
/*     */ 
/*     */   private static char[] extractShortOptionsFrom(String argument) {
/* 575 */     char[] options = new char[argument.length() - 1];
/* 576 */     argument.getChars(1, argument.length(), options, 0);
/*     */ 
/* 578 */     return options;
/*     */   }
/*     */ 
/*     */   private void validateOptionCharacters(char[] options) {
/* 582 */     for (char each : options) {
/* 583 */       String option = String.valueOf(each);
/*     */ 
/* 585 */       if (!isRecognized(option)) {
/* 586 */         throw OptionException.unrecognizedOption(option);
/*     */       }
/* 588 */       if (specFor(option).acceptsArguments())
/* 589 */         return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static KeyValuePair parseLongOptionWithArgument(String argument) {
/* 594 */     return KeyValuePair.valueOf(argument.substring(2));
/*     */   }
/*     */ 
/*     */   private static KeyValuePair parseShortOptionWithArgument(String argument) {
/* 598 */     return KeyValuePair.valueOf(argument.substring(1));
/*     */   }
/*     */ 
/*     */   private Map<String, List<?>> defaultValues() {
/* 602 */     Map defaults = new HashMap();
/* 603 */     for (Map.Entry each : this.recognizedOptions.toJavaUtilMap().entrySet())
/* 604 */       defaults.put(each.getKey(), ((AbstractOptionSpec)each.getValue()).defaultValues());
/* 605 */     return defaults;
/*     */   }
/*     */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.OptionParser
 * JD-Core Version:    0.6.2
 */