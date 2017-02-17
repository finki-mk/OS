package mk.ukim.finki.os.io.exceptions;

/**
 * @author Riste Stojanov
 */
public class MissingPermissionException extends RuntimeException {

  public MissingPermissionException(String permission, String absolutePath) {
    super(
      String.format(
        "'%s' does not have %s permission!",
        absolutePath,
        permission
      )
    );
  }
}
