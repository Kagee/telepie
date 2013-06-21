package no.hild1.bank;

class TelepayException extends Exception {
	public TelepayException() {
		super();
	}

	public TelepayException(String message) {
		super(message);
	}

	public TelepayException(String message, Throwable cause) {
		super(message, cause);
	}

	public TelepayException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -4200483982924961856L;

}