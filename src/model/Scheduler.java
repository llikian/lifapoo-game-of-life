package model;

/**
 * Handles the refresh rate at which a runnable object gets updated.
 * <p>
 * Attributes:<br>
 * - runnable: a reference to a callable.<br>
 * - sleepTime: the time between each execution.<br>
 * - pause: true if the threads are paused, false if not.
 */
public class Scheduler extends Thread {
    private final Runnable runnable;
    private long sleepTime;
    private boolean pause;

    /**
     * Creates a scheduler.
     *
     * @param sleepTime The refresh rate.
     * @param runnable  The object that gets updated.
     */
    public Scheduler(long sleepTime, Runnable runnable) {
        this.sleepTime = sleepTime;
        this.runnable = runnable;
        this.pause = false;
    }

    /**
     * Changes the refresh rate.
     *
     * @param sleepTime The new refresh rate.
     */
    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * Returns the refresh rate.
     */
    public long getSleepTime() {
        return sleepTime;
    }

    /**
     * @return A boolean representing wether the scheduler is paused.
     */
    public boolean isPaused() {
        return pause;
    }

    /**
     * Pauses or unpauses the scheduler.
     */
    public void togglePause() {
        pause = !pause;
    }

    /**
     * Contains the main loop. Updates the runnable object if the sceduler isn't paused and sleeps
     * for the length of the refresh time before updating again.
     */
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
