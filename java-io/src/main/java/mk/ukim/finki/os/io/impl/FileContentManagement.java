package mk.ukim.finki.os.io.impl;

import java.io.*;

/**
 * @author Riste Stojanov
 */
public class FileContentManagement {

    public static final int CAPACITY = 4096;

    public static void main(String[] args) throws IOException {
        File workingDirecotry = new File("");
        System.out.println(workingDirecotry.getAbsolutePath());

        File dataIn = new File("data.txt");
        System.out.println("data exists? " + dataIn.exists());

        File out = new File("out");
        out.mkdirs();
        File dataOut = new File(out, "dataOut.txt");
        dataOut.getAbsolutePath();

        copy(dataIn, dataOut);
        String content = readTextFile(dataOut);

        System.err.println(content);

    }

    public static void copy(File in, File out) throws IOException {

        try (
                FileInputStream fis = new FileInputStream(in);
                FileOutputStream fos = new FileOutputStream(out, false)
        ) {
            int c;
            while ((c = fis.read()) != -1) {
                fos.write(c);
            }
        }

    }

    public static String readTextFile(File file) throws IOException {
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), "UTF-8"
                        )
                )
        ) {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        }
    }

    public static int readChunkInBuffer(InputStream fis, int[] buffer, int capacity) throws IOException {

        int c;
        int i = 0;
        while ((c = fis.read()) != -1) {
            if (i >= capacity) {
                return capacity;
            }
            buffer[i] = c;
            i++;
        }
        return i;

    }

    public static void processChunks(File file) throws IOException {
        int[] buffer = new int[CAPACITY];
        int read;
        try (
                FileInputStream fis = new FileInputStream(file)
        ) {
            while ((read = readChunkInBuffer(fis, buffer, CAPACITY)) != -1) {
                processBuffer(buffer, read);
            }
        }
    }

    private static void processBuffer(int[] buffer, int read) {

    }

}
