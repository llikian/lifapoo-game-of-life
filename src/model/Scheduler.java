package model;

public class Scheduler extends Thread {
    public Scheduler(long sleepTime, Runnable runnable) {
        this.sleepTime = sleepTime;
        this.runnable = runnable;
    }

    public void run() {
        while(true) {
            runnable.run();

            try {
                sleep(sleepTime);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long sleepTime;
    private Runnable runnable;
}
