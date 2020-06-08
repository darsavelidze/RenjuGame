package com.darsavelidze.renju;

public interface WinnerFinder {
	void setPlayingField(PlayingField playinField);

	GameStatus isWinnerFound(CellValue cellValue);
}