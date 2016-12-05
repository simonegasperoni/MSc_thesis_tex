package com.sciamlab.it.acquis.xquery;

public class FileWithoutEurovocTagException extends Exception {
	private static final long serialVersionUID = 1618628287177979566L;
	public FileWithoutEurovocTagException() {}
	public FileWithoutEurovocTagException(String message){
		super(message);
	}
}
