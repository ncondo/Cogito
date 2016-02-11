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
	protected final boolean isFirstMove;
	
	Piece(final int piecePosition, final Color pieceColor) {
		this.piecePosition = piecePosition;
		this.pieceColor = pieceColor;
		this.isFirstMove = false;
	}
	
	public Color getPieceColor() {
		return this.pieceColor;
	}
	
	public boolean isFirstMove() {
		return this.isFirstMove;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);

}
