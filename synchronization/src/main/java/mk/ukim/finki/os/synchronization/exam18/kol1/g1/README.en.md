Matrix multiplication
===

1. **(5 points)** Implement the thread `Reader` that will read the content of the file provided as the property 
`matrixFile` in background. The code for matrix reading is given in the `run()` method. You need to complete this code and
provide a variable `in` that will have the `readLine()` method for reading the content of the file. 

2. **(5 points)** Complete the `Writer` class such that it will behave as thread and will write in background the element 
values of `matrix` into the file `outputPath`, one element per line. 

3. **(5 points)** Complete the `Transformer` class such that it will behave as thread and will compute the result in 
background. It is not allowed more that 15 parallel executions of the given code in the `run()` method. 

4. **(10 points)** Implement the class `FileScanner` that will behave as a *thread*. This class should recursively scan 
the directory given in the property `directoryToScan` and should find all the files with extension `.mat`. If the directory 
`directoryToScan` does not exist, you should provide appropriate message. The recursive scanning should be executed 
in separate thread instances of the class `FileScanner`. At the end of the `directoryToScan` scanning, the `FileScanner`
should waid the results form all `subDirectory` threads. 

5. **(20 points)** Complete the `MainG1.main` method according to `//todo:` comments.

6. **(5 points)** Ensure mutual exclusion for all critical regions.    

The starter code that contains all previously mentioned classes is the follows: 

```java
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
      // todo: The variable in should provide the readLine() method
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
    try {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          // todo: write the element matrix[i][j]
        }
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
    // todo: allow maximum 15 parallel executions
    int n = matrix.length;
    for (int k = 0; k < n; k++) {
      result += matrix[row][k] * matrix[k][column];
    }
  }
}

class FileScanner extends Thread {

  final File directoryToScan;
  final List<File> matrixFiles = new ArrayList<>();

  FileScanner(File directoryToScan) {
    this.directoryToScan = directoryToScan;
  }

  public void run() {
    try {
      List<FileScanner> scanners = new ArrayList<>();

      // todo: find *.mat files and add them in the matrixFiles list
      matrixFiles.add(matFile);

      // todo: for each sub-directory, create a new instance of FileScanner
      FileScanner fs = new FileScanner(subDirectory);
      scanners.add(fs);
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


    // todo: wait for all transformers to finish

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
``` 

