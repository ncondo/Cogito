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
import com.chess.engine.board.Tile;

/**
 * @author ncondo
 *
 */
public class Knight extends Piece {
	
	private final static int[] POSSIBLE_MOVE_OFFSETS = { -17, -15, -10, -6, 6, 10, 15, 17 };

	Knight(final int piecePosition, final Color pieceColor) {
		super(piecePosition, pieceColor);
	}
	
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		int possibleDestination;
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentOffset : POSSIBLE_MOVE_OFFSETS) {
			possibleDestination = this.piecePosition + currentOffset;
			
			if (BoardUtils.isValidTileCoordinate(possibleDestination)) {
				final Tile possibleDestinationTile = board.getTile(possibleDestination);
				
				if (isFirstColumnExclusion(this.piecePosition, currentOffset) ||
						isSecondColumnExclusion(this.piecePosition, currentOffset) ||
						isSeventhColumnExclusion(this.piecePosition, currentOffset) ||
						isEighthColumnExclusion(this.piecePosition, currentOffset)) {
					continue;
				}
				
				if (!possibleDestinationTile.isTileOccupied()) {
					legalMoves.add(new Move.MajorMove(board, this, possibleDestination));
				} else {
					final Piece pieceAtDestination = possibleDestinationTile.getPiece();
					final Color pieceColor = pieceAtDestination.getPieceColor();
					
					if (this.pieceColor != pieceColor) {
						legalMoves.add(new Move.AttackMove(board, this, possibleDestination,
								pieceAtDestination));
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
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