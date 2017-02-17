package mk.ukim.finki.os.io.impl;

import mk.ukim.finki.os.io.FileInfoPrinter;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author Riste Stojanov
 */
public class FileNamePrinter implements FileInfoPrinter {

  public void printInfo(PrintStream out, File file) {
    out.println(file.getAbsolutePath());
  }
}
