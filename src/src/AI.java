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

	private double minimax(String node, int depth, double alpha, double beta) {
//		 System.out.println(node + " " + depth + " " + reversi.getCurrentPlayerColor());
		if (depth == 0 || reversi.gameOver) {
			if (this.color.equals("black")) {
				return reversi.getNbrOfBlack() / ((double) reversi.getNbrOfBlack() + reversi.getNbrOfWhite());
			} else {
				return reversi.getNbrOfWhite() / ((double) reversi.getNbrOfBlack() + reversi.getNbrOfWhite());
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
				bestValue = Math.max(bestValue, minimax(move, depth - 1, alpha, beta));
				alpha = Math.max(alpha, bestValue);
				if (i == (availableMoves.size() - 1))
					reversi.revert(availableMoves.get(i));
				if (beta <= alpha)
					break;
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
				bestValue = Math.min(bestValue, minimax(move, depth - 1, alpha, beta));
				beta = Math.min(beta, bestValue);
				if (i == (availableMoves.size() - 1))
					reversi.revert(availableMoves.get(i));
				if (beta <= alpha)
					break;
			}
			return bestValue;
		}
	}

	@Override
	public void makeMove(ArrayList<String> original) {
		double alpha = Double.MIN_VALUE;
		double beta = Double.MAX_VALUE;
		reversi.setSimulationMode(true);
		HashMap<String, Double> pointsOfMoves = new HashMap<String, Double>();
		ArrayList<String> availableMoves = copy(original);
		for (int i = 0; i < availableMoves.size(); i++) {
			String move = availableMoves.get(i);
			if (i != 0) {
				String prev = availableMoves.get(i - 1);
				reversi.revert(prev);
			}
			double point = minimax(move, depth, alpha, beta);
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
		
		if(!reversi.getCurrentPlayerColor().equals(this.color)) {
			reversi.switchTurn();
			reversi.gameOver = false;
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
