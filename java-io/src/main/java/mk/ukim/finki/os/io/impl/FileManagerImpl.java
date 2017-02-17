package mk.ukim.finki.os.io.impl;

import mk.ukim.finki.os.io.FileInfoPrinter;
import mk.ukim.finki.os.io.FileManager;
import mk.ukim.finki.os.io.exceptions.FileExistsException;
import mk.ukim.finki.os.io.exceptions.MissingPermissionException;
import mk.ukim.finki.os.io.exceptions.NotDirectoryException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Riste Stojanov
 */
public class FileManagerImpl implements FileManager {

  public String workingDirectoryAbsolutePath() {
    File file = new File(".");
    return file.getAbsolutePath();
  }

  public File workingDirectoryAsFile() {
    return new File(".");
  }

  public File parentDirectory(File file) {
    return file.getParentFile();
  }

  public boolean deleteDirectoryRecursively(File directory) {

    // This prevents NullPointerException after obtaining list of files with:
    // directory.listFiles()
    if (!directory.isDirectory()) {
      throw new NotDirectoryException(directory.getAbsolutePath());
    }

    // If the directory doesn't have write permission, its content cant be deleted
    if (!directory.canWrite()) {
      throw new MissingPermissionException("write", directory.getAbsolutePath());
    }
    File[] files = directory.listFiles();

    assert files != null;
    for (File contentFile : files) {
      if (contentFile.isDirectory()) {
        deleteDirectoryRecursively(contentFile);

      } else {
        boolean deleted = contentFile.delete();
        if (!deleted) {
          System.out.println("Can't delete file: " + contentFile.getAbsolutePath());
        }
      }
    }
    return directory.delete();
  }

  public boolean createDirectoryOnlyIfParentExists(String path) {
    File newDirectory = new File(path);
    return newDirectory.mkdir();
  }

  public boolean createDirectoryWithItsParents(String path) {
    File newDirectory = new File(path);
    return newDirectory.mkdirs();
  }

  public boolean createFile(String path) throws IOException {
    File file = new File(path);
    if (file.exists()) {
      throw new FileExistsException(file.getAbsolutePath());
    }
    return file.createNewFile();
  }

  public boolean renameFile(File file, String newName) {
    return moveAndRenameFile(file, file.getParent(), newName);
  }

  public boolean moveFile(File file, String newParent) {
    return moveAndRenameFile(file, newParent, file.getName());
  }

  public boolean moveAndRenameFile(File file, String newParent, String newName) {
    File parent = new File(newParent);
    if (!parent.isDirectory()) {
      throw new NotDirectoryException(parent.getAbsolutePath());
    }
    File renamedFile = new File(parent, newName);
    if (renamedFile.exists()) {
      throw new FileExistsException(renamedFile.getAbsolutePath());
    }
    return file.renameTo(renamedFile);
  }

  public void printFilteredDirectoryContentRecursively(
    File directory,
    FilenameFilter filter,
    FileInfoPrinter printer,
    PrintStream out) {


    String[] fileNames;

    if (filter != null) {
      fileNames = directory.list(filter);
    } else {
      fileNames = directory.list();
    }

    assert fileNames != null;
    for (String name : fileNames) {
      /*
      Cesta greska:
      new File(name); => {projectWorkingDir}/{name}
      a nie sakame:
      {directory}/{name}
       */
      File child = new File(directory, name);
      printer.printInfo(out, child);
      if (child.isDirectory()) {
        printFilteredDirectoryContentRecursively(child, filter, printer, out);
      }
    }
  }
}
