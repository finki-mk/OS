package mk.ukim.finki.os.examples.charcount;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public class CharCount {

  public static final int zeroAscii = '0';

  public static void main(String[] args) throws IOException {

    CharCount charCount = new CharCount();
    List<byte[]> result = charCount.read("data/charcount/data.txt");

    for (byte[] element : result) {
      System.out.print("[");
      for (byte b : element) {
        System.out.print(b - zeroAscii);
        System.out.print(" ");
      }
      System.out.println("]");
    }


  }

  public List<byte[]> read(String input) throws IOException {

    List<byte[]> result = new ArrayList<>();

    try (InputStream is = new FileInputStream(input)) {
      int lenByte, len;
      while ((lenByte = is.read()) != -1) {
        len = lenByte - zeroAscii;
        System.out.println("len: " + len);
        byte[] element = readElement(is, len);
        result.add(element);
      }

    }

    return result;
  }

  private byte[] readElement(InputStream is, int len) throws IOException {
    byte[] element = new byte[len];

    for (int i = 0; i < len; i++) {
      int b = is.read();
      if (b == -1) {
        throw new IllegalStateException("Nema " + len + " bajti vo fajlot za da se procita element");
      }
      element[i] = (byte) b;
    }
    return element;
  }


}
