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
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;

/**
 * @author ncondo
 *
 */
public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(
			Collection<Move> playerLegals, Collection<Move> opponentLegals) {
		
		final List<Move> kingCastles = new ArrayList<>();
		
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			// black's king side castle
			if (!this.board.getTile(5).isTileOccupied() &&
					!this.board.getTile(6).isTileOccupied()) {
				
				final Tile rookTile = this.board.getTile(7);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
							Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
							rookTile.getPiece().getPieceType().isRook()) {
						//TODO
						kingCastles.add(null);
					}
				}
			}
			// black's queen side castle
			if (!this.board.getTile(1).isTileOccupied() &&
					!this.board.getTile(2).isTileOccupied() &&
					!this.board.getTile(3).isTileOccupied()) {
				
				final Tile rookTile = this.board.getTile(0);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					// CHECK IF TILES ARE BEING ATTACKED?
					//TODO
					kingCastles.add(null);
				}
			}
		}
		return Collections.unmodifiableList(kingCastles);
	}

}
