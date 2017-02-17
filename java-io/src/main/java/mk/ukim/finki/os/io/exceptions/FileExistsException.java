package mk.ukim.finki.os.io.exceptions;

/**
 * @author Riste Stojanov
 */
public class FileExistsException extends RuntimeException {

  public FileExistsException(String absolutePath) {
    super(
      String.format(
        "'%s' already exists!",
        absolutePath
      )
    );
  }
}
