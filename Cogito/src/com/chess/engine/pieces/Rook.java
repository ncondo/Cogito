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
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;


public class Rook extends Piece {
	private static final int[] POSSIBLE_MOVE_OFFSETS = { -8, -1, 1, 8 };

	public Rook(final int piecePosition, final Color pieceColor) {
		super(PieceType.ROOK, piecePosition, pieceColor, true);
	}
	
	public Rook(final int piecePosition, final Color pieceColor, final boolean isFirstMove) {
		super(PieceType.ROOK, piecePosition, pieceColor, isFirstMove);
	}
	
	@Override
	public boolean equals(final Object other) {
		return this == other || other instanceof Rook && (super.equals(other));
	}
	
	@Override
	public String toString() {
		return this.pieceType.toString();
	}
	
	@Override
	public Rook movePiece(Move move) {
		return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
	}
	
	@Override
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	@Override
	public int locationBonus() {
		return this.pieceColor.rookBonus(this.piecePosition);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		int possibleDestination;
		for (final int currentOffset : POSSIBLE_MOVE_OFFSETS) {
			possibleDestination = this.piecePosition;
			while (BoardUtils.isValidTileCoordinate(possibleDestination)) {
				if (isFirstColumnExclusion(possibleDestination, currentOffset) ||
					isEighthColumnExclusion(possibleDestination, currentOffset)) {
					break;
				}
				possibleDestination += currentOffset;
				if (BoardUtils.isValidTileCoordinate(possibleDestination)) {
					final Tile possibleDestinationTile = board.getTile(possibleDestination);
					if (!possibleDestinationTile.isTileOccupied()) {
						legalMoves.add(new MajorMove(board, this, possibleDestination));
					} else {
						final Piece pieceAtDestination = possibleDestinationTile.getPiece();
						final Color pieceColor = pieceAtDestination.getPieceColor();
						if (this.pieceColor != pieceColor) {
							legalMoves.add(new MajorAttackMove(board, this,
									possibleDestination, pieceAtDestination));
						}
						break;
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
	}

}
