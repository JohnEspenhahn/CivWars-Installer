package cpw.mods.fml.installer;

public abstract interface IMonitor
{
  public abstract void setMaximum(int paramInt);

  public abstract void setNote(String paramString);

  public abstract void setProgress(int paramInt);

  public abstract void close();
}

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     cpw.mods.fml.installer.IMonitor
 * JD-Core Version:    0.6.2
 */