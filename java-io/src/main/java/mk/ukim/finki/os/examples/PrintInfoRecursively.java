package mk.ukim.finki.os.examples;

import mk.ukim.finki.os.io.FileInfoPrinter;
import mk.ukim.finki.os.io.FileManager;
import mk.ukim.finki.os.io.filters.JavaClassesFilter;
import mk.ukim.finki.os.io.impl.FileManagerImpl;
import mk.ukim.finki.os.io.impl.FileNamePrinter;
import mk.ukim.finki.os.io.impl.FileNameWithoutDirectoryPrinter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Riste Stojanov
 */
public class PrintInfoRecursively {

  public static void main(String args[]) {
    FileManager fileManager = new FileManagerImpl();
    FileInfoPrinter printer = new FileNameWithoutDirectoryPrinter();
    FilenameFilter filter = new JavaClassesFilter();

    File workingDirectory = fileManager.workingDirectoryAsFile();
    printer.printInfo(System.out, workingDirectory);

    System.out.println("==========================================");
    fileManager.printFilteredDirectoryContentRecursively(
      workingDirectory,
      filter,
      printer,
      System.out
    );
    System.out.println("==========================================");
  }
}
