import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TuringMachine {

	/**
	 * These are constants to do with the format of the machine description. 
	 **/
	private static final int N_LINE_SIZE = 2;
	private static final int MAX_STATE_SIZE = 2;
	private static final int NORMAL_STATE_SIZE = 1;
	private final int TRANSITION_LENGTH = 5;

	private int n = 0;
	private State[] states;
	private char[] alphabet;

	private ArrayList<Transition> transition_table;

	private int position = 0;
	private State start_state;
	private State current_state;
	private char current_input;

	public State[] getStates() {
		return states;
	}

	public char[] getAlphabet() {
		return alphabet;
	}

	public ArrayList<Transition> getTransitionTable() {
		return transition_table;
	}

	/**
	 * Takes a machine description and parses it for its number of states, the set
	 * of states including the accept and reject states, the alphabet, and the 
	 * transition table. Throws an InputException if any of these fail to parse, and 
	 * throws a FileNotFoundException if there is no file.
	 */
	public void initialise(String turing_description) throws InputException, FileNotFoundException {
		Scanner scanner = new Scanner(new File(turing_description));

		String line = scanner.nextLine();
		retrieveN(line);

		states = new State[n];

		for(int i = 0; i < n; i++)
			retrieveState(scanner.nextLine(), i);

		start_state = states[0];

		retrieveAlphabet(scanner.nextLine());

		transition_table = new ArrayList<Transition>();

		while(scanner.hasNext())
			retrieveTransition(scanner.nextLine());

		scanner.close();
	}

	/**
	 * Takes in an input line from the input file, the interactive and performance flags,
	 * and sets its position and state to start evaluating. Throws an InputException if
	 * the input line is malformed.
	 **/
	public boolean canAccept(ArrayList<Character> input, boolean i_mode, boolean p_mode) throws InputException {
		position = 0;
		current_state = start_state;
		current_input = input.get(position).charValue();
		return accept(input, i_mode, p_mode);
	}

	/**
	 * From its starting state and position, the machine evaluates the input by examining
	 * its current state and the initial input, transitions into the resulting state, 
	 * writes the corresponding output, and shifts left or right along the input
	 * depending on the transition function.
	 * 
	 * If it encounters either the accept or reject state, it simply breaks the loop
	 * and returns true if it is an accept state and false otherwise.
	 * 
	 * Optionally, it prints the new transition in each iteration if the -I flag was set.
	 * Optionally, it prints the number of steps taken at the end if the -P flag was set.
	 * 
	 * Throws an InputException if the input line is malformed.
	 **/
	private boolean accept(ArrayList<Character> input, boolean i_mode, boolean p_mode) throws InputException {
		Object[] original_input = input.toArray();
		int number_of_steps = 0;
		while(current_state.isDefault()) {
			Transition t = getTransitionContaining(current_state, current_input);
			current_state = t.getResultState();
			if(!current_state.isDefault())
				break;
			input.set(position, t.getTapeOutput());
			
			position = shift(position, t.getMoveDirection(), input.size());

			current_input = input.get(position);

			if(i_mode) 
				printTransition(input, t);
			number_of_steps++;
		}

		if(p_mode)
			printSteps(original_input, number_of_steps);

		return current_state.isAcceptState();
	}

	private void printSteps(Object[] original_input, int number_of_steps) {
		String output = "";
		for(Object o : original_input)
			output += ((Character) o).charValue();
		System.out.println("########################");
		System.out.println("Number of steps on input " + output + ": ");
		System.out.println(number_of_steps);
	}

	/**
	 * This is a nicely formatted printing of the transition if the -I flag was set.
	 * If the input is very large, the program speeds up, and otherwise slows down so
	 * that the transitions being printed are actually readable.
	 **/
	private synchronized void printTransition(ArrayList<Character> input, Transition t) {
		boolean needSpeed = false;
		for(int i = 0; i < input.size() && i < runtm.SCREEN_SIZE; i++)
			System.out.print(input.get(i));

		if(input.size() >= runtm.SCREEN_SIZE) {
			System.out.print("...");
			needSpeed = true;
		}
		System.out.println();
		System.out.println(t.print());
		try {
			if(needSpeed)
				wait(10);
			else
				wait(80);
		} 
		catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Determines where the machine should move based on the move in the transition.
	 * If it reads below 0, it sets to 0. Since the representation of the input
	 * is dynamic and therefore as close to 'infinitely long in one direction' as
	 * is realistically possible, the only way you could read beyond the maximum is
	 * an issue for the maximum size of an integer in java.
	 **/
	private int shift(int position, String moveDirection, int max) {
		if(moveDirection.equals(Transition.LEFT))
			position--;
		else if(moveDirection.equals(Transition.RIGHT))
			position++;
		if(position < 0)
			position++;
		return position;
	}

	/**
	 * Note that since this is a deterministic TM, there is exactly one transition
	 * with the corresponding current_state/current_input pair.
	 * Throws an InputException if there is no transition available.
	 **/
	private Transition getTransitionContaining(State current_state, char current_input) throws InputException {
		for(Transition t : transition_table)
			if(t.getInitialState().equals(current_state) && t.getTapeInput() == current_input)
				return t;
		throw new InputException("Error: there is no transition possible given the state and input.");
	}

	/**
	 * Gets the number of states. This number must be greater than 0 and less than 
	 * Integer.MAX_VALUE. Throws an InputException if it isn't.
	 **/
	private void retrieveN(String line) throws InputException {
		String[] components = line.split(" ");
		if(components.length != N_LINE_SIZE)
			throw new InputException("Error: first line of machine description should be 'states n', where n is the number of states");

		n = parse(components[1]);
		if(n < 0 || n > Integer.MAX_VALUE)
			throw new InputException("Error: number of states too large for java int data type.");

	}

	/**
	 * Attempts to parse the number of states. Essentially converts a 
	 * NumberFormatException to an InputException if it fails.
	 **/
	private int parse(String input) throws InputException {
		try {
			n = Integer.parseInt(input);
			return n;
		}
		catch (NumberFormatException e) {
			throw new InputException("Error: n (number of states) is not a number");
		}
	}

	/**
	 * Retrieves a state, which must have a unique name, and there must be exactly one
	 * accept state, and exactly one reject state. There must also be n states, and 
	 * n - 2 'default' states.
	 * Throws an InputException if the line has too many elements, or too few, or if
	 * a state already exists with the same name or if there are multiple accept/reject
	 * states.
	 **/
	private void retrieveState(String line, int counter) throws InputException {
		String[] components = line.split(" ");

		if(components.length > MAX_STATE_SIZE || components.length == 0)
			throw new InputException("Error: states are either of the form 'state_name' or 'state_name status', one state per line");

		String name, status;
		name = components[0];
		if(components.length > NORMAL_STATE_SIZE)
			status = components[1];
		else
			status = "";

		if(uniqueName(name) && uniqueValidStatus(status))
			states[counter++] = new State(name, status);
	}

	private boolean uniqueName(String name) throws InputException {
		for(State state : states)
			if(state != null && state.getName().equals(name))
				throw new InputException("Error: duplicate states");

		return true;
	}

	/**
	 * Checks if two states have the same status. If both are identical non-default states,
	 * throws an InputException.
	 **/
	private boolean uniqueValidStatus(String status) throws InputException {
		for(State state : states)
			if(state != null && !state.isDefault() && state.getStatus().equals(status))
				throw new InputException("Error: a machine cannot have more than one accept state and one reject state");

		return true;
	}

	/**
	 * Retrieves the input alphabet. Throws an InputException if the alphabet keyword is
	 * missing, the size of the alphabet is missing or not parseable, or if the size
	 * of the alphabet does not correspond to the number of characters provided.
	 **/
	private void retrieveAlphabet(String line) throws InputException {
		String[] components = line.split(" ");			

		if(!components[0].equals("alphabet"))
			throw new InputException("Error: following states given, next line should be of the form\n" + 
					"Error: 'alphabet x a1, a2 ... ax where x is the size of the alphabet and a1, a2 ... ax are the characters");

		int alpha = 0;
		try {
			alpha = Integer.parseInt(components[1]);
		}
		catch(NumberFormatException e) {
			throw new InputException("Error: alphabet must have a defined size");
		}

		if(components.length != alpha + 2)
			throw new InputException("Error: The size of the alphabet does not match the number of arguments entered");

		alphabet = new char[alpha];

		for(int i = 0; i < alpha; i++)
			alphabet[i] = retrieveSymbol(components[i + 2]);
	}

	private char retrieveSymbol(String component) {
		return component.charAt(0);
	}

	/**
	 * Parses a transition line for its 5 components.
	 * Throws an InputException if the number of components is not 5, if there is a
	 * missing/invalid state name, a missing/invalid character, or a missing/invalid
	 * move.
	 **/
	private void retrieveTransition(String line) throws InputException {
		String[] components = line.split(" ");
		if(components.length != TRANSITION_LENGTH)
			throw new InputException("Error: Transitions must be of the form:\n<state1> <tape_input> <state2> <tape_output> <move>");
		State state1 = findStateWithName(components[0]);
		char tape_input = findSymbolMatching(components[1]);
		State state2 = findStateWithName(components[2]);
		char tape_output = findSymbolMatching(components[3]);
		Move move = new Move(components[4]);
		Transition t = new Transition(state1, tape_input, state2, tape_output, move);
		addToTable(t);
	}

	private State findStateWithName(String name) throws InputException {
		for(State state : states)
			if(state.getName().equals(name))
				return state;
		throw new InputException("Error: No state found in set with name " + name);
	}

	/**
	 * Determines whether the symbol provided is in the tape alphabet. Note that this 
	 * includes the empty character ('_') which the input alphabet does not need to 
	 * include.
	 * Throws an InputException if there is no symbol that matches.
	 **/
	private char findSymbolMatching(String symbol) throws InputException {
		char character_symbol = symbol.charAt(0);
		for(char c : alphabet)
			if(c == character_symbol)
				return c;
		char empty = Transition.emptyCharacter();
		if(empty == character_symbol)
			return empty;
		throw new InputException("Error: The input " + symbol + " is not defined in the alphabet");
	}
	
	/**
	 * Checks if the transition provided already exists (since this is a deterministic
	 * TM), and throws an InputException if so.
	 **/
	private void addToTable(Transition t) throws InputException {
		String state_name = t.getInitialState().getName();
		char input = t.getTapeInput();
		for(Transition transition : transition_table)
			if(state_name.equals(transition.getInitialState().getName())
					&& input == transition.getTapeInput())
				throw new InputException("Error: cannot have more than one transition for the same input/state pair, this is a determinisitic turing machine");
				
		transition_table.add(t);
	}

}
