package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Reversi {
	private HashMap<String, Disc> field;
	private int turnCount;
	private char[] letters;
	private HashMap<String, Disc> black;
	private HashMap<String, Disc> white;
	private HashMap<String, JButton> buttons;
	private ArrayList<String> availableMoves;
	private boolean allowAllMoves;
	private boolean whiteHasMoves;
	private boolean blackHasMoves;
	private Player p1;
	private Player p2;
	private boolean gameOver;
	
	public Reversi() {
		turnCount = 0;
		String str = " abcdefgh ";
		letters = str.toCharArray();
		field = new HashMap<String, Disc>();
		black = new HashMap<String, Disc>();
		white = new HashMap<String, Disc>();
		buttons = new HashMap<String, JButton>();
		availableMoves = new ArrayList<String>();
		allowAllMoves = false;
		whiteHasMoves = false;
		blackHasMoves = false;
		gameOver = false;
		p1 = null;
		p2 = null;
	}

	public void allowAllMoves(boolean state){
		allowAllMoves = state;
	}
	
	public void makeMove(String name) {
		JButton button = buttons.get(name);
		int playerID = turnCount % 2;
		boolean succ = addDisc(name, playerID);
		int blackDiscs = black.keySet().size();
		int whiteDiscs = white.keySet().size();
		if (succ) {
			String nextPlayer = "";
			switch (playerID) {
			case 0:
				button.setBackground(Color.BLACK);
				nextPlayer = "white";
				break;
			case 1:
				button.setBackground(Color.WHITE);
				nextPlayer = "black";
				break;
			}
			
			if (turnCount >= 64) {
				nextPlayer = "none";
				gameOver = true;
			}
			
			System.out.println("Next is: " + nextPlayer + " (Black discs = " + blackDiscs + ", White discs = " + whiteDiscs + ")");
			
			turnCount++;
			
			if (turnCount >= 64) {
				clearAvailableMoves();
				gameOver = true;
			}
			
			if (gameOver) {
				if (blackDiscs > whiteDiscs) {
					System.out.println("\nThe winner is BLACK!");
				} else if (whiteDiscs > blackDiscs) {
					System.out.println("\nThe winner is WHITE!");
				} else {
					System.out.println("\nThe game finished with a tie!");
				}
				System.out.println("Black scored " + blackDiscs + " points.");
				System.out.println("White scored " + whiteDiscs + " points.");
			}
			
			if (!allowAllMoves && !gameOver) {
				updateAvailableMoves();
				if (nextPlayer.equals("black") && p1 != null) {
					p1.makeMove(availableMoves);
				} else if (nextPlayer.equals("white") && p2 != null) {
					p2.makeMove(availableMoves);
				}
			}	
			
		} 
	}

	private JButton createButton(final String name) {
		JButton button = new JButton();
		buttons.put(name, button);
		button.setPreferredSize(new Dimension(40, 40));
		button.setBackground(Color.green);
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeMove(name);
			}
		});
		return button;
	}

	private JTextField createTextField(String txt) {
		Font font = new Font("SansSerif", Font.BOLD, 20);
		JTextField jt = new JTextField(txt);
		jt.setPreferredSize(new Dimension(40, 40));
		jt.setEditable(false);
		jt.setBackground(Color.LIGHT_GRAY);
		jt.setHorizontalAlignment(JTextField.CENTER);
		jt.setFont(font);
		return jt;
	}

	public void createField() {
		JFrame frame = new JFrame("Reversi");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		GridLayout layout = new GridLayout(10, 10);
		JPanel panel = new JPanel();
		panel.setLayout(layout);

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (row == 0 || row == 9) {
					if (col == 0 || col == 9) {
						panel.add(createTextField(""));
					} else {
						panel.add(createTextField(letters[col] + ""));
					}
				} else if (col == 0 || col == 9) {
					panel.add(createTextField(row + ""));
				} else {
					String name = letters[col] + "" + row;
					panel.add(createButton(name));
				}
			}
		}

		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void checkDirection(Disc core, int addToX, int addToY) {
		boolean checkNext = true;
		boolean foundDiff = false;
		boolean sameColor = false;
		int x = core.getX() + addToX;
		int y = core.getY() + addToY;
		while (checkNext && !outOfBounds(x, y)) {
			String name = letters[y] + "" + x;
			Disc nieghbour = field.get(name);
			if (nieghbour != null) {
				sameColor = nieghbour.getColor().equals(core.getColor());				
			}
			if ((nieghbour == null && !foundDiff) || sameColor) {
				checkNext = false;
			} else if (nieghbour == null && foundDiff) {
				availableMoves.add(name);
				break;
			} else {
				foundDiff = true;
			}
			x = x + addToX;
			y = y + addToY;
		}
	}
	
	public void clearAvailableMoves() {
		for (int i = 0; i < availableMoves.size(); i++) {
			JButton button = buttons.get(availableMoves.get(i));
			button.setText("");
		}
		availableMoves.clear();
	}
	
	public void updateAvailableMoves() {
		clearAvailableMoves();
		int playerID = turnCount % 2;
		HashMap<String, Disc> player = null;
		switch(playerID) {
			case 0: player = black; break;
			case 1: player = white; break;
		}
		for(String key : player.keySet()) {
			Disc core = field.get(key);
			checkDirection(core, -1, 0); //up
			checkDirection(core, 1, 0); //down
			checkDirection(core, 0, -1); //left
			checkDirection(core, 0, 1); //right
			checkDirection(core, -1, -1); //up left
			checkDirection(core, 1, 1); //down right
			checkDirection(core, -1, 1); // up right
			checkDirection(core, 1, -1); //down left
		}
		for (int i = 0; i < availableMoves.size(); i++) {
			JButton button = buttons.get(availableMoves.get(i));
			button.setText("*");
		}
		if (availableMoves.size() > 0) {
			switch(playerID){
				case 0: blackHasMoves = true; break;
				case 1: whiteHasMoves = true; break;
			}
		} else if(!allowAllMoves && turnCount < 64) {
			if (!whiteHasMoves && !blackHasMoves) {
				System.out.println("\nThe game has finished because both players has no available moves left!");
				String winner = black.size() > white.size() ? "BLACK" : "WHITE";
				System.out.println("\nThe winner is " + winner);
				System.out.println("Black scored " + black.size() + " points.");
				System.out.println("White scored " + white.size() + " points.");
				gameOver = true;
			} else if (playerID == 0) {
				blackHasMoves = false;
				System.out.println("\nPlayer Black has no available moves! Switching turns");
				System.out.println("Next is: white (Black discs = " + black.size() + ", White discs = " + white.size() + ")");
				turnCount++;
				triggerMove(p2);
			} else if (playerID == 1) {
				whiteHasMoves = false;
				System.out.println("\nPlayer White has no available moves! Switching turns");
				System.out.println("Next is: black (Black discs = " + black.size() + ", White discs = " + white.size() + ")");
				turnCount++;
				triggerMove(p1);
			}
		}
	}
	
	private void flipDiscs(ArrayList<String> discsToFlip) {
		for (int i = 0; i < discsToFlip.size(); i++) {
			String key = discsToFlip.get(i);
			Disc disc = field.get(key);
			disc.flip();
			field.put(key, disc);
			if (white.containsKey(key)) {
				white.remove(key);
				black.put(key, disc);
			} else {
				black.remove(key);
				white.put(key, disc);
			}
			if(disc.getColor() == "black") {
				buttons.get(key).setBackground(Color.BLACK);
			} else {
				buttons.get(key).setBackground(Color.WHITE);			
			}
		}
	}
	
	private void update(Disc core, int addToX, int addToY){
		ArrayList<String> discsToFlip = new ArrayList<String>();
		int x = core.getX() + addToX;
		int y = core.getY() + addToY;
		boolean foundDiff = false;
		boolean flip = false;
		String key = letters[y] + "" + x; 
		while (!outOfBounds(x, y) && field.get(key) != null) {
			Disc next = field.get(key);
			foundDiff = !next.getColor().equals(core.getColor());
			if(foundDiff) {
				discsToFlip.add(key);
			} else {
				flip = true;
				break;
			}
			x = x + addToX;
			y = y + addToY;
			key = letters[y] + "" + x;
		}
		if(flip)
			flipDiscs(discsToFlip);
	}
	
	public void updateBoard(Disc core) {
		update(core, 1, 0); //down
		update(core, -1, 0); //up
		update(core, 0, 1); //right
		update(core, 0, -1); //left
		update(core, 1, 1); //down right
		update(core, -1, -1); //up left
		update(core, -1, 1); //down left
		update(core, 1, -1); //up right 
	}

	public boolean addDisc(String name, int playerID) {
		if (!allowAllMoves && (field.containsKey(name) || !availableMoves.contains(name))) {
			return false;
		} else {
			Disc n = new Disc(playerID, name);
			field.put(name, n);
			if (playerID == 0) {
				black.put(name, n);
			} else {
				white.put(name, n);
			}
			updateBoard(n);
			return true;
		}
	}
	
	private boolean outOfBounds(int x, int y) {
		int rows = 8;
		int columns = 8;
		if (x < 1 || x > rows || y < 1 || y > columns)
			return true;
		return false;
	}
	
	public void blackPlayer(Player p1) {
		this.p1 = p1;
	}
	
	public void whitePlayer(Player p2) {
		this.p2 = p2;
	}
	
	public void triggerMove(Player p) {
		updateAvailableMoves();
		if (p != null)
			p.makeMove(availableMoves);
	}

	public static void start() {
		JDialog dialog = new JDialog();
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Reversi start menu");
		JPanel[] panelList = new JPanel[4];
		Font font = new Font("Serif", Font.PLAIN, 30);
		for (int i = 0; i < panelList.length; i++) {
			panelList[i] = new JPanel();
			panelList[i].setBackground(Color.decode("#E6CFB8"));
		}
		JLabel startLabel = new JLabel("*Choose players*");
		startLabel.setFont(font);
		panelList[0].add(startLabel);
		JLabel blackPlayerLabel = new JLabel("Black discs ");
		blackPlayerLabel.setFont(font);
		String[] players = new String[] {"Human", "Random plays", "AI"};
		JComboBox<String> box1 = new JComboBox<String>(players);
		box1.setFont(font);
		panelList[1].add(blackPlayerLabel);
		panelList[1].add(box1);
		JLabel whitePlayerLabel = new JLabel("White discs ");
		whitePlayerLabel.setFont(font);
		JComboBox<String> box2 = new JComboBox<String>(players);
		box2.setFont(font);
		panelList[2].add(whitePlayerLabel);
		panelList[2].add(box2);
		JButton startButton = new JButton("Start");
		startButton.setFont(font);
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					dialog.dispose();
					Player p1 = null;
					Player p2 = null;
					String player1 = (String) box1.getSelectedItem();
					String player2 = (String) box2.getSelectedItem();
					Reversi reversi = new Reversi();
					reversi.createField();
					reversi.allowAllMoves(true);
					reversi.makeMove("d5");
					reversi.makeMove("e5");
					reversi.makeMove("e4");
					reversi.makeMove("d4");
					reversi.allowAllMoves(false);
					if (player1.equals("Random plays")) {
						p1 = new RandomPlays(reversi);						
					} else if (player1.equals("AI")) {
						//TODO
					}
					if (player2.equals("Random plays")) {
						p2 = new RandomPlays(reversi);						
					} else if (player2.equals("AI")) {
						//TODO
					}
					reversi.blackPlayer(p1);
					reversi.whitePlayer(p2);
					reversi.triggerMove(p1);
				} catch (Exception e) {
					start();
				}
			}			
		});
		panelList[3].add(startButton);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		for (int i = 0; i < panelList.length; i++)
			panel.add(panelList[i]);
		dialog.add(panel);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public static void main(String[] args) {
		start();
	}
}
