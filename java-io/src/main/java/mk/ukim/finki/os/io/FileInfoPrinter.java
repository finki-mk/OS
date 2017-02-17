package mk.ukim.finki.os.io;

import java.io.File;
import java.io.PrintStream;

/**
 * @author Riste Stojanov
 */
public interface FileInfoPrinter {

  void printInfo(PrintStream out, File file);

}
