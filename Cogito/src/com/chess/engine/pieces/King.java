/**
 * 
 */
package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.google.common.collect.ImmutableList;


public class King extends Piece {
	private final static int[] POSSIBLE_MOVE_OFFSETS = { -9, -8, -7, -1, 1, 7, 8, 9 };
	private final boolean isCastled;
	private final boolean kingSideCastleCapable;
	private final boolean queenSideCastleCapable;

	public King(final int piecePosition, final Color pieceColor, 
				final boolean kingSideCastleCapable,
				final boolean queenSideCastleCapable) {
		super(PieceType.KING, piecePosition, pieceColor, true);
		this.isCastled = false;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}
	
	public King(final int piecePosition, final Color pieceColor,
				final boolean isFirstMove, final boolean isCastled,
				final boolean kingSideCastleCapable,
				final boolean queenSideCastleCapable) {
		super(PieceType.KING, piecePosition, pieceColor, isFirstMove);
		this.isCastled = isCastled;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}
	
	public boolean isCastled() {
		return this.isCastled;
	}
	
	public boolean isKingSideCastleCapable() {
		return this.kingSideCastleCapable;
	}
	
	public boolean isQueenSideCastleCapable() {
		return this.queenSideCastleCapable;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof King)) {
			return false;
		}
		if (!super.equals(other)) {
			return false;
		}
		final King king = (King) other;
		return isCastled == king.isCastled;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (isCastled ? 1: 0);
		return result;
	}
	
	@Override
	public String toString() {
		return this.pieceType.toString();
	}
	
	@Override
	public King movePiece(Move move) {
		return new King(move.getDestinationCoordinate(), this.pieceColor,
						false, move.isCastlingMove(), false, false);
	}
	
	@Override
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	@Override
	public int locationBonus() {
		return this.pieceColor.kingBonus(this.piecePosition);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		int possibleDestination;
		for (final int currentOffset : POSSIBLE_MOVE_OFFSETS) {
			possibleDestination = this.piecePosition + currentOffset;
			if (isFirstColumnExclusion(this.piecePosition, currentOffset) ||
				isEighthColumnExclusion(this.piecePosition, currentOffset)) {
				continue;
			}
			if (BoardUtils.isValidTileCoordinate(possibleDestination)) {
				final Tile possibleDestinationTile = board.getTile(possibleDestination);
				if (!possibleDestinationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, possibleDestination));
				} else {
					final Piece pieceAtDestination = possibleDestinationTile.getPiece();
					final Color pieceColor = pieceAtDestination.getPieceColor();
					if (this.pieceColor != pieceColor) {
						legalMoves.add(new MajorAttackMove(board, this, possibleDestination,
								pieceAtDestination));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 ||
				candidateOffset == -1 || candidateOffset == 7);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 ||
				candidateOffset == 1 || candidateOffset == 9);
	}

}
