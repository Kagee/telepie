package no.hild1.bank;

public class TelepayParserException extends Exception {
	public TelepayParserException() {
		super();
	}

	public TelepayParserException(String message) {
		super(message);
	}
	public TelepayParserException(int record, String message) {
		super("Record " + record + ": " + message);
	}
	public TelepayParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public TelepayParserException(Throwable cause) {
		super(cause);
	}
}