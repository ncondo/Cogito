/**
 * 
 */
package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Color;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;


public class Board {
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final Pawn enPassantPawn;
	
	private Board(final BoardBuilder builder) {
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	public Player blackPlayer() {
		return this.blackPlayer;
	}

	public Player currentPlayer() {
		return this.currentPlayer;
	}

	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}
	
	public Tile getTile(final int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}
	
	public Collection<Piece> getBlackPieces() {
		return this.blackPieces;
	}
	
	public Collection<Piece> getWhitePieces() {
		return this.whitePieces;
	}
	
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return Collections.unmodifiableList(legalMoves);
	}

	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
														   final Color color) {
		final List<Piece> activePieces = new ArrayList<>();
		for (final Tile tile : gameBoard) {
			if (tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if (piece.getPieceColor() == color) {
					activePieces.add(piece);
				}
			}
		}
		return Collections.unmodifiableList(activePieces);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			
			if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	private static List<Tile> createGameBoard(final BoardBuilder builder) {
		final List<Tile> tiles = new ArrayList<Tile>(BoardUtils.NUM_TILES);
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles.add(i, Tile.createTile(i, builder.boardConfig.get(i)));
		}
		return Collections.unmodifiableList(tiles);
	}
	
	public static Board createStandardBoard() {
		final BoardBuilder builder = new BoardBuilder();
		// Black Layout
		builder.setPiece(new Rook(0, Color.BLACK));
		builder.setPiece(new Knight(1, Color.BLACK));
		builder.setPiece(new Bishop(2, Color.BLACK));
		builder.setPiece(new Queen(3, Color.BLACK));
		builder.setPiece(new King(4, Color.BLACK));
		builder.setPiece(new Bishop(5, Color.BLACK));
		builder.setPiece(new Knight(6, Color.BLACK));
		builder.setPiece(new Rook(7, Color.BLACK));
		builder.setPiece(new Pawn(8, Color.BLACK));
		builder.setPiece(new Pawn(9, Color.BLACK));
		builder.setPiece(new Pawn(10, Color.BLACK));
		builder.setPiece(new Pawn(11, Color.BLACK));
		builder.setPiece(new Pawn(12, Color.BLACK));
		builder.setPiece(new Pawn(13, Color.BLACK));
		builder.setPiece(new Pawn(14, Color.BLACK));
		builder.setPiece(new Pawn(15, Color.BLACK));
		// White Layout
		builder.setPiece(new Pawn(48, Color.WHITE));
		builder.setPiece(new Pawn(49, Color.WHITE));
		builder.setPiece(new Pawn(50, Color.WHITE));
		builder.setPiece(new Pawn(51, Color.WHITE));
		builder.setPiece(new Pawn(52, Color.WHITE));
		builder.setPiece(new Pawn(53, Color.WHITE));
		builder.setPiece(new Pawn(54, Color.WHITE));
		builder.setPiece(new Pawn(55, Color.WHITE));
		builder.setPiece(new Rook(56, Color.WHITE));
		builder.setPiece(new Knight(57, Color.WHITE));
		builder.setPiece(new Bishop(58, Color.WHITE));
		builder.setPiece(new Queen(59, Color.WHITE));
		builder.setPiece(new King(60, Color.WHITE));
		builder.setPiece(new Bishop(61, Color.WHITE));
		builder.setPiece(new Knight(62, Color.WHITE));
		builder.setPiece(new Rook(63, Color.WHITE));
		
		// White to move first
		builder.setMoveMaker(Color.WHITE);
		return builder.build();
	}
	
	public static class BoardBuilder {
		Map<Integer, Piece> boardConfig;
		Color nextMoveMaker;
		Pawn enPassantPawn;
		
		public BoardBuilder() {
			this.boardConfig = new HashMap<>();
		}
		
		public BoardBuilder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public BoardBuilder setMoveMaker(final Color nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
			
		}
	}

}
