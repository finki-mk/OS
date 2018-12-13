package mk.ukim.finki.os.synchronization.exam18.kol1.g1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author Riste Stojanov
 */
public class Solution {

  static Semaphore barrier = new Semaphore(0);
  static Semaphore semaphore = new Semaphore(15);

  static List<int[][]> matrixQueue = new ArrayList<>();

  static class FileScanner extends Thread {

    private static final List<File> matrixFiles = new ArrayList<>();
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
          synchronized (matrixFiles) {
            matrixFiles.addAll(scanner.matrixFiles);
          }
        }
        System.out.println("Done scanning: " + directoryToScan.getAbsolutePath());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static class Reader extends Thread {
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
      // todo: The variable in should provide the readLine() method
      try (BufferedReader s = new BufferedReader(new FileReader(new File(matrixFile)))) {

        int n = Integer.parseInt(s.readLine().trim());
        this.matrix = new int[n][n];

        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            matrix[i][j] = Integer.parseInt(s.readLine().trim());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static class Writer extends Thread {

    private final String outputPath;
    private final int[][] matrix;

    Writer(String outputPath, int[][] matrix) {
      this.outputPath = outputPath;
      this.matrix = matrix;
    }


    @Override
    public void run() {
      int n = matrix.length;
      try (BufferedWriter dos = new BufferedWriter(new FileWriter(outputPath))) {
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            // todo: write the element
            dos.write(""+matrix[i][j]);
            dos.write("\n");
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static class Transformer extends Thread {
    private final int[][] matrix;
    private final int row;
    private final int column;

    private int result;

    Transformer(int[][] matrix, int row, int column) {
      this.matrix = matrix;
      this.row = row;
      this.column = column;
    }

    @Override
    public void run() {
      try {
        // todo: allow maximum 15 parallel executions
        semaphore.acquire();
        int n = matrix.length;
        for (int k = 0; k < n; k++) {
          result += matrix[row][k] * matrix[k][column];
        }
        semaphore.release();
        barrier.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    List<Transformer> transformers = new ArrayList<>();

    Reader reader = new Reader("data/m.mat");
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
    barrier.acquire(n*n);

    int[][] result = new int[n][n];
    for (Transformer t : transformers) {
      result[t.row][t.column] = t.result;
    }

    Writer writer = new Writer("data/out.txt", result);
    // todo: execute file writing in background
    writer.start();

    FileScanner scanner = new FileScanner(new File("data"));
    // todo: execute file scanning in background
    scanner.start();
    // todo: wait for the scanner to finish and show the results
    scanner.join();
    for (File matrixFile : scanner.matrixFiles) {
      System.out.println(matrixFile.getAbsolutePath());
    }
  }


}
