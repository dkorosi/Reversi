package ai;

import gamelogic.Board;
import gamelogic.Coordinate;
import gamelogic.Player;
import gamelogic.TileType;

import java.util.*;

/**
 * Mesterséges intelligencia a játékhoz
 */
public class AiPlayer extends Player {

    private int difficulty;
    private Random rand = new Random();

    public AiPlayer(String name, TileType color, int timer, int difficulty) {
        super(name, color, timer);
        this.difficulty = difficulty;
    }

    @Override
    public void makeMove(Board board) {
        List<Coordinate> moves = board.getValidCoordinates();
        if (moves.isEmpty() || board.getCurrent() != getColor())
            return;

        int totalSimulations = 100;
        int simulationsPerMove = totalSimulations / moves.size();

        Map<Coordinate, Integer> moveScores = new HashMap<>();
        for (Coordinate move : moves) {
            int score = 0;
            for (int i = 0; i < simulationsPerMove; i++) {
                Board simBoard = new Board(board.getBoard(), getColor());
                simBoard.makeMoveAt(getColor(), move);

                while (simBoard.isActive()) {
                    makeRandomMove(simBoard);
                }

                TileType winner = simBoard.getWinning();
                if (winner == getColor()) {
                    score += 1;
                } else if (winner == getColor().enemyTileType())
                    score -= 1;
            }
            moveScores.put(move, score);
        }

        Coordinate move = getMaximumScoreMove(moveScores);
        board.makeMoveAt(getColor(), move);
    }

    private void makeRandomMove(Board board) {
        List<Coordinate> moves = board.getValidCoordinates();

        Coordinate randomMove = moves.get(rand.nextInt(moves.size()));
        board.makeMoveAt(board.getCurrent(), randomMove);
    }

    private Coordinate getMaximumScoreMove(Map<Coordinate, Integer> moveScores) {
        Map.Entry<Coordinate, Integer> maxEntry = null;
        for (Map.Entry<Coordinate, Integer> entry : moveScores.entrySet()) {
            if (null == maxEntry || 0 < entry.getValue().compareTo(maxEntry.getValue())) {
                maxEntry = entry;
            }
        }
        return Objects.requireNonNull(maxEntry).getKey();
    }
}
