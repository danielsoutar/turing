
public class Move {

	private static final String LEFT = "L";
	private static final String RIGHT = "R";
	
	private String direction = "";
	
	/**
	 * Move constructor that accepts a direction. 
	 * Throws an InputException if the direction is invalid.
	 **/
	public Move(String direction) throws InputException {
		if(direction.equals(LEFT) || direction.equals(RIGHT))
			this.direction = direction;
		else
			throw new InputException("Error: a turing machine can only move left (L) or right (R)");
	}
	
	public String getDirection() {
		return direction;
	}
	
}
