/**
 * 
 */
package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * @author ncondo
 *
 */
public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Color pieceColor;
	protected final boolean isFirstMove;
	private final int cachedHashCode;
	
	Piece(final PieceType pieceType, final int piecePosition,
			final Color pieceColor) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceColor = pieceColor;
		this.isFirstMove = false;
		this.cachedHashCode = computeHashCode();
	}
	
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public int getPiecePosition() {
		return this.piecePosition;
	}
	
	public Color getPieceColor() {
		return this.pieceColor;
	}
	
	public boolean isFirstMove() {
		return this.isFirstMove;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		
		if (!(other instanceof Piece)) {
			return false;
		}
		
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() &&
				pieceType == otherPiece.getPieceType() &&
				pieceColor == otherPiece.getPieceColor() &&
				isFirstMove == otherPiece.isFirstMove();
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceColor.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	public abstract Piece movePiece(Move move);
	
	public enum PieceType {
		
		PAWN("P") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				return true;
			}
		};
		
		private String pieceName;
		
		PieceType(final String pieceName) {
			this.pieceName = pieceName;
		}
		
		public abstract boolean isKing();
		
		@Override
		public String toString() {
			return this.pieceName;
		}
	}

}
