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
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;


public class Pawn extends Piece {
	private final static int[] POSSIBLE_MOVE_OFFSETS = { 7, 8, 9, 16 };

	public Pawn(final int piecePosition, final Color pieceColor) {
		super(PieceType.PAWN, piecePosition, pieceColor, true);
	}
	
	public Pawn(final int piecePosition, final Color pieceColor, final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceColor, isFirstMove);
	}
	
	public Piece getPromotionPiece() {
		return new Queen(this.piecePosition, this.pieceColor, false);
	}
	
	@Override
	public boolean equals(final Object other) {
		return this == other || other instanceof Pawn && (super.equals(other));
	}
	
	@Override
	public String toString() {
		return this.pieceType.toString();
	}
	
	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
	}
	
	@Override
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	@Override
	public int locationBonus() {
		return this.pieceColor.pawnBonus(this.piecePosition);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		int possibleDestination;
		
		for (final int currentOffset : POSSIBLE_MOVE_OFFSETS) {
			possibleDestination = this.piecePosition + 
					(this.pieceColor.getDirection() * currentOffset);
			if (!BoardUtils.isValidTileCoordinate(possibleDestination)) {
				continue;
			}
			if (currentOffset == 8 && !board.getTile(possibleDestination).isTileOccupied()) {
				if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, possibleDestination)));
				} else {
					legalMoves.add(new PawnMove(board, this, possibleDestination));
				}
			} else if (currentOffset == 16 && this.isFirstMove() && 
					((BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceColor.isWhite()) ||
					(BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceColor.isBlack()))) {
				final int behindPossibleDestination = this.piecePosition + 
						(this.pieceColor.getDirection() * 8);
				if (!board.getTile(behindPossibleDestination).isTileOccupied() &&
						!board.getTile(possibleDestination).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, possibleDestination));
				}
			} else if (currentOffset == 7 && 
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack()) ||
					 (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceColor.isWhite()))) {
				if (board.getTile(possibleDestination).isTileOccupied()) {
					final Piece pieceAtDestination = board.getTile(possibleDestination).getPiece();
					if (this.pieceColor != pieceAtDestination.getPieceColor()) {
						if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board,this,
									possibleDestination, pieceAtDestination)));
						} else {
							legalMoves.add(new PawnAttackMove(board, this, possibleDestination,
									pieceAtDestination));
						}
					}
				} else if (board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition +
							(this.pieceColor.getOppositeDirection()))) {
						final Piece pieceAtDestination = board.getEnPassantPawn();
						if (this.pieceColor != pieceAtDestination.getPieceColor()) {
							if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
								legalMoves.add(new PawnPromotion(new PawnEnPassantAttackMove(board,
										this, possibleDestination, pieceAtDestination)));
							} else {
								legalMoves.add(new PawnEnPassantAttackMove(board, this, 
										possibleDestination, pieceAtDestination));
							}
						}
					}
				}
			} else if (currentOffset == 9 &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isWhite()) ||
					 (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceColor.isBlack()))) {
				if (board.getTile(possibleDestination).isTileOccupied()) {
					final Piece pieceAtDestination = board.getTile(possibleDestination).getPiece();
					if (this.pieceColor != pieceAtDestination.getPieceColor()) {
						if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
									possibleDestination, pieceAtDestination)));
						} else {
							legalMoves.add(new PawnAttackMove(board, this, possibleDestination,
									pieceAtDestination));
						}
					}
				} else if (board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition -
							(this.pieceColor.getOppositeDirection()))) {
						final Piece pieceAtDestination = board.getEnPassantPawn();
						if (this.pieceColor != pieceAtDestination.getPieceColor()) {
							if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
								legalMoves.add(new PawnPromotion(new PawnEnPassantAttackMove(board,
										this, possibleDestination, pieceAtDestination)));
							} else {
								legalMoves.add(new PawnEnPassantAttackMove(board, this,
										possibleDestination, pieceAtDestination));
							}
						}
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}

}
