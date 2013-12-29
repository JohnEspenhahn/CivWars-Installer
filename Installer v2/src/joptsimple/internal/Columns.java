/*     */ package joptsimple.internal;
/*     */ 
/*     */ import java.text.BreakIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ class Columns
/*     */ {
/*     */   private final int optionWidth;
/*     */   private final int descriptionWidth;
/*     */ 
/*     */   Columns(int optionWidth, int descriptionWidth)
/*     */   {
/*  47 */     this.optionWidth = optionWidth;
/*  48 */     this.descriptionWidth = descriptionWidth;
/*     */   }
/*     */ 
/*     */   List<Row> fit(Row row) {
/*  52 */     List options = piecesOf(row.option, this.optionWidth);
/*  53 */     List descriptions = piecesOf(row.description, this.descriptionWidth);
/*     */ 
/*  55 */     List rows = new ArrayList();
/*  56 */     for (int i = 0; i < Math.max(options.size(), descriptions.size()); i++) {
/*  57 */       rows.add(new Row(itemOrEmpty(options, i), itemOrEmpty(descriptions, i)));
/*     */     }
/*  59 */     return rows;
/*     */   }
/*     */ 
/*     */   private static String itemOrEmpty(List<String> items, int index) {
/*  63 */     return index >= items.size() ? "" : (String)items.get(index);
/*     */   }
/*     */ 
/*     */   private List<String> piecesOf(String raw, int width) {
/*  67 */     List pieces = new ArrayList();
/*     */ 
/*  69 */     for (String each : raw.trim().split(Strings.LINE_SEPARATOR)) {
/*  70 */       pieces.addAll(piecesOfEmbeddedLine(each, width));
/*     */     }
/*  72 */     return pieces;
/*     */   }
/*     */ 
/*     */   private List<String> piecesOfEmbeddedLine(String line, int width) {
/*  76 */     List pieces = new ArrayList();
/*     */ 
/*  78 */     BreakIterator words = BreakIterator.getLineInstance(Locale.US);
/*  79 */     words.setText(line);
/*     */ 
/*  81 */     StringBuilder nextPiece = new StringBuilder();
/*     */ 
/*  83 */     int start = words.first();
/*  84 */     for (int end = words.next(); end != -1; end = words.next()) {
/*  85 */       nextPiece = processNextWord(line, nextPiece, start, end, width, pieces);
/*     */ 
/*  84 */       start = end;
/*     */     }
/*     */ 
/*  87 */     if (nextPiece.length() > 0) {
/*  88 */       pieces.add(nextPiece.toString());
/*     */     }
/*  90 */     return pieces;
/*     */   }
/*     */ 
/*     */   private StringBuilder processNextWord(String source, StringBuilder nextPiece, int start, int end, int width, List<String> pieces)
/*     */   {
/*  95 */     StringBuilder augmented = nextPiece;
/*     */ 
/*  97 */     String word = source.substring(start, end);
/*  98 */     if (augmented.length() + word.length() > width) {
/*  99 */       pieces.add(augmented.toString().replaceAll("\\s+$", ""));
/* 100 */       augmented = new StringBuilder(Strings.repeat(' ', 2)).append(word);
/*     */     }
/*     */     else {
/* 103 */       augmented.append(word);
/*     */     }
/* 105 */     return augmented;
/*     */   }
/*     */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.internal.Columns
 * JD-Core Version:    0.6.2
 */