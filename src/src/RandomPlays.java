package src;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlays implements Player{
	
	private Reversi reversi;
	
	public RandomPlays(Reversi reversi) {
		this.reversi =reversi;
	}

	@Override
	public void makeMove(ArrayList<String> availableMoves) {
		if (!availableMoves.isEmpty()) {
			Random ran = new Random();
			int index = ran.nextInt(availableMoves.size());
			String move = availableMoves.get(index);
			reversi.makeMove(move);
		}
	}
}
