package ai;


public class GameState {

    private double score = 0;
    private int simulations = 0;

    public GameState() {

    }

    public GameState(double score) {
        simulations = 1;
        this.score = score;
    }

    public int getSimulations() {
        return simulations;
    }

    public void increaseScore(double inc) {
        ++simulations;
        score += inc;
    }


    /**
     * Upper Confidence Bound kiszámítása
     *
     * @param parentSimulations szülőállapot szimulációinak a száma
     * @param exploitation      exploitation paraméter
     * @return UCT
     */
    public double getUct(int parentSimulations, double exploitation) {
        return score / simulations + exploitation * Math.sqrt(Math.log(parentSimulations) / simulations);
    }

}
