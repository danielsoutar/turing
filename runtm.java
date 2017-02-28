import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

		String[] inputs = getInputFrom(args[1]);

		getAllFlags(args);		

		clearScreen();

		run(machine, inputs);
	}

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
		System.out.println("########################");
	}

	private static void run(TuringMachine machine, String[] inputs) {
		try {
			for(String input : inputs) {
				if(machine.canAccept(input.toCharArray(), in_interactive_mode, in_performance_mode))
					System.out.println("input accepted");
				else
					System.out.println("input rejected");
				System.out.println("########################");
			}
		}
		catch(InputException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private static void printMachineDescription(TuringMachine machine) {
		for(State s : machine.getStates())
			System.out.println("State: " + s.getName());

		for(char c : machine.getAlphabet())
			System.out.println("Alphabet symboL:"  + c);

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

	private static String[] getInputFrom(String input_file) {
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
		String[] input_array = new String[input_array_list.size()];
		input_array = input_array_list.toArray(input_array);
		return input_array;
	}

}