package mk.ukim.finki.os.synchronization;

import java.util.HashMap;
import java.util.Map;

public class PointsException extends RuntimeException {

	private static HashMap<String, PointsException> exceptions = new HashMap<String, PointsException>();
	private int points;

	public PointsException(int points, String message) {
		super(message);
		this.points = points;
		exceptions.put(message, this);
	}

	public static int getTotalPoints() {
		int sum = 0;
		for (PointsException e : exceptions.values()) {
			sum += e.getPoints();
		}
		return sum;
	}

	public static void printErrors() {
		if (!exceptions.isEmpty()) {
			System.out.println("Gi imate slednite greski: ");
			for (Map.Entry<String, PointsException> e : exceptions.entrySet()) {
				System.out.println(String.format("[%s] : (-%d)", e.getKey(), e
						.getValue().getPoints()));
			}
		}
	}

	public int getPoints() {
		return points;
	}
}
