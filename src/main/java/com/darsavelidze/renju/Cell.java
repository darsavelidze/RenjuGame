package com.darsavelidze.renju;

public class Cell {
	private final int rowIndex;
	private final int columnIndex;
	
	public Cell (int rowIndex, int columnIndex) {
		super();
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
	
	@Override
	public String toString() {
		return rowIndex + ":" + columnIndex;
	}
}