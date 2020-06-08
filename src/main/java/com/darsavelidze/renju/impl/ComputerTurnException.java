package com.darsavelidze.renju.impl;

public class ComputerTurnException extends IllegalStateException {
	private static final long serialVersionUID = -1405149560593132716L;

	public ComputerTurnException(String message) {
		super(message);
	}
}