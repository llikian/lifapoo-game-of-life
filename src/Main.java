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
                Environment environment = new Environment(50, 50);

                Window window = new Window(environment);
                window.setVisible(true);

                environment.addObserver(window);

                Scheduler scheduler = new Scheduler(500, environment);
                scheduler.start();
            }
        });
    }
}
