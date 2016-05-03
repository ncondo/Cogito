package com.chess.engine.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Board.BoardBuilder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece.PieceType;

public class PawnTest {
	
	@Test
	public void whitePawnJumpMovesTwoSpaces() {
		final BoardBuilder builder = new BoardBuilder();
		// setup a legal board with White's turn to move
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a8"), Color.BLACK, false, false));
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a1"), Color.WHITE, false, false));
		builder.setPiece(new Pawn(BoardUtils.getCoordinateAtPosition("e2"), Color.WHITE, true));
		builder.setMoveMaker(Color.WHITE);
		final Board board = builder.build();
		// move pawn with pawn jump
		final Move pawnJumpMove = new PawnJump(board, 
				board.getTile(BoardUtils.getCoordinateAtPosition("e2")).getPiece(),
				BoardUtils.getCoordinateAtPosition("e4"));
		final Board newBoard = pawnJumpMove.execute();
		final Piece pieceAtDestination = newBoard.getTile(BoardUtils.getCoordinateAtPosition("e4")).getPiece();
		assertEquals("Pawn did not jump two squares", PieceType.PAWN, pieceAtDestination.getPieceType());
	}

	@Test
	public void whitePawnPromotionChangesPieceToQueen() {
		final BoardBuilder builder = new BoardBuilder();
		// setup a legal board with White's turn to move
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a8"), Color.BLACK, false, false));
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a1"), Color.WHITE, false, false));
		builder.setPiece(new Pawn(BoardUtils.getCoordinateAtPosition("e7"), Color.WHITE));
		builder.setMoveMaker(Color.WHITE);
		final Board board = builder.build();
		// move white pawn to promotion square "e8"
		final Move promotionMove = Move.MoveFactory.createMove(board, 
				BoardUtils.getCoordinateAtPosition("e7"), BoardUtils.getCoordinateAtPosition("e8"));
		final Board newBoard = promotionMove.execute();
		final Piece pieceAtDestination = newBoard.getTile(BoardUtils.getCoordinateAtPosition("e8")).getPiece();
		assertEquals("Pawn did not promote to Queen", PieceType.QUEEN, pieceAtDestination.getPieceType());
	}
	
	@Test
	public void blackPawnJumpMovesTwoSpaces() {
		final BoardBuilder builder = new BoardBuilder();
		// setup a legal board with Black's turn to move
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a8"), Color.BLACK, false, false));
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a1"), Color.WHITE, false, false));
		builder.setPiece(new Pawn(BoardUtils.getCoordinateAtPosition("e7"), Color.BLACK, true));
		builder.setMoveMaker(Color.BLACK);
		final Board board = builder.build();
		// move pawn with pawn jump
		final Move pawnJumpMove = new PawnJump(board, 
				board.getTile(BoardUtils.getCoordinateAtPosition("e7")).getPiece(),
				BoardUtils.getCoordinateAtPosition("e5"));
		final Board newBoard = pawnJumpMove.execute();
		final Piece pieceAtDestination = newBoard.getTile(BoardUtils.getCoordinateAtPosition("e5")).getPiece();
		assertEquals("Pawn did not jump two squares", PieceType.PAWN, pieceAtDestination.getPieceType());
	}
	
	@Test
	public void blackPawnPromotionChangesPieceToQueen() {
		final BoardBuilder builder = new BoardBuilder();
		// setup a legal board with Black's turn to move
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a8"), Color.BLACK, false, false));
		builder.setPiece(new King(BoardUtils.getCoordinateAtPosition("a1"), Color.WHITE, false, false));
		builder.setPiece(new Pawn(BoardUtils.getCoordinateAtPosition("e2"), Color.BLACK));
		builder.setMoveMaker(Color.BLACK);
		final Board board = builder.build();
		// move black pawn to promotion square "e1"
		final Move promotionMove = Move.MoveFactory.createMove(board, 
				BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e1"));
		final Board newBoard = promotionMove.execute();
		final Piece pieceAtDestination = newBoard.getTile(BoardUtils.getCoordinateAtPosition("e1")).getPiece();
		assertEquals("Pawn did not promote to Queen", PieceType.QUEEN, pieceAtDestination.getPieceType());
	}

}
