package com.darsavelidze.renju;

public interface ComputerTurn {
	void setPlayingField(PlayingField playingField);
	
	Cell makeTurn();
	
	Cell makeFirstTurn();
} 