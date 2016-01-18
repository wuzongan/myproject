package com.kunlun.poker.exception;

public class GameException extends Exception {

	private static final long serialVersionUID = 3042426956948241030L;

	private int errorCode;
	public GameException(int errorCode)
	{
		super();

		this.errorCode = errorCode;
	}
	
	public GameException(int errorCode, Throwable cause)
	{
		super(cause);

		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}
