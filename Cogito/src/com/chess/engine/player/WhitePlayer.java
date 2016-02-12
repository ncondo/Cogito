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
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * @author ncondo
 *
 */
public class WhitePlayer extends Player {

	public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(
			Collection<Move> playerLegals, Collection<Move> opponentLegals) {
		
		final List<Move> kingCastles = new ArrayList<>();
		
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			// white's king side castle
			if (!this.board.getTile(61).isTileOccupied() &&
					!this.board.getTile(62).isTileOccupied()) {
				
				final Tile rookTile = this.board.getTile(63);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
							Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
							rookTile.getPiece().getPieceType().isRook()) {
						
						kingCastles.add(new KingSideCastleMove(this.board,
								this.playerKing, 62, (Rook)rookTile.getPiece(), 
								rookTile.getTileCoordinate(), 61));
					}
				}
			}
			// white's queen side castle
			if (!this.board.getTile(57).isTileOccupied() &&
					!this.board.getTile(58).isTileOccupied() &&
					!this.board.getTile(59).isTileOccupied()) {
				
				final Tile rookTile = this.board.getTile(56);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					// Check for attacks on 57?
					if (Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
							Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
							rookTile.getPiece().getPieceType().isRook()) {
						
						kingCastles.add(new QueenSideCastleMove(this.board,
								this.playerKing, 58, (Rook)rookTile.getPiece(),
								rookTile.getTileCoordinate(), 59));
					}
				}
			}
		}
		return Collections.unmodifiableList(kingCastles);
	}

}
