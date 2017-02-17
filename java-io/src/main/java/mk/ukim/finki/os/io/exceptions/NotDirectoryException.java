package mk.ukim.finki.os.io.exceptions;

import java.io.IOException;

/**
 * @author Riste Stojanov
 */
public class NotDirectoryException extends RuntimeException {

  public NotDirectoryException(String absolutePath) {
    super(
      String.format(
        "'%s' is not directory!",
        absolutePath
      )
    );
  }
}
