package modele;

import java.util.Observable;

public class Environnement extends Observable implements Runnable {
    public Environnement(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        tab = new Case[sizeX][sizeY];
        for(int i = 0 ; i < sizeX ; i++) {
            for(int j = 0 ; j < sizeY ; j++) {
                tab[i][j] = new Case();
            }
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean getState(int x, int y) {
        return tab[x][y].getState();
    }

    public Case getCase(Case source, Direction d) {
        // TODO : une case utilisera ogligatoirement cette fonction pour percevoir son environnement, et définir son état suivant
        return null;
    }

    public void rndState() {
        for(int i = 0 ; i < sizeX ; i++) {
            for(int j = 0 ; j < sizeY ; j++) {
                tab[i][j].rndState();
            }
        }
    }

    @Override
    public void run() {
        rndState();
        // notification de l'observer
        setChanged();
        notifyObservers();
    }

    private Case[][] tab;
    private int sizeX;
    private int sizeY;
}
