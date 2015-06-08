package eu.w4.contrib.genactors.cli;

public class CliRuntimeException extends RuntimeException {

  public CliRuntimeException() {
    super();
  }

  public CliRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CliRuntimeException(String message) {
    super(message);
  }

  public CliRuntimeException(Throwable cause) {
    super(cause);
  }

}
