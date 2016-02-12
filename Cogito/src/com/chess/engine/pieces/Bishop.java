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
public class Bishop extends Piece {
	
	private final static int[] POSSIBLE_MOVE_OFFSETS = { -9, -7, 7, 9 };

	public Bishop(final int piecePosition, final Color pieceColor) {
		super(PieceType.BISHOP, piecePosition, pieceColor);
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
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || 
				candidateOffset == 7);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || 
				candidateOffset == 9);
	}
	
	@Override
	public Bishop movePiece(Move move) {
		return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
	}
	
	@Override
	public String toString() {
		return PieceType.BISHOP.toString();
	}

}
