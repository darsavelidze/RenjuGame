package com.darsavelidze.renju;

public interface HumanTurn {
	void setGameTable(GameTable gameTable);

	Cell makeTurn(int rowIndex, int columnIndex);
}