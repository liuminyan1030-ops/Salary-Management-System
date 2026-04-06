package problemdomain;

public class InvalidEmployeeDataException extends Exception {

	private static final long serialVersionUID = 5663014201006657014L;

	public InvalidEmployeeDataException(String message) {
		super(message);
	}

}
