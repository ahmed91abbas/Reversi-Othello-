package src;

public class Disc {
	private int playerID;
	private String name;
	private int x;
	private int y;
	
	public Disc(int playerID, String name) {
			this.playerID = playerID;
			this.name = name;
			String str = " abcdefgh ";
			x = Integer.parseInt(name.substring(1));
			y = str.indexOf(name.substring(0, 1));
		}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String getColor() {
		switch (playerID) {
			case 0: return "black";
			case 1: return "white";
		}
		return "";
	}
	
	public void flip() {
		playerID = (playerID + 1) % 2;
	}
}
