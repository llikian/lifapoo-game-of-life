package model;

public class Scheduler extends Thread {
    private final Runnable runnable;
    private long sleepTime;
    private boolean pause;

    public Scheduler(long sleepTime, Runnable runnable) {
        this.sleepTime = sleepTime;
        this.runnable = runnable;
        this.pause = false;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public boolean isPaused() {
        return pause;
    }

    public void togglePause() {
        pause = !pause;
    }

    public void run() {
        while(true) {
            if(!pause) {
                runnable.run();
            }

            try {
                sleep(sleepTime);
            } catch(InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}
