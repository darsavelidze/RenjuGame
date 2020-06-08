package com.darsavelidze.renju.impl;

import com.darsavelidze.renju.CellValue;
import com.darsavelidze.renju.PlayingField;

public class DefaultPlayingField implements PlayingField {
	private final CellValue[][] playingField;

	public DefaultPlayingField() {
		playingField = new CellValue[DefaultConstants.SIZE][DefaultConstants.SIZE];
		reInitialize();
	}

	@Override
	public CellValue getValue(int rowIndex, int columnIndex) {
		if (rowIndex >= 0 && rowIndex < getSize() && columnIndex >= 0 && columnIndex < getSize()) {
			return playingField[rowIndex][columnIndex];
		} else {
			throw new IndexOutOfBoundsException("Invalid row or column indexes: rowIndex = " + rowIndex
					+ ", columnIndex = " + columnIndex + ", size = " + getSize());
		}
	}

	@Override
	public void setValue(int rowIndex, int columnIndex, CellValue cellValue) {
		if (rowIndex >= 0 && rowIndex < getSize() && columnIndex >= 0 && columnIndex < getSize()) {
			playingField[rowIndex][columnIndex] = cellValue;
		} else {
			throw new IndexOutOfBoundsException("Invalid row or column indexes: rowIndex = " + rowIndex
					+ ", columnIndex = " + columnIndex + ", size = " + getSize());
		}
	}

	@Override
	public void reInitialize() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				setValue(i, j, CellValue.EMPTY);
			}
		}
	}

	@Override
	public int getSize() {
		return playingField.length;
	}

	@Override
	public boolean isCellFree(int rowIndex, int columnIndex) {
		return getValue(rowIndex, columnIndex) == CellValue.EMPTY;
	}

	@Override
	public boolean isEmptyCellExists() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				if (getValue(i, j) == CellValue.EMPTY) {
					return true;
				}
			}
		}
		return false;
	}

}