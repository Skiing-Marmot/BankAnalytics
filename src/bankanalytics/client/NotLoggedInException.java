package bankanalytics.client;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public NotLoggedInException() {
		super();
	}

	public NotLoggedInException(String message) {
		super(message);
	}

}