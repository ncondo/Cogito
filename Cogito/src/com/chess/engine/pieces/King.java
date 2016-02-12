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
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;

/**
 * @author ncondo
 *
 */
public class King extends Piece {
	
	private final static int[] POSSIBLE_MOVE_OFFSETS = { -9, -8, -7, -1, 1, 7, 8, 9 };

	/**
	 * @param piecePosition
	 * @param pieceColor
	 */
	public King(final int piecePosition, final Color pieceColor) {
		super(PieceType.KING, piecePosition, pieceColor);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		int possibleDestination;
		final List<Move> legalMoves = new ArrayList<>();
		
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
						legalMoves.add(new AttackMove(board, this, possibleDestination,
								pieceAtDestination));
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
	
	@Override
	public King movePiece(Move move) {
		return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}

}
