import model.Environment;
import model.Scheduler;
import view_controller.Window;

import javax.swing.SwingUtilities;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Environment environment = new Environment(100, 50);
                Scheduler scheduler = new Scheduler(500, environment);
                Window window = new Window(environment, scheduler);

                environment.addObserver(window);

                scheduler.start();
            }
        });
    }
}
