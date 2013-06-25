package no.hild1.bank;

class TelepayParserException extends Exception {
	public TelepayParserException() {
		super();
	}

	public TelepayParserException(String message) {
		super(message);
	}

	public TelepayParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public TelepayParserException(Throwable cause) {
		super(cause);
	}
}