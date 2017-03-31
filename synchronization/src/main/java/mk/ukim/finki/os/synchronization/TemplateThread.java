package mk.ukim.finki.os.synchronization;

import java.util.Date;

public abstract class TemplateThread extends Thread {

  static boolean hasException = false;
  public int iteration = 0;
  protected Exception exception = null;
  int numRuns = 1;

  public TemplateThread(int numRuns) {
    this.numRuns = numRuns;
  }

  public abstract void execute() throws InterruptedException;

  @Override
  public void run() {
    try {
      for (int i = 0; i < numRuns && !hasException; i++) {
        execute();
        iteration++;

      }
    } catch (InterruptedException e) {
      // Do nothing
    } catch (Exception e) {
      exception = e;
      e.printStackTrace();
      hasException = true;
    }
  }

  public void setException(Exception exception) {
    this.exception = exception;
    hasException = true;
  }

  @Override
  public String toString() {
    Thread current = Thread.currentThread();
    if (numRuns > 1) {
      return String.format("[%d]%s\t%d\t%d", new Date().getTime(), ""
          + current.getClass().getSimpleName().charAt(0), getId(),
        iteration);
    } else {
      return String.format("[%d]%s\t%d\t", new Date().getTime(), ""
        + current.getClass().getSimpleName().charAt(0), getId());
    }
  }
}
