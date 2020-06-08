package com.darsavelidze.renju;

import java.util.List;

public interface GameStatus {
	boolean winnerExists();
	
	List<Cell> getWinnerCells();
}