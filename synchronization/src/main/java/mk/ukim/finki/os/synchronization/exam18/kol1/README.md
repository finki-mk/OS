Прв колоквиум
===

1. Да се имплементира класа `FileScanner` која ќе се однесува како *thread*. Оваа класа треба да изврши рекурзивно 
скенирање на именикот проследен преку својството `directoryToScan` и треба да ги пронајде сите датотеки со екстензија 
`.mat`. Доколку именикот `directoryToScan` не постои треба да се испише соодветна порака. Рекурзивното скенирање да 
се изврши со посебни thread-ови од класата `FileScanner`. Почетниот код за оваа класа е во продолжение: 

```java
  static class FileScanner {
    private final java.io.File directoryToScan;
    private java.util.List<File> matrixFiles=new java.util.ArrayList<>();
    FileScanner(java.io.File directoryToScan) {
      this.directoryToScan = directoryToScan;
    }
    // todo: implement recursive directory scan
  }
```  

2. Имплементирајте thread `Reader` кој во позадина ќе ја вчита содржината на бинарната датотека проследена преку 
својството `matrixFile`. Кодот за вчитување на матрицата е даден во методот `run()`. Потребно е да го дополните 
овој код за да може да се вчитаат матриците, при што типот на `in` променливата треба да го овозможува методот
`readInt()`. 


```java
  static class Reader {
    private final String matrixFile;
    int[][] matrix;
    Reader(String matrixFile) {
      this.matrixFile = matrixFile;
    }
    /** 
    * This method should execute in background
    */ 
    public void run() {
        // todo: complete this method according to the text description
        // todo: The variable in should provide the readInt() method
        int n = in.readInt();
        this.matrix = new int[n][n];
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            matrix[i][j] = s.readInt();
          }
        }
    }
  }
```


3. Дополнете ја `Writer` класата така што таа ќе се однесува како thread и во позадина ќе ги запише вредностите на елементите 
од матрицата `matrix` во датотеката `outputPath`. 

```java
static class Writer {
    private final String outputPath;
    private final int[][] matrix;
    Writer(String outputPath, int[][] matrix) {
      this.outputPath = outputPath;
      this.matrix = matrix;
    }
    public void run() {
      int n = matrix.length;
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            // todo: write the element
          }
        }
    }
  }
```

4. Дополнете ја `Transformer` класата да се однесува како thread и во позадина на го пресмета резултатот. Притоа, 
не треба да дозволите повеќе од 10 паралелни извршувања на дадениот код во `run()` методот. 

```java
static class Transformer {
    private final int[][] matrix;
    private final int row;
    private final int column;
    private int result;
    Transformer(int[][] matrix, int row, int column) {
      this.matrix = matrix;
      this.row = row;
      this.column = column;
    }
    public void run() {
      // todo: allow maximum 10 parallel executions
      int n = matrix.length;
      for (int k = 0; k < n; k++) {
        result += matrix[row][k] * matrix[k][column];
      }
    }
  }
```
5. Дополнете го `main` методот според упатството во `//todo:` коментарите. 

```java
public class Main {
public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    List<Transformer> transformers = new ArrayList<>();
    Reader reader = new Reader("data/matrix.mat");
    // todo: execute file reading in background
    
    // todo: wait for the matrix to be read
    
    // transform the matrix
    int n = reader.matrix.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        Transformer t = new Transformer(reader.matrix, i, j);
        transformers.add(t);
        // todo: execute the background transformation
        
      }
    }
    // todo: wait for all transformers to finish
    
    int[][] result = new int[n][n];
    for (Transformer t : transformers) {
      result[t.row][t.column] = t.result;
    }
    Writer writer = new Writer("data/out/matrix2.bin", result);
    // todo: execute file writing in background

    FileScanner scanner = new FileScanner(new File("data"));
    // todo: execute file scanning in background
    
    // todo: show the results
    System.out.println(scanner.matrixFiles);
  }
}
```