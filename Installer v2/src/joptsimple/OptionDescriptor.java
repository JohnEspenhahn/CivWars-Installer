package joptsimple;

import java.util.Collection;
import java.util.List;

public abstract interface OptionDescriptor
{
  public abstract Collection<String> options();

  public abstract String description();

  public abstract List<?> defaultValues();

  public abstract boolean isRequired();

  public abstract boolean acceptsArguments();

  public abstract boolean requiresArgument();

  public abstract String argumentDescription();

  public abstract String argumentTypeIndicator();

  public abstract boolean representsNonOptions();
}

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.OptionDescriptor
 * JD-Core Version:    0.6.2
 */