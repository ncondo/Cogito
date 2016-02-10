/**
 * 
 */
package com.chess.engine.pieces;

import java.util.Collection;

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
	
	public Color getPieceColor() {
		return this.pieceColor;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);

}
