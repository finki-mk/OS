package mk.ukim.finki.os.synchronization.exam18.kol1.g2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author Riste Stojanov
 */

class FileScanner extends Thread {

  static final List<File> matrixFiles = new ArrayList<>();
  private final File directoryToScan;

  FileScanner(File directoryToScan) {
    this.directoryToScan = directoryToScan;
  }

  @Override
  public void run() {
    try {
      List<FileScanner> scanners = new ArrayList<>();
      File[] files = directoryToScan.listFiles();
      for (File file : files) {

        if (file.isFile() &&
          file.getName().endsWith(".mat")) {
          synchronized (matrixFiles) {
            matrixFiles.add(file);
          }
        }
        if (file.isDirectory()) {
          FileScanner fs = new FileScanner(file);
          scanners.add(fs);
          fs.start();
        }

      }
      for (FileScanner scanner : scanners) {
        scanner.join();
      }
      System.out.println("Done scanning: " + directoryToScan.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class Reader extends Thread {
  private final String matrixFile;
  int[][] matrix;

  Reader(String matrixFile) {
    this.matrixFile = matrixFile;
  }

  /**
   * This method should execute in background
   */
  @Override
  public void run() {
    // todo: complete this method according to the text description
    // todo: The variable in should provide the readInt() method
    try (DataInputStream s = new DataInputStream(new FileInputStream(new File(matrixFile)))) {

      int n = s.readInt();
      this.matrix = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = s.readInt();
          System.out.print(matrix[i][j] + " ");
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class Writer extends Thread {

  private final String outputPath;
  private final int[][] matrix;

  Writer(String outputPath, int[][] matrix) {
    this.outputPath = outputPath;
    this.matrix = matrix;
  }


  @Override
  public void run() {
    int n = matrix.length;
    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputPath))) {
      dos.writeInt(n);
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          // todo: write the element
          System.out.print(matrix[i][j] + " ");
          dos.writeInt(matrix[i][j]);
        }
        System.out.println();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class Transformer extends Thread {
  final int row;
  final int column;
  private final int[][] matrix;
  int result;

  Transformer(int[][] matrix, int row, int column) {
    this.matrix = matrix;
    this.row = row;
    this.column = column;
  }

  @Override
  public void run() {
    try {
      // todo: allow maximum 10 parallel executions
      MatrixMultiplication.semaphore.acquire();
      int n = matrix.length;
      for (int k = 0; k < n; k++) {
        result += matrix[row][k] * matrix[k][column];
      }
      MatrixMultiplication.semaphore.release();
      System.out.print(".");
      MatrixMultiplication.barrier.release();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

public class MatrixMultiplication {

  static Semaphore barrier = new Semaphore(0);
  static Semaphore semaphore = new Semaphore(10);

  public static void main1(String[] args) {
    int n = 30;
    int[][] matrix = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        matrix[i][j] = 1;
      }
    }
    Writer w = new Writer("data/matrix.mat", matrix);
    w.run();
    System.out.println("\n==============================================================================================");
    Reader r = new Reader("data/matrix.mat");
    r.run();
  }


  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    List<Transformer> transformers = new ArrayList<>();

    Reader reader = new Reader("data/matrix.mat");
    // todo: execute file reading in background
    reader.start();

    // todo: wait for the matrix to be read
    reader.join();

    // todo: transform the matrix
    int n = reader.matrix.length;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        Transformer t = new Transformer(reader.matrix, i, j);
        transformers.add(t);
        // todo: start the background execution
        t.start();
      }
    }


    // todo: wait for all transformers to finish
    barrier.acquire(n * n);

    System.out.println("==============================================================================================");
    int[][] result = new int[n][n];
    for (Transformer t : transformers) {
      result[t.row][t.column] = t.result;
    }

    Writer writer = new Writer("data/out.bin", result);
    // todo: execute file writing in background
    writer.start();

    FileScanner scanner = new FileScanner(new File("data"));
    // todo: execute file scanning in background
    scanner.start();
    // todo: wait for the scanner to finish and show the results
    scanner.join();
    for (File matrixFile : scanner.matrixFiles) {
//      System.out.println(matrixFile.getAbsolutePath());
    }

  }


}
