package com.darsavelidze.renju;

public interface ComputerTurn {
	void setGameTable(GameTable gameTable);
	
	Cell makeTurn();
	
	Cell makeFirstTurn();
} 