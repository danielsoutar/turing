import java.io.*;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class Test {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@org.junit.Test(expected = FileNotFoundException.class)
	public void testFilePresent() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();
		machine.initialise("");
	}

	@org.junit.Test
	public void testStatesLineHasKeyword() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: first line of machine description should be 'states n', where n is the number of states");

		machine.initialise("test_files/1_states_keyword.txt");
	}

	@org.junit.Test
	public void testStatesLineHasNumber() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: first line of machine description should be 'states n', where n is the number of states");

		machine.initialise("test_files/2_states_no_number.txt");
	}

	@org.junit.Test
	public void testStatesLineNumberIsParseable() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: n (number of states) is not a number");

		machine.initialise("test_files/3_states_number_invalid.txt");
	}


	@org.junit.Test
	public void testDuplicateStateNames() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: duplicate states");

		machine.initialise("test_files/4_states_repeated_name.txt");
	}

	@org.junit.Test
	public void testDuplicateStateStatus() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: a machine cannot have more than one accept state and one reject state");

		machine.initialise("test_files/5_states_repeated_status.txt");
	}

	@org.junit.Test
	public void testInvalidStateStatus() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: status provided is invalid");

		machine.initialise("test_files/6_states_invalid_status.txt");
	}
	
	@org.junit.Test
	public void testOnlyStateNameOrNameAndStatus() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: states are either of the form 'state_name' or 'state_name status', one state per line");

		machine.initialise("test_files/7_states_one_per_line.txt");
	}

	@org.junit.Test
	public void testAlphabetLineHasKeyword() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: following states given, next line should be of the form");
		thrown.expectMessage("Error: 'alphabet x a1, a2 ... ax where x is the size of the alphabet and a1, a2 ... ax are the characters");

		machine.initialise("test_files/8_alphabet_keyword.txt");
	}

	@org.junit.Test
	public void testAlphabetLineHasParseableSize() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: alphabet must have a defined size");
		machine.initialise("test_files/9_alphabet_no_size.txt");
	}

	@org.junit.Test
	public void testAlphabetLineHasCorrectSize() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: The size of the alphabet does not match the number of arguments entered");
		machine.initialise("test_files/10_alphabet_wrong_size.txt");
	}
	
	@org.junit.Test
	public void testTooLongTransition() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: Transitions must be of the form:\n<state1> <tape_input> <state2> <tape_output> <move>");
		machine.initialise("test_files/11_transition_size_big.txt");
	}
	
	@org.junit.Test
	public void testTooShortTransition() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: Transitions must be of the form:\n<state1> <tape_input> <state2> <tape_output> <move>");
		machine.initialise("test_files/12_transition_size_short.txt");
	}
	
	@org.junit.Test
	public void testTransitionWithInvalidStateName() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: No state found in set with name q*");
		machine.initialise("test_files/13_transition_state_name");
	}
	
	@org.junit.Test
	public void testTransitionWithInvalidInput() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: The input * is not defined in the alphabet");
		machine.initialise("test_files/14_transition_input.txt");
	}
	
	@org.junit.Test
	public void testTransitionWithInvalidMove() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: a turing machine can only move left (L) or right (R)");
		machine.initialise("test_files/15_transition_move.txt");
	}
	
	@org.junit.Test
	public void testDuplicateTransitions() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();

		thrown.expect(InputException.class);
		thrown.expectMessage("Error: cannot have more than one transition for the same input/state pair, this is a determinisitic turing machine");
		machine.initialise("test_files/16_transition_duplicate.txt");
	}
	
}
