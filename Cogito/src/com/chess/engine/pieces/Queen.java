/**
 * 
 */
package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Tile;


public class Queen extends Piece {
	private static final int[] POSSIBLE_MOVE_OFFSETS = { -9, -8, -7, -1, 1, 7, 8, 9 };

	public Queen(final int piecePosition, final Color pieceColor) {
		super(PieceType.QUEEN, piecePosition, pieceColor, true);
	}
	
	public Queen(final int piecePosition, final Color pieceColor, final boolean isFirstMove) {
		super(PieceType.QUEEN, piecePosition, pieceColor, isFirstMove);
	}
	
	@Override
	public boolean equals(final Object other) {
		return this == other || other instanceof Queen && (super.equals(other));
	}
	
	@Override
	public String toString() {
		return this.pieceType.toString();
	}
	
	@Override
	public Queen movePiece(Move move) {
		return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
	}
	
	@Override
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	@Override
	public int locationBonus() {
		return this.pieceColor.queenBonus(this.piecePosition);
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
							legalMoves.add(new MajorAttackMove(board, this, possibleDestination, 
									pieceAtDestination));
						}
						break;
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
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
