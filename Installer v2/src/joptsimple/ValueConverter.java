package joptsimple;

public abstract interface ValueConverter<V>
{
  public abstract Class<V> valueType();

  public abstract String valuePattern();
}

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.ValueConverter
 * JD-Core Version:    0.6.2
 */