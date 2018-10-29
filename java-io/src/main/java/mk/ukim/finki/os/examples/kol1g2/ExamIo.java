package mk.ukim.finki.os.examples.kol1g2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public class ExamIo {

  public static void main(String[] args) throws IOException {
    File f = new File(".");
    System.out.println(f.getAbsolutePath());
    ExamIo instance = new ExamIo();
    instance.copyLargeTxtFiles("unexisting", "data/to", 20);

    instance.copyLargeTxtFiles("data/CsvToOrderedBinaryDatabase.csv", "data/to", 20);


    instance.copyLargeTxtFiles("data/from", "data/to", 20);

    List<byte[]> data = new ArrayList<>();
    data.add("123".getBytes());
    data.add("234".getBytes());
    data.add("456".getBytes());
    data.add("789".getBytes());
    data.add("901".getBytes());

    instance.serializeData("data/dest.bin", data);
    byte[] el = instance.deserializeDataAtPosition("data/dest.bin", 3, 3);
    System.out.println(new String(el));

    el = instance.deserializeDataAtPosition("data/dest.bin", 1, 3);
    System.out.println(new String(el));


  }


  public void serializeData(String destination, List<byte[]> data) throws IOException {
    File f = new File(destination);
    System.out.println(f.getAbsolutePath());
    f.createNewFile();
    try (FileOutputStream fos = new FileOutputStream(f, false)) {
      for (byte[] el : data) {
        for (byte b : el) {
          fos.write((int) b);
        }
      }
    }
  }

  public byte[] deserializeDataAtPosition(String source,
                                          long position,
                                          long elementLength) throws IOException {
    try (
      RandomAccessFile raf = new RandomAccessFile(source, "r")
    ) {
      raf.seek(position * elementLength);
      byte[] el = new byte[(int) elementLength];

      int elPos = 0;
      while (elPos < elementLength) {
        el[elPos] = (byte) raf.read();
        if (el[elPos] == -1) {
          throw new IllegalStateException("Not enough bytes for the element");
        }
        elPos++;
      }
      return el;
    }
  }

  public void copyLargeTxtFiles(String from,
                                String to,
                                long size) {

    try {
      File fromDir = validateFrom(from);

      File toDir = createUnexistingTo(to);

      moveLargeTxtFiles(fromDir, toDir, size);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }

  }

  private void moveLargeTxtFiles(File fromDir, File toDir, long size) {
    File[] largeTxtFiles = fromDir.listFiles(new FileFilter() {

      @Override
      public boolean accept(File childFile) {
        if (childFile.isFile() &&
          childFile.getName().endsWith(".txt") &&
          childFile.length() > size) {
          return true;
        } else if (childFile.isDirectory()) {
          moveLargeTxtFiles(childFile, toDir, size);
        }
        return false;
      }
    });

    for (File f : largeTxtFiles) {
      copyFileContent(f, toDir);
    }

  }

  private void copyFileContent(File f, File toDir) {
    try (
      FileInputStream fis = new FileInputStream(f);
      FileOutputStream fos = new FileOutputStream(new File(toDir, f.getName()))
    ) {
      int b;
      while ((b = fis.read()) != -1) {
        fos.write(b);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File createUnexistingTo(String to) {
    File toDir = new File(to);
    if (!toDir.exists()) {
      toDir.mkdirs();
    } else if (!toDir.isDirectory()) {
      throw new IllegalStateException(to + " ne e direktorium");
    }

    return toDir;
  }

  private File validateFrom(String from) {

    File fromDir = new File(from);
    if (!fromDir.exists()) {

      throw new IllegalArgumentException("Ne postoi: " + fromDir.getAbsolutePath());
    }
    if (!fromDir.isDirectory()) {
      throw new IllegalArgumentException("Ne e direktorium");

    }
    return fromDir;
  }
}
