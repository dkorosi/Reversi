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

    private static final double EXPLOITATION = 1.4142;
    private static final int HASH_LENGTH = 8;

    private int difficultyTime;
    private Random rand = new Random();
    private Map<ByteSequence, GameState> gameStates = new HashMap<>();

    // Teljesítmény érdekében
    private final ByteSequence[][] pieceByPositionHashes;

    public AiPlayer(String name, TileType color, int difficulty, int width, int height) {
        super(name, color, Long.MAX_VALUE, false);
        difficultyTime = 1000 * (difficulty + 2) / 2;

        pieceByPositionHashes = new ByteSequence[width * height][2];
        for (int i = 0; i < width * height; i++) {
            for (int j = 0; j < 2; j++) {
                byte[] bytes = new byte[HASH_LENGTH];
                rand.nextBytes(bytes);
                pieceByPositionHashes[i][j] = new ByteSequence(bytes);
            }
        }
    }

    @Override
    public void makeMove(Board board) {
        List<Coordinate> moves = board.getValidCoordinates();
        if (moves.isEmpty() || board.getCurrent() != getColor())
            return;

        int timePerMove = difficultyTime / moves.size();

        Map<Coordinate, GameState> moveScores = new HashMap<>();
        ByteSequence boardHash = hashBoard(board.getBoard());
        GameState initialGameState = gameStates.get(boardHash);
        int parentSimulations = 0;
        if (initialGameState != null)
            parentSimulations = initialGameState.getSimulations();

        if (moves.size() == 1) {
            board.makeMoveAt(getColor(), moves.get(1));
            return;
        }

        for (Coordinate move : moves) {
            double score;
            Board boardAfterMove = new Board(board.getBoard(), getColor());
            List<Coordinate> changedCoordinates = boardAfterMove.makeMoveAt(getColor(), move);
            // Ha ez egy lépés van hátra, ha győzünk lépjük azt, ha nem folytassuk
            if (!boardAfterMove.isActive()) {
                if (board.getWinning() == getColor() || move.equals(moves.get(moves.size() - 1))) {
                    board.makeMoveAt(getColor(), move);
                    return;
                } else
                    continue;
            }

            ByteSequence boardAfterMoveHash = changeBoardHash(boardHash, getColor(), changedCoordinates, board.getWidth());
            GameState gameState = gameStates.get(boardAfterMoveHash);
            if (gameState == null) {
                gameState = new GameState();
            }

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + timePerMove) {
                Board simBoard = new Board(boardAfterMove.getBoard(), boardAfterMove.getCurrent());

                score = simulateRecursive(simBoard, boardAfterMoveHash);
                gameState.increaseScore(score);
            }
            moveScores.put(move, gameState);
        }
        Coordinate move = getMaximumScoreMove(moveScores, parentSimulations);
        board.makeMoveAt(getColor(), move);
    }


    private double simulateRecursive(Board board, ByteSequence boardHash) {
        TileType nextColor = board.getCurrent();
        List<Coordinate> changedCoordinates = makeRandomMove(board);
        if (board.isActive()) {
            ByteSequence hash = changeBoardHash(boardHash, nextColor, changedCoordinates, board.getWidth());
            double score = simulateRecursive(board, hash);
            if (nextColor == getColor()) {
                GameState gameState = gameStates.get(hash);
                if (gameState == null)
                    gameStates.put(hash, new GameState(score));
                else {
                    gameState.increaseScore(score);
                }
            }
            return score;
        } else {
            TileType winner = board.getWinning();
            if (winner == getColor()) {
                return 1;
            } else if (winner == TileType.EMPTY)
                return 0.5;
        }
        return 0;
    }

    private ByteSequence hashBoard(List<List<TileType>> board) {
        int width = board.get(0).size();
        byte[] boardHash = new byte[HASH_LENGTH];
        for (int y = 0; y < board.size(); y++) {
            for (int x = 0; x < width; x++) {
                TileType tile = board.get(y).get(x);
                if (tile == TileType.EMPTY)
                    continue;
                for (int k = 0; k < HASH_LENGTH; k++) {
                    boardHash[k] ^= pieceByPositionHashes[y * width + x][tile.getIndex() - 1].getBytes()[k];
                }
            }
        }
        return new ByteSequence(boardHash);
    }

    private ByteSequence changeBoardHash(ByteSequence initialHash, TileType color, List<Coordinate> changedCoordinates, int boardWidth) {
        byte[] newHash = new byte[HASH_LENGTH];
        for (int i = 0; i < HASH_LENGTH; i++) {
            newHash[i] = initialHash.getBytes()[i];
            for (int j = 0; j < changedCoordinates.size(); j++) {
                Coordinate coordinate = changedCoordinates.get(j);
                int tileNumber = coordinate.getY() * boardWidth + coordinate.getX();
                if (j != 0) // Ha üres volt, nem vesszük ki az ellenfelet, az az első a listában
                    newHash[i] ^= pieceByPositionHashes[tileNumber][color.enemyTileType().getIndex() - 1].getBytes()[i];
                newHash[i] ^= pieceByPositionHashes[tileNumber][color.getIndex() - 1].getBytes()[i];
            }

        }
        return new ByteSequence(newHash);
    }

    private List<Coordinate> makeRandomMove(Board board) {
        List<Coordinate> moves = board.getValidCoordinates();

        Coordinate randomMove = moves.get(rand.nextInt(moves.size()));
        return board.makeMoveAt(board.getCurrent(), randomMove);
    }

    private Coordinate getMaximumScoreMove(Map<Coordinate, GameState> moveScores, int parentSimulations) {
        Map.Entry<Coordinate, GameState> maxEntry = null;
        for (Map.Entry<Coordinate, GameState> entry : moveScores.entrySet()) {
            if (null == maxEntry || entry.getValue().getUct(parentSimulations, EXPLOITATION) >
                    maxEntry.getValue().getUct(parentSimulations, EXPLOITATION)) {
                maxEntry = entry;
            }
        }
        return Objects.requireNonNull(maxEntry).getKey();
    }
}
