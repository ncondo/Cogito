/**
 * 
 */
package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;


public abstract class Piece {
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Color pieceColor;
	protected final boolean isFirstMove;
	private final int cachedHashCode;
	
	Piece(final PieceType pieceType, final int piecePosition,
			final Color pieceColor, final boolean isFirstMove) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceColor = pieceColor;
		this.isFirstMove = isFirstMove;
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
	
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
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
		return piecePosition == otherPiece.piecePosition &&
				pieceType == otherPiece.pieceType &&
				pieceColor == otherPiece.pieceColor &&
				isFirstMove == otherPiece.isFirstMove;
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
	public abstract int locationBonus();
	
	public enum PieceType {
		
		PAWN("P", 100) {
			@Override
			public boolean isPawn() {
				return true;
			}
			
			@Override
			public boolean isKing() {
				return false;
			}
			
			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N", 320) {
			@Override
			public boolean isPawn() {
				return false;
			}
			
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B", 330) {
			@Override
			public boolean isPawn() {
				return false;
			}
			
			@Override
			public boolean isKing() {
				return false;
			}
			
			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R", 500) {
			@Override
			public boolean isPawn() {
				return false;
			}
			
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q", 900) {
			@Override
			public boolean isPawn() {
				return false;
			}
			
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K", 10000) {
			@Override
			public boolean isPawn() {
				return false;
			}
			
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		
		PieceType(final String pieceName, final int pieceValue) {
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public int getPieceValue() {
			return this.pieceValue;
		}
		
		public abstract boolean isKing();
		public abstract boolean isRook();
		public abstract boolean isPawn();
	}

}
