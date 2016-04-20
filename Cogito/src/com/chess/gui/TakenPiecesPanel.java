package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.GameBoard.MoveLog;

public class TakenPiecesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel northPanel;
	private final JPanel southPanel;
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
	
	public TakenPiecesPanel() {
		super(new BorderLayout());
		setBackground(PANEL_COLOR);
		setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8, 2));
		this.southPanel = new JPanel(new GridLayout(8, 2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
	}
	
	public void redo(final MoveLog moveLog) {
		this.northPanel.removeAll();
		this.southPanel.removeAll();
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();
		
		for (final Move move : moveLog.getMoves()) {
			if (move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				if (takenPiece.getPieceColor().isWhite()) {
					whiteTakenPieces.add(takenPiece);
				} else {
					blackTakenPieces.add(takenPiece);
				}
			}
		}
		
		for (final Piece takenPiece : whiteTakenPieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("assets/" +
						takenPiece.getPieceColor().toString().substring(0, 1) +
						takenPiece.toString()));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel();
				this.southPanel.add(imageLabel);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		for (final Piece takenPiece : blackTakenPieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("assets/" +
						takenPiece.getPieceColor().toString().substring(0, 1) +
						takenPiece.toString()));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel();
				this.northPanel.add(imageLabel);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		validate();
	}

}
