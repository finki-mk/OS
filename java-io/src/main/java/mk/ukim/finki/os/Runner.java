package mk.ukim.finki.os;

import mk.ukim.finki.os.io.FileManager;
import mk.ukim.finki.os.io.impl.FileManagerImpl;

import java.io.File;

/**
 * @author Riste Stojanov
 */
public class Runner {

  public static void main(String[] args) {
    FileManager manager = new FileManagerImpl();

    String workingDirectoryAbsolutePath =
      manager.workingDirectoryAbsolutePath();
    System.out.println(workingDirectoryAbsolutePath);

    File currentDirectory =
      manager.workingDirectoryAsFile();

    System.out.println(currentDirectory);

    File parentDirectory =
      manager.parentDirectory(currentDirectory);

    System.out.println(parentDirectory);
    System.out.println(manager
      .parentDirectory(parentDirectory));


    File currentDirectoryChild = new File(currentDirectory, "subdir/child");
    System.out.println("child exists? " + currentDirectoryChild.exists());
    if (currentDirectoryChild.exists()) {
      boolean status = currentDirectoryChild.delete();
      System.out.println("Delete status: " + status);
    }
    currentDirectoryChild.mkdirs();
    System.out.println("child exists after create? " + currentDirectoryChild.exists());
    System.out.println(currentDirectoryChild);

    boolean changeWritableStatus = currentDirectoryChild.setWritable(false);
    System.out.println("Writable status: "+changeWritableStatus);

    System.out.println("write: "+currentDirectoryChild.canWrite());


    boolean deleteStatus = manager.deleteDirectoryRecursively(
      manager.parentDirectory(currentDirectoryChild)
    );
    System.out.println("Delete status: " + deleteStatus);
  }
}
