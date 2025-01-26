package org.sundeepchand.chess;

import java.util.*;

enum Color {
    BLACK,
    WHITE
}

class Position {
    private final int r, c;

    public Position(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getRow() {
        return r;
    }

    public int getCol() {
        return c;
    }
}

abstract class Piece {
    private final Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    abstract public boolean validateMove(Position start, Position end);
}

class RookPiece extends Piece {
    public RookPiece(Color color) {
        super(color);
    }

    public boolean validateMove(Position start, Position end) {
        // validate the move
        return true;
    }
}

class KnightPiece extends Piece {
    public KnightPiece(Color color) {
        super(color);
    }

    public boolean validateMove(Position start, Position end) {
        // validate the move
        return true;
    }
}

class BishopPiece extends Piece {
    public BishopPiece(Color color) {
        super(color);
    }

    public boolean validateMove(Position start, Position end) {
        // validate the move
        return true;
    }
}

class QueenPiece extends Piece {
    public QueenPiece(Color color) {
        super(color);
    }

    public boolean validateMove(Position start, Position end) {
        // validate the move
        return true;
    }
}

class KingPiece extends Piece {
    public KingPiece(Color color) {
        super(color);
    }

    public boolean validateMove(Position start, Position end) {
        // validate the move
        return true;
    }
}

class PawnPiece extends Piece {
    public PawnPiece(Color color) {
        super(color);
    }

    public boolean validateMove(Position start, Position end) {
        // validate the move
        return true;
    }
}

class Cell {
    private final Position position;
    private Piece piece;
    private final Color color;

    public Cell(Position position, Piece piece, Color color) {
        this.position = position;
        this.piece = piece;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece removePiece() {
        Piece curValue = piece;
        piece = null;
        return curValue;
    }
}

class Player {
    private final String name;
    private final List<Piece> capturedPieces;

    public Player(String name) {
        this.name = name;
        capturedPieces = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void addCapturedPiece(Piece piece) {
        capturedPieces.add(piece);
    }

    public List<Piece> getAllCapturedPieces() {
        return capturedPieces;
    }
}

class Board {
    private final int N_ROWS = 8, N_COLS = 8;
    private final Cell[][] cells = new Cell[N_ROWS][N_COLS];

    public Board() {
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                cells[i][j] = new Cell(new Position(i, j), null, ((i + j) % 2 == 0) ? Color.WHITE : Color.BLACK);
            }
        }

        // Put black pieces at the top
        cells[0][0].setPiece(new RookPiece(Color.BLACK));
        cells[0][N_COLS - 1].setPiece(new RookPiece(Color.BLACK));
        cells[0][1].setPiece(new KnightPiece(Color.BLACK));
        cells[0][N_COLS - 2].setPiece(new KnightPiece(Color.BLACK));
        cells[0][2].setPiece(new BishopPiece(Color.BLACK));
        cells[0][N_COLS - 3].setPiece(new BishopPiece(Color.BLACK));
        cells[0][3].setPiece(new QueenPiece(Color.BLACK));
        cells[0][4].setPiece(new KingPiece(Color.BLACK));
        for (int i = 0; i < N_COLS; i++) {
            cells[1][i].setPiece(new PawnPiece(Color.BLACK));
        }

        // Put white pieces at the bottom
        cells[N_ROWS - 1][0].setPiece(new RookPiece(Color.WHITE));
        cells[N_ROWS - 1][N_COLS - 1].setPiece(new RookPiece(Color.WHITE));
        cells[N_ROWS - 1][1].setPiece(new KnightPiece(Color.WHITE));
        cells[N_ROWS - 1][N_COLS - 2].setPiece(new KnightPiece(Color.WHITE));
        cells[N_ROWS - 1][2].setPiece(new BishopPiece(Color.WHITE));
        cells[N_ROWS - 1][N_COLS - 3].setPiece(new BishopPiece(Color.WHITE));
        cells[N_ROWS - 1][3].setPiece(new QueenPiece(Color.WHITE));
        cells[N_ROWS - 1][4].setPiece(new KingPiece(Color.WHITE));
        for (int i = 0; i < N_COLS; i++) {
            cells[N_ROWS - 2][i].setPiece(new PawnPiece(Color.WHITE));
        }
    }

    private Piece getPieceAt(Position position) {
        return cells[position.getRow()][position.getCol()].getPiece();
    }

    private void movePiece(Piece piece, Position from, Position to) {
        cells[from.getRow()][from.getCol()].removePiece();
        cells[to.getRow()][to.getCol()].setPiece(piece);
    }

    public void makeMove(Player player, Position from, Position to) throws IllegalArgumentException {
        if (from.getRow() < 0 || from.getRow() >= N_ROWS || from.getCol() < 0 || from.getCol() >= N_COLS) {
            throw new IllegalArgumentException("Invalid from position provided (" + from.getRow() + ", " + from.getCol() + ")");
        }

        if (to.getRow() < 0 || to.getRow() >= N_ROWS || to.getCol() < 0 || to.getCol() >= N_COLS) {
            throw new IllegalArgumentException("Invalid to position provided (" + to.getRow() + ", " + to.getCol() + ")");
        }

        if (cells[from.getRow()][from.getCol()].getPiece() == null) {
            throw new IllegalArgumentException("No piece exists in the from position (" + from.getRow() + ", " + from.getCol() + ")");
        }

        Piece fromPiece = this.getPieceAt(from);
        if (!fromPiece.validateMove(from, to)) {
            throw new IllegalArgumentException("Not a valid move");
        }

        Piece toPiece = this.getPieceAt(to);
        if (toPiece == null) {
            movePiece(fromPiece, from, to);
            return;
        }

        if (toPiece.getColor() == fromPiece.getColor()) {
            throw new IllegalArgumentException("Invalid move as cell is already occupied");
        }

        player.addCapturedPiece(toPiece);
        movePiece(fromPiece, from, to);
    }

    public boolean isGameOver() {
        // add logic for game over
        return false;
    }

    public Color getWinningColor() {
        return Color.BLACK;
    }
}

class Game {
    private final Board board;
    private final Queue<Player> players;

    private final Scanner scanner = new Scanner(System.in);

    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.players = new LinkedList<>();
        players.add(player1);
        players.add(player2);
    }

    public void run() {
        while (true) {
            Player curPlayer = players.poll();
            if (curPlayer == null) {
                break;
            }
            System.out.println(curPlayer.getName() + " player's turn." +
                    "Enter the positions (from_row, from_col, to_row, to_col): ");
            int fromRow = scanner.nextInt();
            int fromCol = scanner.nextInt();
            int toRow = scanner.nextInt();
            int toCol = scanner.nextInt();

            try {
                board.makeMove(curPlayer, new Position(fromRow, fromCol), new Position(toRow, toCol));
            } catch (IllegalArgumentException e) {
                System.out.println("Cannot make move: " + e.getMessage());
            }

            if (board.isGameOver()) {
                System.out.println(board.getWinningColor().toString() + " player wins!!");
                break;
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("Mufasa"), player2 = new Player("Simba");
        Game game = new Game(player1, player2);
        game.run();
    }

}
