package src;

import java.util.ArrayList;
import java.util.HashMap;

public class AI implements Player {

	private Reversi reversi;
	private int depth;
	private String color;

	public AI(Reversi reversi, int depth, String color) {
		this.reversi = reversi;
		this.depth = depth;
		this.color = color;
	}

	private double minimax(String node, int depth) {
//		 System.out.println(node + " " + depth + " " + reversi.getCurrentPlayerColor());
		if (depth == 0 || reversi.gameOver) {
			if (this.color.equals("black")) {
//				return reversi.getNbrOfBlack() / ((double) reversi.getNbrOfBlack() + reversi.getNbrOfWhite());
				return reversi.getNbrOfBlack();
			} else {
//				return reversi.getNbrOfWhite() / ((double) reversi.getNbrOfBlack() + reversi.getNbrOfWhite());
				return reversi.getNbrOfWhite();
			}
		}
		
		reversi.makeMove(node);
		boolean maximizingPlayer;
		if (reversi.getCurrentPlayerColor().equals(this.color)) {
			maximizingPlayer = true;
		} else {
			maximizingPlayer = false;
		}
		if (maximizingPlayer) {
			double bestValue = Double.MIN_VALUE;
			ArrayList<String> availableMoves = copy(reversi.getAvailableMoves());
			for (int i = 0; i < availableMoves.size(); i++) {
				String move = availableMoves.get(i);
				if (i != 0) {
					String prev = availableMoves.get(i - 1);
					reversi.revert(prev);
				}
				double v = minimax(move, depth - 1);
				bestValue = Math.max(bestValue, v);
				if (i == (availableMoves.size() - 1))
					reversi.revert(availableMoves.get(i));
			}
			return bestValue;
		} else {
			double bestValue = Double.MAX_VALUE;
			ArrayList<String> availableMoves = copy(reversi.getAvailableMoves());
			for (int i = 0; i < availableMoves.size(); i++) {
				String move = availableMoves.get(i);
				if (i != 0) {
					String prev = availableMoves.get(i - 1);
					reversi.revert(prev);
				}
				double v = minimax(move, depth - 1);
				bestValue = (double) Math.min(bestValue, v);
				if (i == (availableMoves.size() - 1))
					reversi.revert(availableMoves.get(i));
			}
			return bestValue;
		}
	}

	@Override
	public void makeMove(ArrayList<String> original) {
		reversi.setSimulationMode(true);
		HashMap<String, Double> pointsOfMoves = new HashMap<String, Double>();
		ArrayList<String> availableMoves = copy(original);
		for (int i = 0; i < availableMoves.size(); i++) {
			String move = availableMoves.get(i);
			if (i != 0) {
				String prev = availableMoves.get(i - 1);
				reversi.revert(prev);
			}
			double point = minimax(move, depth);
			pointsOfMoves.put(move, point);
			if (i == availableMoves.size() - 1)
				reversi.revert(availableMoves.get(i));
		}
		System.out.println(pointsOfMoves.toString());
		String bestMove = "";
		double currentMax = -1;
		for (String key : pointsOfMoves.keySet()) {
			double point = pointsOfMoves.get(key);
			if (point >= currentMax) {
				currentMax = point;
				bestMove = key;
			}
		}
		reversi.setSimulationMode(false);
		reversi.makeMove(bestMove);
	}

	private ArrayList<String> copy(ArrayList<String> original) {
		ArrayList<String> copy = new ArrayList<String>();
		for(int i = 0; i < original.size(); i++) {
			copy.add(original.get(i));
		}
		return copy;
	}
}
