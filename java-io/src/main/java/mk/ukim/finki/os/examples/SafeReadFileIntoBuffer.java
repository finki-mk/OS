package mk.ukim.finki.os.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;

/**
 * @author Riste Stojanov
 */
public class SafeReadFileIntoBuffer {

  public static void main(String[] args) throws IOException {
    InputStream inputStream = new FileInputStream(
      new File("data.txt")
    );

    byte[] data = new byte[100];
    int index = 0;
    int current;

    while ((current = inputStream.read()) != -1) {
      if (index == data.length) {
        throw new BufferOverflowException();
      }
      // This is ok :)
      // Only one byte is read by inputStream.read() in range between 0-255, so the cast is safe
      data[index] = (byte) current;
      index++;
    }


    System.out.printf("Successfully read %d bytes!\n", index);
    System.out.println("The content of the file is: ");
    System.out.println(new String(data, 0, index));
  }
}
