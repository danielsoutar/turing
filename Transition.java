
public class Transition {

	private State state1;
	private char input;
	private State state2;
	private char output;
	private Move move;
	
	private static final char emptyCharacter = '_';
	public static final String LEFT = "L";
	public static final String RIGHT = "R";
	
	public Transition(State state1, char tape_input, State state2, char tape_output, Move move) {
		this.state1 = state1;
		this.input = tape_input;
		this.state2 = state2;
		this.output = tape_output;
		this.move = move;
	}
	
	public State getInitialState() {
		return state1;
	}
	
	public char getTapeInput() {
		return input;
	}
	
	public State getResultState() {
		return state2;
	}
	
	public char getTapeOutput() {
		return output;
	}
	
	public String getMoveDirection() {
		return move.getDirection();
	}
	
	public String print() {
		String print = "";
		print += "<" + state1.getName() + "> ";
		print += "<" + input + "> ";
		print += "<" + state2.getName() + "> ";
		print += "<" + output + "> ";
		print += "<" + move.getDirection() + ">";
		return print;
	}

	public static char emptyCharacter() {
		return emptyCharacter;
	}
}