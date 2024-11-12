package modele;

import static java.lang.Thread.*;

public class Ordonnanceur extends Thread {
    public Ordonnanceur(long sleepTime, Runnable runnable) {
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
