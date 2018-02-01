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
//		System.out.println(node + "   " + depth);
		reversi.makeMove(node);
		if (depth == 0 || reversi.gameOver) {
			String color = reversi.getBoxColor(node);
			if(color.equals("black")) {
				return reversi.getNbrOfBlack();
			} else {
				return reversi.getNbrOfWhite();
			}
		}
		
		if (maximizingPlayer) {
			int bestValue = Integer.MIN_VALUE;
			ArrayList<String> availableMoves = reversi.getAvailableMoves();
			for (int i = 0; i < availableMoves.size(); i++) {
				String move = availableMoves.get(i);
				if (i != 0) {
					String prev = availableMoves.get(i - 1);
					reversi.revert(prev);
				}
				int v = minimax(move, depth - 1, false);
				bestValue = Math.max(bestValue, v);
				if (i == availableMoves.size() - 1)
					reversi.revert(availableMoves.get(i));
			}
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			ArrayList<String> availableMoves = reversi.getAvailableMoves();
			for (int i = 0; i < availableMoves.size(); i++) {
				String move = availableMoves.get(i);
				if (i != 0) {
					String prev = availableMoves.get(i - 1);
					reversi.revert(prev);
				}
				int v = minimax(move, depth - 1, false);
				bestValue = Math.min(bestValue, v);
				if (i == availableMoves.size() - 1)
					reversi.revert(availableMoves.get(i));
			}
			return bestValue;
		}
	}
	
	@Override
	public void makeMove(ArrayList<String> availableMoves) {
		reversi.setSimulationMode(true);
		HashMap<String, Integer> pointsOfMoves = new HashMap<String, Integer>();
		for (int i = 0; i < availableMoves.size(); i++) {
			String move = availableMoves.get(i);
			if (i != 0) {
				String prev = availableMoves.get(i - 1);
				reversi.revert(prev);
			}
			int point = minimax(move, depth, true);
			pointsOfMoves.put(move, point);
			if (i == availableMoves.size() - 1)
				reversi.revert(availableMoves.get(i));
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
		reversi.setSimulationMode(false);
		reversi.makeMove(bestMove);
	}

}
