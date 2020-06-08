package com.darsavelidze.renju;

public interface PlayingField {
	CellValue getValue(int rowIndex, int columnIndex);

	void setValue(int rowIndex, int columnIndex, CellValue cellValue);
	
	void reInitialize();
	
	int getSize();
	
	boolean isCellFree(int rowIndex, int columnIndex);
	
	boolean isEmptyCellExists();
}