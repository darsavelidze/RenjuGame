package com.darsavelidze.renju;

public interface HumanTurn {
	void setPlayingField(PlayingField playingField);

	Cell makeTurn(int rowIndex, int columnIndex);
}