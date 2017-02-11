package mk.ukim.finki.os.io;

import java.io.File;

/**
 *
 * @author Riste Stojanov
 */
public interface FileManager {

  String workingDirectoryAbsolutePath();

  File workingDirectoryAsFile();

  File parentDirectory(File file);

  void deleteDirectoryRecursively(File directory);

  void createDirectoryOnlyIfParentExists(String path);

  void createDirectoryWithItsParents(String path);

}
