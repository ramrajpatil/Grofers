package com.grofers.exceptions;

public class DuplicateEntryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6094188738004802105L;

	public DuplicateEntryException(String message) {
		super(message);
	}
}
