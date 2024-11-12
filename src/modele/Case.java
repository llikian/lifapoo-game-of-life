package modele;

import java.util.Random;

public class Case {
    public boolean getState() {
        return state;
    }

    public void rndState() {
        state = rnd.nextBoolean();
    }

    public void nextState() {
        // TODO
        // calcul de l'état suivant.
        // Utiliser la fonction getCase de Environnement
    }

    private static final Random rnd = new Random();
    private boolean state;
}
