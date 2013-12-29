/*    */ package joptsimple.internal;
/*    */ 
/*    */ class Row
/*    */ {
/*    */   final String option;
/*    */   final String description;
/*    */ 
/*    */   Row(String option, String description)
/*    */   {
/* 36 */     this.option = option;
/* 37 */     this.description = description;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object that)
/*    */   {
/* 42 */     if (that == this)
/* 43 */       return true;
/* 44 */     if ((that == null) || (!getClass().equals(that.getClass()))) {
/* 45 */       return false;
/*    */     }
/* 47 */     Row other = (Row)that;
/* 48 */     return (this.option.equals(other.option)) && (this.description.equals(other.description));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 53 */     return this.option.hashCode() ^ this.description.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.internal.Row
 * JD-Core Version:    0.6.2
 */