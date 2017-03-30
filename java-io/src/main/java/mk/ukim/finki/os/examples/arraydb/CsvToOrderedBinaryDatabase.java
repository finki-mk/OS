package mk.ukim.finki.os.examples.arraydb;

import java.io.*;

/**
 * @author Riste Stojanov
 */
public class CsvToOrderedBinaryDatabase {

  private static final int ROW_NUMBER_BYTES = 8; // 1 long x 8 bytes
  private static final int VALUE_BYTES = 8; // 8 chars x 1 byte
  private static final int STATUS_BYTES = 1; // 1 char x 1 byte
  private static final int ADDITIONAL_CONTROL_BYTES = 4; // 2 for each utf string
  private static final long ELEMENT_SIZE = ROW_NUMBER_BYTES + VALUE_BYTES + STATUS_BYTES + ADDITIONAL_CONTROL_BYTES;
  private static String CSV_FILE = "data/CsvToOrderedBinaryDatabase.csv";
  private static String DB_FILE = "data/CsvToOrderedBinaryDatabase.osdb";

  public static void main(String[] args) throws IOException {
    parseCsv(CSV_FILE, DB_FILE);

  }

  public static void parseCsv(String csvFile, String binFile) throws IOException {
    // Java 8 try with resources that replaces the try-finally block
    // see: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
    try (
      BufferedReader csvReader = openCsv(csvFile);
      RandomAccessFile randomAccessFile = createOrOpenDatabase(binFile)
    ) {
      String line;
      // one line at a time in memory. OK until the line is not too long
      // we have specification that the line is 13 characters + semicolons and spaces. It will fit in memory :)
      while ((line = csvReader.readLine()) != null) {
        saveLine(line, randomAccessFile);
      }
    }
    // how can we validate ourselves?
    printDatabase(binFile);
  }


  private static BufferedReader openCsv(String csvFile) throws FileNotFoundException {
    File file = new File(csvFile);
    if (!file.exists()) {
      throw new IllegalStateException("csv file not found! absolutePath: "
        + file.getAbsolutePath());
    }
    return new BufferedReader(new FileReader(file));
  }

  private static RandomAccessFile createOrOpenDatabase(String binFile) throws FileNotFoundException {
    return new RandomAccessFile(binFile, "rw");
  }

  private static void saveLine(String line, RandomAccessFile randomAccessFile) throws IOException {
    //TODO: change the code to write it in the right place

    String[] elements = validateAndGetElements(line);
    //ADVICE: Always use trim() before parsing type (unless white spaces are expected)
    Long rowNumber = Long.parseLong(
      elements[0].trim() // .trim removes the invisible characters at the beginning and at the end of the string
    );
    String value = elements[1].trim();
    String status = elements[2].trim();
    validateElements(value, status);

    randomAccessFile.seek(rowNumber*ELEMENT_SIZE);

    randomAccessFile.writeLong(rowNumber);
    // if the characters are cyrillic, there will be 2 bytes per character
    System.out.println("Value length: " + value.getBytes().length);

    // Documentation snippet from DataOutputStream.writeUTF:
    // First, two bytes are written to out as if by the <code>writeShort</code>
    // method giving the number of bytes to follow ...
    randomAccessFile.writeUTF(value);
    System.out.println("Status length: " + status.getBytes().length);
    randomAccessFile.writeUTF(status);
  }

  private static String[] validateAndGetElements(String line) {
    String[] elements = line.split(",");
    if (elements.length != 3) {
      throw new IllegalArgumentException("Line does not have exactly 3 elements! line: " + line);
    }
    return elements;
  }

  private static void validateElements(String value, String status) {
    if (value.length() != 8) {
      throw new IllegalArgumentException("Invalid value length! expected 8 characters. value=" + value);
    }
    if (status.length() != 1) {
      throw new IllegalArgumentException("Invalid value length! expected 1 characters. status=" + status);
    }
  }


  private static void printDatabase(String binFile) throws IOException {
    try (
      RandomAccessFile randomAccessFile = new RandomAccessFile(binFile, "r")
    ) {
      Long numberOfElements = randomAccessFile.length() / ELEMENT_SIZE;
      System.out.println("file size: " + randomAccessFile.length());
      int index = 0;
      System.out.println("number of elements: " + numberOfElements);
      while (index < numberOfElements) {
        printElement(randomAccessFile, index);
        index++;
      }
    }
  }

  private static void printElement(RandomAccessFile randomAccessFile, int index) throws IOException {
    byte[] valueBytes = new byte[VALUE_BYTES];
    byte[] statusBytes = new byte[STATUS_BYTES];

    // go to the position of the element
    randomAccessFile.seek(index * ELEMENT_SIZE);

    //read from the random access file
    Long rowNumber = randomAccessFile.readLong();

    String value = randomAccessFile.readUTF();
    String status = randomAccessFile.readUTF();
    System.out.println("file pointer: " + randomAccessFile.getFilePointer());

    System.out.println(rowNumber + ", " + value + ", " + status);
  }


}