import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TuringMachine {

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

	public boolean canAccept(char[] input, boolean i_mode, boolean p_mode) throws InputException {
		position = 0;
		current_state = start_state;
		current_input = input[position];
		return accept(input, position, current_state, current_input, i_mode, p_mode);
	}

	private boolean accept(char[] input, int position, State current_state, char current_input, boolean i_mode, boolean p_mode) throws InputException {
		char[] original_input = input.clone();
		int number_of_steps = 0;
		while(current_state.isDefault()) {
			Transition t = getTransitionContaining(current_state, current_input);
			current_state = t.getResultState();
			if(!current_state.isDefault())
				break;
			input[position] = t.getTapeOutput();

			position = shift(position, t.getMoveDirection());

			if(input.length < position + 1)
				throw new InputException("Error: Malformed transition table or input. "
						+ "Machine attempting to read outside input in non-rejecting state.");
			current_input = input[position];

			if(i_mode) 
				printTransition(input, t);
			number_of_steps++;
		}

		if(p_mode)
			printSteps(original_input, number_of_steps);

		return current_state.isAcceptState();
	}

	private void printSteps(char[] original_input, int number_of_steps) {
		String output = String.copyValueOf(original_input);
		System.out.println("########################");
		System.out.println("Number of steps on input "+ output + ": ");
		System.out.println(number_of_steps);
	}

	private synchronized void printTransition(char[] input, Transition t) {
		boolean needSpeed = false;
		for(int i = 0; i < input.length && i < runtm.SCREEN_SIZE; i++)
			System.out.print(input[i]);

		if(input.length >= runtm.SCREEN_SIZE) {
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
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	private int shift(int position, String moveDirection) {
		if(moveDirection.equals(Transition.LEFT))
			position--;
		else if(moveDirection.equals(Transition.RIGHT))
			position++;
		return position;
	}

	private Transition getTransitionContaining(State current_state, char current_input) throws InputException {
		for(Transition t : transition_table)
			if(t.getInitialState().equals(current_state) && t.getTapeInput() == current_input)
				return t;
		throw new InputException("Error: there is no transition possible given the state and input.");
	}

	//n must be greater than 0, and less than java.MAX_INTEGER.
	private void retrieveN(String line) throws InputException {
		String[] components = line.split(" ");
		if(components.length != N_LINE_SIZE)
			throw new InputException("Error: first line of machine description should be 'states n', where n is the number of states");

		n = parse(components[1]);
		if(n < 0 || n > Integer.MAX_VALUE)
			throw new InputException("Error: number of states too large for java int data type.");

	}

	private int parse(String input) throws InputException {
		try {
			n = Integer.parseInt(input);
			return n;
		}
		catch (NumberFormatException e) {
			throw new InputException("Error: n (number of states) is not a number");
		}
	}

	//Each state must have a unique name, there must be n states, there must be exactly one accept state, and exactly
	//one reject state. There must be (n - 2) default states.
	private void retrieveState(String line, int counter) throws InputException {
		String[] components = line.split(" ");

		if(components.length > MAX_STATE_SIZE)
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

	//Checking if it is unique in the case of being an accept or reject state.
	private boolean uniqueValidStatus(String status) throws InputException {
		for(State state : states)
			if(state != null && state.getStatus().equals(status) && !state.isDefault())
				throw new InputException("Error: a machine cannot have more than one accept state and one reject state");

		return true;
	}

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
