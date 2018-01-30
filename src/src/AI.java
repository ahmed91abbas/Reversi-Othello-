package src;

import java.util.ArrayList;
import java.util.HashMap;

public class AI implements Player{
	
	private Reversi reversi;
	private int depth;
	
	public AI(Reversi reversi, int depth) {
		this.reversi = reversi;
		this.depth = depth;
	}
	
	private int minimax(String node, int depth, boolean maximizingPlayer) {
		reversi.makeMove(node);
		if (depth == 0 || reversi.gameOver) {
			if(node.equals("black")) {
				return reversi.getNbrOfBlack();
			} else {
				return reversi.getNbrOfWhite();
			}
		}
		
		if (maximizingPlayer) {
			int bestValue = Integer.MIN_VALUE;
			ArrayList<String> availableMoves = reversi.getAvailableMoves();
			for (String move : availableMoves) {
				int v = minimax(move, depth - 1, false);
				bestValue = Math.max(bestValue, v);
				//TODO revert
			}
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			ArrayList<String> availableMoves = reversi.getAvailableMoves();
			for (String move : availableMoves) {
				int v = minimax(move, depth - 1, true);
				bestValue = Math.min(bestValue, v);
				//TODO revert
			}
			return bestValue;
		}
	}
	
	@Override
	public void makeMove(ArrayList<String> availableMoves) {
		HashMap<String, Integer> pointsOfMoves = new HashMap<String, Integer>();
		for (String move : availableMoves) {
			int point = minimax(move, depth, true);
			pointsOfMoves.put(move, point);
			//TODO revert
		}
		String bestMove = "";
		int currentMax = -1;
		for (String key : pointsOfMoves.keySet()) {
			int point = pointsOfMoves.get(key);
			if (point >= currentMax) {
				currentMax = point;
				bestMove = key;
			}
		}
		reversi.makeMove(bestMove);
	}

}
