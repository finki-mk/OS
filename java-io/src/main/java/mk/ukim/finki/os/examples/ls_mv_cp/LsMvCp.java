package mk.ukim.finki.os.examples.ls_mv_cp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public class LsMvCp {



  public static void main(String[] args) throws IOException {

    new LsMvCp().work("data/ls_mv_cp/in", "data/ls_mv_cp/out");
  }

  private void work(String in, String out) throws IOException {

    File inDir = new File(in);
    File outDir = new File(out);

    if (!inDir.exists()) {
      System.out.println("prazno");
      return;
    }

    if (!outDir.exists()) {
      outDir.mkdirs();
    } else {
      removeDirectoryContent(outDir);
    }

    processDirectory(inDir, outDir);


  }

  private void processDirectory(File inDir, File outDir) throws IOException {
    File[] children = inDir.listFiles();


    List<File> childrenFiles = obtainFilesOrProcessDirectory(children, outDir);

    if (childrenFiles.size() > 5) {
      moveFiles(childrenFiles, outDir);
    } else if (childrenFiles.size() < 5) {
      copyFiles(childrenFiles, outDir);
    }

  }

  private List<File> obtainFilesOrProcessDirectory(File[] children, File outDir) throws IOException {
    List<File> childrenFiles = new ArrayList<>();
    for (File file : children) {
      if (file.isFile()) {
        childrenFiles.add(file);
      } else if (file.isDirectory()) {
        processDirectory(file, outDir);
      }
    }
    return childrenFiles;
  }

  private void copyFiles(List<File> children, File outDir) throws IOException {
    for (File file : children) {
      File destination = new File(outDir, file.getName());
      copy(file, destination);
    }
  }

  private void copy(File file, File destination) throws IOException {
    try (
      InputStream is = new FileInputStream(file);
      OutputStream os = new FileOutputStream(destination, false)
    ) {
      int b;
      while ((b = is.read()) != -1) {
        os.write(b);
      }
    }
  }

  private void moveFiles(List<File> children, File outDir) {
    for (File file : children) {
      File destination = new File(outDir, file.getName());
      file.renameTo(destination);
    }
  }

  private void removeDirectoryContent(File dir) {
    File[] files = dir.listFiles();
    for (File f : files) {
      f.delete();
    }
  }
}
