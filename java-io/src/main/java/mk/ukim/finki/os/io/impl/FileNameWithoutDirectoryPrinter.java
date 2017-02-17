package mk.ukim.finki.os.io.impl;

import mk.ukim.finki.os.io.FileInfoPrinter;

import java.io.File;
import java.io.PrintStream;

/**
 * @author Riste Stojanov
 */
public class FileNameWithoutDirectoryPrinter implements FileInfoPrinter {

  public void printInfo(PrintStream out, File file) {
    if (file.isFile()) {
      out.println(file.getAbsolutePath());
    }
  }
}
