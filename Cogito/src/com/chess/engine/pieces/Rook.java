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
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;

/**
 * @author ncondo
 *
 */
public class Rook extends Piece {
	
	private static final int[] POSSIBLE_MOVE_OFFSETS = { -8, -1, 1, 8 };

	/**
	 * @param piecePosition
	 * @param pieceColor
	 */
	public Rook(final int piecePosition, final Color pieceColor) {
		super(piecePosition, pieceColor);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		int possibleDestination;
		final List<Move> legalMoves = new ArrayList<>();
		
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
							legalMoves.add(new AttackMove(board, this, possibleDestination, 
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
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
	}
	
	@Override
	public String toString() {
		return PieceType.ROOK.toString();
	}

}
