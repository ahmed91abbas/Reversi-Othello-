package src;

import java.util.ArrayList;

public class Move {
	protected String name;
	protected ArrayList<String> flippedDiscs;
	public Move(String name,  ArrayList<String> flippedDiscs) {
		this.name = name;
		this.flippedDiscs = flippedDiscs;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
    public boolean equals(Object o) {
    	return o.toString().equals(this.name);
    }
}