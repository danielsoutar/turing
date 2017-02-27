
public class State {
	
	private boolean IS_DEFAULT = false;
	private boolean IS_ACCEPT = false;

	private String name;
	private String status;

	public State(String name, String status) throws InputException {
		this.name = name;
		if(status.equals(""))
			IS_DEFAULT = true;
		else if(status.equals("+"))
			IS_ACCEPT = true;
		else if(status.equals("-"))
			IS_ACCEPT = false;
		else
			throw new InputException("Error: status provided is invalid");

		this.status = status;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public boolean isDefault() {
		return IS_DEFAULT;
	}

	public boolean isAcceptState() {
		return IS_ACCEPT;
	}
}