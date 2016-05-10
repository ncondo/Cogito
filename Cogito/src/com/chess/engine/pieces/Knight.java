/**
 * 
 */
package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;


public class Knight extends Piece {
	private final static int[] POSSIBLE_MOVE_OFFSETS = { -17, -15, -10, -6, 6, 10, 15, 17 };

	public Knight(final int piecePosition, final Color pieceColor) {
		super(PieceType.KNIGHT, piecePosition, pieceColor, true);
	}
	
	public Knight(final int piecePosition, final Color pieceColor, final boolean isFirstMove) {
		super(PieceType.KNIGHT, piecePosition, pieceColor, isFirstMove);
	}
	
	@Override
	public boolean equals(final Object other) {
		return this == other || other instanceof Knight && (super.equals(other));
	}
	
	@Override
	public String toString() {
		return this.pieceType.toString();
	}
	
	@Override
	public Knight movePiece(Move move) {
		return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
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
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		int possibleDestination;
		for (final int currentOffset : POSSIBLE_MOVE_OFFSETS) {
			possibleDestination = this.piecePosition + currentOffset;
			if (BoardUtils.isValidTileCoordinate(possibleDestination)) {
				if (isFirstColumnExclusion(this.piecePosition, currentOffset) ||
					isSecondColumnExclusion(this.piecePosition, currentOffset) ||
					isSeventhColumnExclusion(this.piecePosition, currentOffset) ||
					isEighthColumnExclusion(this.piecePosition, currentOffset)) {
					continue;
				}
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
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 ||
				candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15);
	}
	
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 ||
				candidateOffset == 6);
	}
	
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 ||
				candidateOffset == 10);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 ||
				candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17);
	}

}
