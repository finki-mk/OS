package mk.ukim.finki.os.io.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Riste Stojanov
 */
public class JavaClassesFilter implements FilenameFilter {

  public boolean accept(File dir, String name) {
    File file=new File(dir, name);
    return
      file.isDirectory() ||
      file.isFile() && name.endsWith(".java");
  }
}
