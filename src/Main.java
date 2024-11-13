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
                Environment environment = new Environment(10, 10);

                Window window = new Window(environment);
                window.setVisible(true);

                environment.addObserver(window);

                Scheduler o = new Scheduler(500, environment);
                o.start();
            }
        });
    }
}
