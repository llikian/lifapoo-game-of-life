package model;

public class Scheduler extends Thread {
    private long sleepTime;
    private Runnable runnable;

    public Scheduler(long sleepTime, Runnable runnable) {
        this.sleepTime = sleepTime;
        this.runnable = runnable;
    }

    public void run() {
        while(true) {
            runnable.run();

            try {
                sleep(sleepTime);
            } catch(InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}
