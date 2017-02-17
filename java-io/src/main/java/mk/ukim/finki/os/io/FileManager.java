package mk.ukim.finki.os.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Riste Stojanov
 */
public interface FileManager {

  String workingDirectoryAbsolutePath();

  File workingDirectoryAsFile();

  File parentDirectory(File file);

  boolean deleteDirectoryRecursively(File directory);

  boolean createDirectoryOnlyIfParentExists(String path);

  boolean createDirectoryWithItsParents(String path);

  boolean createFile(String path) throws IOException;

  boolean renameFile(File file, String newName);

  boolean moveFile(File file, String newParent);

  boolean moveAndRenameFile(File file, String newParent, String newName);

  void printFilteredDirectoryContentRecursively(
    File directory,
    FilenameFilter filter,
    FileInfoPrinter printer,
    PrintStream out
  );

}
