/**
 * 
 */
package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.ai.MoveStrategy;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


public abstract class Player {
	
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	private MoveStrategy strategy;
	
	Player(final Board board, final Collection<Move> legalMoves,
			final Collection<Move> opponentLegalMoves) {
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = ImmutableList.copyOf(
				Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentLegalMoves)));
		this.isInCheck = !Player.calculateAttacksOnTile(
				this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
	}
	
	public King getPlayerKing() {
		return this.playerKing;
	}
	
	public boolean isInCheck() {
		return this.isInCheck;
	}
	
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	private boolean hasEscapeMoves() {
		for (final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCastled() {
		return this.playerKing.isCastled();
	}
	
	public boolean isKingSideCastleCapable() {
		return this.playerKing.isKingSideCastleCapable();
	}
	
	public boolean isQueenSideCastleCapable() {
		return this.playerKing.isQueenSideCastleCapable();
	}

	protected static Collection<Move> calculateAttacksOnTile(int piecePosition,
			Collection<Move> opponentMoves) {
		
		final List<Move> attackMoves = new ArrayList<>();
		for (final Move move : opponentMoves) {
			if (piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return Collections.unmodifiableList(attackMoves);
	}

	private King establishKing() {
		for (final Piece piece : getActivePieces()) {
			if (piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("Not a valid board!");
	}
	
	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}
	
	public boolean isMoveLegal(final Move move) {
		return !(move.isCastlingMove() && isInCheck()) && this.legalMoves.contains(move);
	}
	
	public MoveStrategy getMoveStrategy() {
		return this.strategy;
	}
	
	public void setMoveStrategy(final MoveStrategy strategy) {
		this.strategy = strategy;
	}
	
	public MoveTransition makeMove(final Move move) {
		if (!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board transitionBoard = move.execute();
		
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
				transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
				transitionBoard.currentPlayer().getLegalMoves());
		
		if (!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	public String playerInfor() {
		return ("Player is: " + this.getColor() + "\nlegal moves =" + getLegalMoves() +
				"\ninCheck = " + isInCheck() + "\nisInCheckMate = " + isInCheckMate() +
				"\nisCastled = " + isCastled()) + "\n";
	}

	public abstract Collection<Piece> getActivePieces();
	public abstract Color getColor();
	public abstract Player getOpponent();
	protected abstract Collection<Move> calculateKingCastles(
			Collection<Move> playerLegals, Collection<Move> opponentLegals);

}
