/**
 * 
 */
package com.chess.engine.pieces;

import java.util.List;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * @author ncondo
 *
 */
public abstract class Piece {
	
	protected final int piecePosition;
	protected final Color pieceColor;
	
	Piece(final int piecePosition, final Color pieceColor) {
		this.piecePosition = piecePosition;
		this.pieceColor = pieceColor;
	}
	
	public abstract List<Move> calculateLegalMoves(final Board board);

}
