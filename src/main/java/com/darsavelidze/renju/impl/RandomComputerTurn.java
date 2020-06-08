package com.darsavelidze.renju.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.darsavelidze.renju.Cell;
import com.darsavelidze.renju.CellValue;
import com.darsavelidze.renju.ComputerTurn;
import com.darsavelidze.renju.PlayingField;

public class RandomComputerTurn implements ComputerTurn {
	private PlayingField playingField;

	@Override
	public void setPlayingField(PlayingField playingField) {
		Objects.requireNonNull(playingField, "Playing field can't be null");
		this.playingField = playingField;
	}

	@Override
	public Cell makeTurn() {
		List<Cell> emptyCells = getAllEmptyCells();
		if (emptyCells.size() > 0) {
			Cell randomCell = emptyCells.get(new Random().nextInt(emptyCells.size()));
			playingField.setValue(randomCell.getRowIndex(), randomCell.getColumnIndex(), CellValue.COMPUTER);
			return randomCell;
		} else {
			throw new ComputerTurnException("All cells are filled! Please check draw state");
		}
	}

	@Override
	public Cell makeFirstTurn() {
		return makeTurn();
	}

	protected List<Cell> getAllEmptyCells() {
		List<Cell> emptyCells = new ArrayList<>();
		for (int i = 0; i < playingField.getSize(); i++) {
			for (int j = 0; j < playingField.getSize(); j++) {
				if (playingField.isCellFree(i, j)) {
					emptyCells.add(new Cell(i, j));
				}
			}
		}
		return emptyCells;
	}

}
