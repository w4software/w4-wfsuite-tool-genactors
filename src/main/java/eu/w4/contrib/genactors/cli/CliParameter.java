package eu.w4.contrib.genactors.cli;

/**
 * Command Line Interface Option
 *
 */
public class CliParameter {

  /** name */
  private final String name;
  /** description */
  private final String description;
  /** default value */
  private final String defaultValue;
  /** single flag or option with value? */
  private final boolean flag;
  /** is required? */
  private final boolean required;


  public CliParameter(final String name, final String description, final String defaultValue, final boolean flag, final boolean required) {
    super();
    this.name = name;
    this.description = description;
    this.defaultValue = defaultValue;
    this.flag = flag;
    this.required = required;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean isFlag() {
    return flag;
  }

}
