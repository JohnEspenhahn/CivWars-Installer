package cpw.mods.fml.installer;

import java.io.File;

public abstract interface ActionType
{
  public abstract boolean run(File paramFile);

  public abstract boolean isPathValid(File paramFile);

  public abstract String getFileError(File paramFile);

  public abstract String getSuccessMessage();
}

/* Location:           C:\Users\User\Desktop\minecraftforge-installer-1.6.2-9.10.0.804.jar
 * Qualified Name:     cpw.mods.fml.installer.ActionType
 * JD-Core Version:    0.6.2
 */