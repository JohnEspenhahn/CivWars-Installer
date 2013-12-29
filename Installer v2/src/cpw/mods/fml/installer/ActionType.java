package cpw.mods.fml.installer;

import java.io.File;

public abstract interface ActionType
{
  public abstract boolean run(File paramFile);

  public abstract boolean isPathValid(File paramFile);

  public abstract String getFileError(File paramFile);

  public abstract String getSuccessMessage();

  public abstract String getSponsorMessage();
}

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     cpw.mods.fml.installer.ActionType
 * JD-Core Version:    0.6.2
 */