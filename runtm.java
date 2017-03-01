import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class runtm {

	public static final int MIN_NUM_ARGS = 2;
	public static final int ARGS_WITH_FLAG = 3;
	public static final int ARGS_ALL_FLAGS = 4;

	private static boolean in_performance_mode = false;
	private static final String PERFORMANCE_FLAG = "-P";
	private static boolean in_interactive_mode = false;
	private static final String INTERACTIVE_FLAG = "-I";
	public static final int SCREEN_SIZE = 77;


	/**
	 * The machine is initialised, the inputs are stored in an array, 
	 * and the screen is cleared.
	 **/
	public static void main(String[] args) {
		check(args.length);

		TuringMachine machine = new TuringMachine();

		try {
			machine.initialise(args[0]);
		} 
		catch (FileNotFoundException | InputException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		//printMachineDescription(machine);

		ArrayList<String> inputs = getInputFrom(args[1]);

		getAllFlags(args);		

		clearScreen();

		run(machine, inputs);
	}

	/**
	 * All flags present are stored internally. Terminates the program
	 * if an invalid flag is entered.
	 **/
	private static void getAllFlags(String[] args) {
		if(args.length == MIN_NUM_ARGS)
			return;

		for (int i = 2; i < args.length; i++) {
			String flag = args[i];
			if(flag.equals(PERFORMANCE_FLAG))
				in_performance_mode = true;
			else if(flag.equals(INTERACTIVE_FLAG))
				in_interactive_mode = true;
			else {
				System.out.println("Error: only valid flags are -I and -P.");
				System.exit(1);
			}
		}
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	/**
	 * Iterates through the inputs provided and compares each of them to the machine
	 * that is now represented internally. A message to the console is provided with
	 * the result.
	 **/
	private static void run(TuringMachine machine, ArrayList<String> inputs) {
		try {
			for(String in : inputs) {
				ArrayList<Character> input = new ArrayList<Character>();
				for (char c : in.toCharArray())
				  input.add(c);
				if(machine.canAccept(input, in_interactive_mode, in_performance_mode))
					System.out.println("input accepted");
				else
					System.out.println("input rejected");
			}
		}
		catch(InputException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("########################");
	}

	/**
	 * This is a method to display the machine as represented internally.
	 * It's somewhat lengthy, hence it is not used, although should you wish
	 * it would be easy enough to call it.
	 **/
	@SuppressWarnings("unused")
	private static void printMachineDescription(TuringMachine machine) {
		for(State s : machine.getStates())
			System.out.println("State: " + s.getName());

		for(char c : machine.getAlphabet())
			System.out.println("Alphabet symbol:"  + c);

		for(Transition t : machine.getTransitionTable())
			System.out.println(t.print());
	}

	public static void check(int args_length) {
		if(args_length < MIN_NUM_ARGS || args_length > ARGS_ALL_FLAGS) {
			System.out.println("Usage: java runtm <Turing Machine Description File> <Input File> <Optional flag>");
			System.out.println("Example: java runtm Turing.txt input.txt");
			System.exit(1);
		}
	}

	/**
	 * The input file is scanned and the lines are iteratively added to the
	 * data structure responsible for storing them - I start with an arraylist for
	 * its flexibility, and then convert it to an array for simplicity and speed.
	 **/
	private static ArrayList<String> getInputFrom(String input_file) {
		Scanner scanner = null;
		ArrayList<String> input_array_list = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(input_file));
			while(scanner.hasNextLine())
				input_array_list.add(scanner.nextLine());
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: No input file was provided.");
			System.exit(1);
		}
		return input_array_list;
	}

}