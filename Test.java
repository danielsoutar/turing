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
		thrown.expectMessage("Error: first line of machine description should be 'states x', where x is the number of states");
		
		machine.initialise("test_files/state1.txt");
	}
	
	@org.junit.Test
	public void testStatesLineNumberIsParseable() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();
		
		thrown.expect(InputException.class);
		thrown.expectMessage("Error: n (number of states) is not a number");
		
		machine.initialise("test_files/state2.txt");
	}
	
	@org.junit.Test
	public void testDuplicateStateNames() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();
		
		thrown.expect(InputException.class);
		thrown.expectMessage("Error: duplicate states");
		
		machine.initialise("test_files/state3.txt");
	}

	@org.junit.Test
	public void testDuplicateStateStatus() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();
		
		thrown.expect(InputException.class);
		thrown.expectMessage("Error: a machine cannot have more than one accept state and one reject state");
		
		machine.initialise("test_files/state4.txt");
	}
	
	@org.junit.Test
	public void testInvalidStateStatus() throws FileNotFoundException, InputException {
		TuringMachine machine = new TuringMachine();
		
		thrown.expect(InputException.class);
		thrown.expectMessage("Error: status provided is invalid");
		
		machine.initialise("test_files/state5.txt");
	}
	
}
