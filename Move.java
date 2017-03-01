
public class Move {

	private static final String LEFT = "L";
	private static final String RIGHT = "R";
	
	private String direction = "";
	
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
