package com.chess.engine.player.ai;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicLong;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveTransition;

public final class MiniMax extends Observable implements MoveStrategy {
	private final BoardEvaluator evaluator;
	private long boardsEvaluated;
	private long executionTime;
	private FreqTableRow[] freqTable;
	private int freqTableIndex;
	
	public MiniMax() {
		this.evaluator = new StandardBoardEvaluator();
		this.boardsEvaluated = 0;
	}
	
	@Override
	public String toString() {
		return "MiniMax";
	}
	
	@Override
	public long getNumBoardsEvaluated() {
		return this.boardsEvaluated;
	}

	@Override
	public Move execute(Board board, int depth) {
		final long startTime = System.currentTimeMillis();
		Move bestMove = Move.NULL_MOVE;
		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue;
		System.out.println(board.currentPlayer() + " Thinking with depth = " + depth);
		this.freqTable = new FreqTableRow[board.currentPlayer().getLegalMoves().size()];
		this.freqTableIndex = 0;
		int moveCounter = 1;
		int numMoves = board.currentPlayer().getLegalMoves().size();
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				FreqTableRow row = new FreqTableRow(move);
				freqTable[freqTableIndex] = row;
				currentValue = board.currentPlayer().getColor().isWhite() ?
						min(moveTransition.getTransitionBoard(), depth-1) :
						max(moveTransition.getTransitionBoard(), depth-1);
				System.out.println("\t" + toString() + " analyzing move (" +
						moveCounter + "/" + numMoves + ") " + move + " scores " +
						currentValue + " " + this.freqTable[this.freqTableIndex]);
				freqTableIndex++;
				if (board.currentPlayer().getColor().isWhite() &&
						currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
					bestMove = move;
				} else if (board.currentPlayer().getColor().isBlack() &&
						currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
					bestMove = move;
				}
			} else {
				System.out.println("\t" + toString() + " can't execute move (" +
						moveCounter + "/" + numMoves + ") " + move);
			}
			moveCounter++;
		}
		
		this.executionTime = System.currentTimeMillis() - startTime;
		System.out.printf("%s SELECTS %s [#boards = %d time taken = %d ms, rate = %.1f\n",
				board.currentPlayer(), bestMove, this.boardsEvaluated, this.executionTime,
				(1000 * ((double)this.boardsEvaluated / this.executionTime)));
		long total = 0;
		for (FreqTableRow row : freqTable) {
			if (row != null) {
				total += row.getCount();
			}
		}
		if (this.boardsEvaluated != total) {
			System.out.println("something is wrong with the # of boards evaluated");
		}
		return bestMove;
	}
	
	public int min(final Board board, final int depth) {
		if (depth == 0) {
			this.boardsEvaluated++;
			freqTable[freqTableIndex].increment();
			return this.evaluator.evaluate(board, depth);
		}
		if (isEndGameScenario(board)) {
			return this.evaluator.evaluate(board, depth);
		}
		int lowestSeenValue = Integer.MAX_VALUE;
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer()
					.makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(
						moveTransition.getTransitionBoard(), depth - 1);
				if (currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
			}
		}
		return lowestSeenValue;
	}

	public int max(final Board board, final int depth) {
		if (depth == 0) {
			this.boardsEvaluated++;
			freqTable[freqTableIndex].increment();
			return this.evaluator.evaluate(board, depth);
		}
		if (isEndGameScenario(board)) {
			return this.evaluator.evaluate(board, depth);
		}
		int highestSeenValue = Integer.MIN_VALUE;
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer()
					.makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(
						moveTransition.getTransitionBoard(), depth - 1);
				if (currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
			}
		}
		return highestSeenValue;
	}

	private static boolean isEndGameScenario(final Board board) {
		return board.currentPlayer().isInCheckMate() ||
			   board.currentPlayer().isInStaleMate();
	}
	
	private static class FreqTableRow {
		private final Move move;
		private final AtomicLong count;
		
		FreqTableRow(final Move move) {
			this.count = new AtomicLong();
			this.move = move;
		}
		
		public long getCount() {
			return this.count.get();
		}
		
		public void increment() {
			this.count.incrementAndGet();
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(move.getCurrentCoordinate()) +
				   BoardUtils.getPositionAtCoordinate(move.getDestinationCoordinate()) +
				   " : " + count;
		}
	}
	
}
