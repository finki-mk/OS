package mk.ukim.finki.os.synchronization.exam18.kol1.g1;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


class Reader extends Thread {
  final String matrixFile;
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

    try {
      // todo: The variable in should provide the readInt() method
      DataInputStream in = null;
      int n = in.readInt();
      System.out.println("Reading matrix: ");
      this.matrix = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = in.readInt();
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
    System.out.println("Writing matrix:");
    try {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          // todo: write the element matrix[i][j]
          System.out.print(matrix[i][j] + " ");
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class Transformer extends Thread {
  final int[][] matrix;
  final int row;
  final int column;

  int result;

  Transformer(int[][] matrix, int row, int column) {
    this.matrix = matrix;
    this.row = row;
    this.column = column;
  }

  @Override
  public void run() {
    // todo: allow maximum 10 parallel executions
    int n = matrix.length;
    for (int k = 0; k < n; k++) {
      result += matrix[row][k] * matrix[k][column];
    }
  }
}

class FileScanner extends Thread {

  final static List<File> matrixFiles = new ArrayList<>();
  final File directoryToScan;

  FileScanner(File directoryToScan) {
    this.directoryToScan = directoryToScan;
  }

  public void run() {
    try {
      List<FileScanner> scanners = new ArrayList<>();

      // todo: find *.mat files and add them in the matrixFiles list
//      matrixFiles.add(matFile);

      // todo: for each sub-directory, create a new instance of FileScanner
//      FileScanner fs = new FileScanner(subDirectory);
//      scanners.add(fs);
      //todo: invoke the scanning of the subDirectory in background

      for (FileScanner scanner : scanners) {
        //todo: wait for the scanner to finish

      }
      System.out.println("Done scanning: " + directoryToScan.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


public class MainG1 {

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    List<Transformer> transformers = new ArrayList<>();

    Reader reader = new Reader("data/matrix.mat");
    // todo: execute file reading in background

    // todo: wait for the matrix to be read

    // todo: transform the matrix
    int n = reader.matrix.length;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        Transformer t = new Transformer(reader.matrix, i, j);
        transformers.add(t);
        // todo: start the background execution

      }
    }


    // todo: wait for all transformers to finish at once

    int[][] result = new int[n][n];
    for (Transformer t : transformers) {
      result[t.row][t.column] = t.result;
    }

    Writer writer = new Writer("data/out.bin", result);
    // todo: execute file writing in background

    FileScanner scanner = new FileScanner(new File("data"));
    // todo: execute file scanning in background

    // todo: wait for the scanner to finish and show the results

    for (File matrixFile : scanner.matrixFiles) {
      System.out.println(matrixFile.getAbsolutePath());
    }

  }
}
