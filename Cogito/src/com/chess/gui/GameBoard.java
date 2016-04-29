/**
 * 
 */
package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.AlphaBetaWithMoveOrdering;


public final class GameBoard extends Observable {
	private final JFrame gameFrame;
	private final GameHistoryPanel gameHistoryPanel;
	private final TakenPiecesPanel takenPiecesPanel;
	private final BoardPanel boardPanel;
	private final DebugPanel debugPanel;
	private final GameSetup gameSetup;
	private Move computerMove;
	private Board chessBoard;
	private final MoveLog moveLog;
	private boolean highlightLegalMoves;
	private String pieceIconPath;
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private final Color lightTileColor = Color.decode("#FFFACD");
	private final Color darkTileColor = Color.decode("#593E1A");
	private static final GameBoard INSTANCE = new GameBoard();
	
	public GameBoard() {
		this.gameFrame = new JFrame("Cogito");
		final JMenuBar gameMenuBar = new JMenuBar();
		populateMenuBar(gameMenuBar);
		this.gameFrame.setJMenuBar(gameMenuBar);
		this.gameFrame.setLayout(new BorderLayout());
		this.chessBoard = Board.createStandardBoard();
		this.highlightLegalMoves = false;
		this.pieceIconPath = "assets/";
		this.gameHistoryPanel = new GameHistoryPanel();
		this.takenPiecesPanel = new TakenPiecesPanel();
		this.boardPanel = new BoardPanel();
		this.debugPanel = new DebugPanel();
		this.moveLog = new MoveLog();
		this.addObserver(new GameAIWatcher());
		this.gameSetup = new GameSetup(this.gameFrame, true);
		this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
		this.gameFrame.add(debugPanel, BorderLayout.SOUTH);
		//setDefaultLookAndFeelDecorated(true);
		this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		center(this.gameFrame);
		this.gameFrame.setVisible(true);
	}
	
	public static GameBoard get() {
		return INSTANCE;
	}
	
	private JFrame getGameFrame() {
		return this.gameFrame;
	}
	
	Board getGameBoard() {
		return this.chessBoard;
	}
	
	MoveLog getMoveLog() {
		return this.moveLog;
	}
	
	BoardPanel getBoardPanel() {
		return this.boardPanel;
	}
	
	GameHistoryPanel getGameHistoryPanel() {
		return this.gameHistoryPanel;
	}
	
	TakenPiecesPanel getTakenPiecesPanel() {
		return this.takenPiecesPanel;
	}
	
	DebugPanel getDebugPanel() {
		return this.debugPanel;
	}
	
	GameSetup getGameSetup() {
		return this.gameSetup;
	}
	
	private void updateGameBoard(final Board board) {
		this.chessBoard = board;
	}
	
	private void updateComputerMove(final Move move) {
		this.computerMove = move;
	}
	
	public void moveMadeUpdate(final PlayerType playerType) {
		setChanged();
		notifyObservers(playerType);
	}
	// TODO
	private void undoLastMove() {
		
	}
	// TODO
	private void undoAllMoves() {
		
	}
	
	public void show() {
		GameBoard.get().getMoveLog().clear();
		GameBoard.get().getGameHistoryPanel().redo(chessBoard, GameBoard.get().getMoveLog());
		GameBoard.get().getTakenPiecesPanel().redo(GameBoard.get().getMoveLog());
		GameBoard.get().getBoardPanel().drawBoard(GameBoard.get().getGameBoard());
		GameBoard.get().getDebugPanel().redo();
	}
	
	private static void center(final JFrame frame) {
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		final int w = frame.getSize().width;
		final int h = frame.getSize().height;
		final int x = (dim.width - w) / 2;
		final int y = (dim.height - h) / 2;
		frame.setLocation(x, y);
	}
	
	private void populateMenuBar(final JMenuBar gameMenuBar) {
		gameMenuBar.add(createFileMenu());
		gameMenuBar.add(createPreferencesMenu());
		gameMenuBar.add(createOptionsMenu());
	}
	
	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private JMenu createOptionsMenu() {
		final JMenu optionsMenu = new JMenu("Options");
		final JMenuItem resetMenuItem = new JMenuItem("New Game");
		resetMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				//undoAllMoves();
			}
		});
		optionsMenu.add(resetMenuItem);
		return optionsMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JCheckBoxMenuItem cbLegalMoveHighlighter = new JCheckBoxMenuItem(
				"Highlight Legal Moves", false);
		cbLegalMoveHighlighter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				highlightLegalMoves = cbLegalMoveHighlighter.isSelected();
			}
		});
		preferencesMenu.add(cbLegalMoveHighlighter);
		return preferencesMenu;
	}
	
	private class BoardPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		final List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8, 8));
			this.boardTiles = new ArrayList<>();
			for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		public void drawBoard(final Board board) {
			removeAll();
			for (final TilePanel boardTile : boardTiles) {
				boardTile.drawTile(board);
				add(boardTile);
			}
			validate();
			repaint();
		}
	}
	
	private class TilePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int tileID;
		
		TilePanel(final BoardPanel boardPanel, final int tileID) {
			super(new GridBagLayout());
			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (sourceTile == null) {
							sourceTile = chessBoard.getTile(tileID);
							humanMovedPiece = sourceTile.getPiece();
							if (humanMovedPiece == null) {
								sourceTile = null;
							}
						} else {
							destinationTile = chessBoard.getTile(tileID);
							final Move move = Move.MoveFactory.createMove(
									chessBoard, sourceTile.getTileCoordinate(),
									destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if (transition.getMoveStatus().isDone()) {
								chessBoard = transition.getTransitionBoard();
								moveLog.addMove(move);
							}
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}
					} else if (SwingUtilities.isRightMouseButton(e)) {
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					}
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							gameHistoryPanel.redo(chessBoard, moveLog);
							takenPiecesPanel.redo(moveLog);
							if (gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
								GameBoard.get().moveMadeUpdate(PlayerType.HUMAN);
							}
							boardPanel.drawBoard(chessBoard);
							debugPanel.redo();
						}
					});
				}
				
				@Override
				public void mousePressed(final MouseEvent e) { }
				@Override
				public void mouseReleased(final MouseEvent e) { }
				@Override
				public void mouseEntered(final MouseEvent e) { }
				@Override
				public void mouseExited(final MouseEvent e) { }
			});
			validate();
		}
		
		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegalMoves(board);
			validate();
			repaint();
		}
		
		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if (board.getTile(this.tileID).isTileOccupied()) {
				try {
					// Example: white bishop == "WB.gif"
					final BufferedImage image = ImageIO.read(new File(pieceIconPath +
						board.getTile(this.tileID).getPiece().getPieceColor().toString().substring(0, 1) +
						"" + board.getTile(this.tileID).getPiece().toString() + ".gif"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void assignTileColor() {
			if (BoardUtils.EIGHTH_ROW[this.tileID] ||
					BoardUtils.SIXTH_ROW[this.tileID] ||
					BoardUtils.FOURTH_ROW[this.tileID] ||
					BoardUtils.SECOND_ROW[this.tileID]) {
				setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
			} else if (BoardUtils.SEVENTH_ROW[this.tileID] ||
					BoardUtils.FIFTH_ROW[this.tileID] ||
					BoardUtils.THIRD_ROW[this.tileID] ||
					BoardUtils.FIRST_ROW[this.tileID]) {
				setBackground(this.tileID % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
		
		private void highlightLegalMoves(final Board board) {
			for (final Move move : pieceLegalMoves(board)) {
				if (move.getDestinationCoordinate() == this.tileID) {
					try {
						add(new JLabel(new ImageIcon(ImageIO.read(
								new File("assets/green_highlight.png")))));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private Collection<Move> pieceLegalMoves(final Board board) {
			if (humanMovedPiece != null && 
					humanMovedPiece.getPieceColor() == board.currentPlayer().getColor()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}
	}
	
	public static class MoveLog {
		private final List<Move> moves;
		
		MoveLog() {
			this.moves = new ArrayList<>();
		}
		
		public List<Move> getMoves() {
			return this.moves;
		}
		
		public void addMove(final Move move) {
			this.moves.add(move);
		}
		
		public int size() {
			return this.moves.size();
		}
		
		public void clear() {
			this.moves.clear();
		}
		
		public Move removeMove(int index) {
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move) {
			return this.moves.remove(move);
		}
	}
	
	public enum PlayerType {
		HUMAN,
		COMPUTER
	}
	
	private static class GameAIWatcher implements Observer {
		
		@Override
		public void update(final Observable o, final Object arg) {
			if (GameBoard.get().getGameSetup().isAIPlayer(GameBoard.get().getGameBoard().currentPlayer()) &&
					!GameBoard.get().getGameBoard().currentPlayer().isInCheckMate() &&
					!GameBoard.get().getGameBoard().currentPlayer().isInStaleMate()) {
				System.out.println(GameBoard.get().getGameBoard().currentPlayer() +
						" is set to AI, thinking...");
				final AIThinkTank thinkTank = new AIThinkTank();
				thinkTank.execute();
			}
			
			if (GameBoard.get().getGameBoard().currentPlayer().isInCheckMate()) {
				JOptionPane.showMessageDialog(GameBoard.get().getBoardPanel(),
						"Game Over: Player " + GameBoard.get().getGameBoard().currentPlayer() +
						" is in checkmate!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
			}
			
			if (GameBoard.get().getGameBoard().currentPlayer().isInStaleMate()) {
				JOptionPane.showMessageDialog(GameBoard.get().getBoardPanel(),
						"Game Over: Player " + GameBoard.get().getGameBoard().currentPlayer() +
						" is in stalemate!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private static class AIThinkTank extends SwingWorker<Move, String> {
		
		private AIThinkTank() {
		}
		
		@Override
		protected Move doInBackground() throws Exception {
			final Move bestMove;
			
			final int moveNumber = GameBoard.get().getMoveLog().size();
			final int quiescenceFactor = 2000 + (100 * moveNumber);
			final AlphaBetaWithMoveOrdering strategy = new AlphaBetaWithMoveOrdering(0/*quiescenceFactor*/);
			strategy.addObserver(GameBoard.get().getDebugPanel());
			GameBoard.get().getGameBoard().currentPlayer().setMoveStrategy(strategy);
			bestMove = GameBoard.get().getGameBoard().currentPlayer().getMoveStrategy().execute(
					GameBoard.get().getGameBoard(), GameBoard.get().getGameSetup().getSearchDepth());
			return bestMove;
		}
		
		@Override
		public void done() {
			try {
				final Move bestMove = get();
				GameBoard.get().updateComputerMove(bestMove);
				GameBoard.get().updateGameBoard(GameBoard.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
				GameBoard.get().getMoveLog().addMove(bestMove);
				GameBoard.get().getGameHistoryPanel().redo(GameBoard.get().getGameBoard(), GameBoard.get().getMoveLog());
				GameBoard.get().getTakenPiecesPanel().redo(GameBoard.get().getMoveLog());
				GameBoard.get().getBoardPanel().drawBoard(GameBoard.get().getGameBoard());
				GameBoard.get().getDebugPanel().redo();
				GameBoard.get().moveMadeUpdate(PlayerType.COMPUTER);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
