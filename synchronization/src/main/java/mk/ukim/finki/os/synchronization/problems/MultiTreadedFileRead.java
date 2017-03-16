package mk.ukim.finki.os.synchronization.problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import mk.ukim.finki.os.synchronization.Switcher;

public class MultiTreadedFileRead {

	static class MyFileReader implements Runnable {

		private BufferedReader br;
		private int x;

		public MyFileReader(BufferedReader br, int x) {
			this.br = br;
			this.x = x;
		}

		@Override
		public void run() {
			String s = new String();
			try {
				char[] c = new char[1];
				while (br.read(c) != -1) {
					s += c[0];
					Switcher.forceSwitch(5);
				}
				synchronized (MyFileReader.class) {
					System.out.println("++++++++++++++++++++++++++++");
					System.out.println(s);
					System.out.println("++++++++++++++++++++++++++++");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader("resources/text.txt"));
			int x = 5;
			Runnable r1 = new MyFileReader(br, x);
			Thread t1 = new Thread(r1);
			Thread t2 = new Thread(new MyFileReader(br, x));

			t1.start();
			t2.start();

			t1.join();
			t2.join();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}

	}

}
