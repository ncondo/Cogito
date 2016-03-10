package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.GameBoard;


public class Cogito {

	public static void main(String[] args) {
		
		Board board = Board.createStandardBoard();
		
		System.out.println(board);
		
		@SuppressWarnings("unused")
		GameBoard table = new GameBoard();

	}

}
