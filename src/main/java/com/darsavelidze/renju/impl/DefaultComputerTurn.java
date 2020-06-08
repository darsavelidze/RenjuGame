package com.darsavelidze.renju.impl;

import static com.darsavelidze.renju.impl.DefaultConstants.WIN_COUNT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.darsavelidze.renju.Cell;
import com.darsavelidze.renju.CellValue;
import com.darsavelidze.renju.ComputerTurn;
import com.darsavelidze.renju.PlayingField;

public class DefaultComputerTurn implements ComputerTurn {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultComputerTurn.class);
	private PlayingField playingField;
	private Random random = new Random();

	@Override
	public void setPlayingField(PlayingField playingField) {
		Objects.requireNonNull(playingField, "Playing field can't be null");
		if (playingField.getSize() < WIN_COUNT) {
			throw new IllegalArgumentException("Size of playing field is small: size = " + playingField.getSize()
					+ ". Minimum requirement: " + WIN_COUNT);
		}
		this.playingField = playingField;
	}

	@Override
	public Cell makeTurn() {
		CellValue[] figures = { CellValue.COMPUTER, CellValue.HUMAN };
		for (int i = WIN_COUNT - 1; i > 0; i--) {
			for (CellValue cellValue : figures) {
				Cell cell = tryMakeTurn(cellValue, i);
				if (cell != null) {
					LOGGER.info("Computer turn is {}", cell);
					return cell;
				}
			}
		}
		return makeRandomTurn();
	}

	@Override
	public Cell makeFirstTurn() {
		Cell cell = new Cell(playingField.getSize() / 2, playingField.getSize() / 2);
		playingField.setValue(cell.getRowIndex(), cell.getColumnIndex(), CellValue.COMPUTER);
		LOGGER.info("Computer first turn: {}", cell);
		return cell;
	}

	protected Cell makeRandomTurn() {
		List<Cell> emptyCells = getAllEmptyCells();
		if (emptyCells.size() > 0) {
			Cell randomCell = emptyCells.get(random.nextInt(emptyCells.size()));
			playingField.setValue(randomCell.getRowIndex(), randomCell.getColumnIndex(), CellValue.COMPUTER);
			LOGGER.info("Computer random turn: {}", randomCell);
			return randomCell;
		} else {
			throw new ComputerTurnException("All cells are filled! Please check draw state");
		}
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

	protected Cell tryMakeTurn(CellValue cellValue, int notBlankCount) {
		LOGGER.trace("AI trying to make turn by row pattern: {} empty and {} not empty cells for {}",
				WIN_COUNT - notBlankCount, notBlankCount, cellValue);
		Cell cell = tryMakeTurnInRows(cellValue, notBlankCount);
		if (cell != null) {
			return cell;
		}
		LOGGER.trace("AI trying to make turn by column pattern: {} empty and {} not empty cells for {}",
				WIN_COUNT - notBlankCount, notBlankCount, cellValue);
		cell = tryMakeTurnInColumns(cellValue, notBlankCount);
		if (cell != null) {
			return cell;
		}
		LOGGER.trace("AI trying to make turn by main diagonal pattern: {} empty and {} not empty cells for {}",
				WIN_COUNT - notBlankCount, notBlankCount, cellValue);
		cell = tryMakeTurnInMainDiagonals(cellValue, notBlankCount);
		if (cell != null) {
			return cell;
		}
		LOGGER.trace("AI trying to make turn by not main diagonal pattern: {} empty and {} not empty cells for {}",
				WIN_COUNT - notBlankCount, notBlankCount, cellValue);
		cell = tryMakeTurnInNotMainDiagonals(cellValue, notBlankCount);
		if (cell != null) {
			return cell;
		}
		return null;
	}

	protected Cell tryMakeTurnInRows(CellValue cellValue, int notBlankCount) {
		for (int i = 0; i < playingField.getSize(); i++) {
			for (int j = 0; j <= playingField.getSize() - WIN_COUNT; j++) {
				boolean hasEmptyCells = false;
				int count = 0;
				List<Cell> inspectedCells = new ArrayList<>(WIN_COUNT);
				for (int k = 0; k < WIN_COUNT; k++) {
					inspectedCells.add(new Cell(i, j + k));
					if (playingField.getValue(i, j + k) == cellValue) {
						count++;
					} else if (playingField.getValue(i, j + k) == CellValue.EMPTY) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (count == notBlankCount && hasEmptyCells) {
					LOGGER.debug("Found {} empty and {} not empty cells by row: {} {}", WIN_COUNT - count, count,
							inspectedCells, new LoggerPattern(inspectedCells));
					return makeTurnToOneCellFromDataSet(inspectedCells);
				}
			}
		}
		return null;
	}

	protected Cell tryMakeTurnInColumns(CellValue cellValue, int notBlankCount) {
		for (int i = 0; i < playingField.getSize(); i++) {
			for (int j = 0; j <= playingField.getSize() - WIN_COUNT; j++) {
				boolean hasEmptyCells = false;
				int count = 0;
				List<Cell> inspectedCells = new ArrayList<>(WIN_COUNT);
				for (int k = 0; k < WIN_COUNT; k++) {
					inspectedCells.add(new Cell(j + k, i));
					if (playingField.getValue(j + k, i) == cellValue) {
						count++;
					} else if (playingField.getValue(j + k, i) == CellValue.EMPTY) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (count == notBlankCount && hasEmptyCells) {
					LOGGER.debug("Found {} empty and {} not empty cells by col: {} {}", WIN_COUNT - count, count,
							inspectedCells, new LoggerPattern(inspectedCells));
					return makeTurnToOneCellFromDataSet(inspectedCells);
				}
			}
		}
		return null;
	}

	protected Cell tryMakeTurnInMainDiagonals(CellValue cellValue, int notBlankCount) {
		for (int i = 0; i <= playingField.getSize() - WIN_COUNT; i++) {
			for (int j = 0; j <= playingField.getSize() - WIN_COUNT; j++) {
				boolean hasEmptyCells = false;
				int count = 0;
				List<Cell> inspectedCells = new ArrayList<Cell>(WIN_COUNT);
				for (int k = 0; k < WIN_COUNT; k++) {
					inspectedCells.add(new Cell(i + k, j + k));
					if (playingField.getValue(i + k, j + k) == cellValue) {
						count++;
					} else if (playingField.getValue(i + k, j + k) == CellValue.EMPTY) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (count == notBlankCount && hasEmptyCells) {
					LOGGER.debug("Found {} empty and {} not empty cells by main diagonal: {} {}", WIN_COUNT - count,
							count, inspectedCells, new LoggerPattern(inspectedCells));
					return makeTurnToOneCellFromDataSet(inspectedCells);
				}
			}
		}
		return null;
	}

	protected Cell tryMakeTurnInNotMainDiagonals(CellValue cellValue, int notBlankCount) {
		for (int i = 0; i <= playingField.getSize() - WIN_COUNT; i++) {
			for (int j = WIN_COUNT - 1; j < playingField.getSize(); j++) {
				boolean hasEmptyCells = false;
				int count = 0;
				List<Cell> inspectedCells = new ArrayList<>(WIN_COUNT);
				for (int k = 0; k < WIN_COUNT; k++) {
					inspectedCells.add(new Cell(i + k, j - k));
					if (playingField.getValue(i + k, j - k) == cellValue) {
						count++;
					} else if (playingField.getValue(i + k, j - k) == CellValue.EMPTY) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (count == notBlankCount && hasEmptyCells) {
					LOGGER.debug("Found {} empty and {} not empty cells by not main diagonal: {} {}", WIN_COUNT - count,
							count, inspectedCells, new LoggerPattern(inspectedCells));
					return makeTurnToOneCellFromDataSet(inspectedCells);
				}

			}
		}
		return null;
	}

	protected Cell makeTurnToOneCellFromDataSet(List<Cell> inspectedCells) {
		Cell cell = findEmptyCellForComputerTurn(inspectedCells);
		playingField.setValue(cell.getRowIndex(), cell.getColumnIndex(), CellValue.COMPUTER);
		LOGGER.trace("The best cell is {} for pattern {} {}", cell, inspectedCells, new LoggerPattern(inspectedCells));
		return cell;
	}

	protected Cell findEmptyCellForComputerTurn(List<Cell> cells) {
		LOGGER.trace("Try to find the best turn by pattern {} {}", cells, new LoggerPattern(cells));
		for (int i = 0; i < cells.size(); i++) {
			Cell currentCell = cells.get(i);
			if (!isCellEmpty(currentCell)) {
				if (i == 0) {
					if (isCellEmpty(cells.get(i + 1))) {
						return cells.get(i + 1);
					}
				} else if (i == cells.size() - 1) {
					if (isCellEmpty(cells.get(i - 1))) {
						return cells.get(i - 1);
					}
				} else {
					boolean searchDirectionAsc = random.nextBoolean();
					int first = searchDirectionAsc ? i + 1 : i - 1;
					int second = searchDirectionAsc ? i - 1 : i + 1;
					if (isCellEmpty(cells.get(first))) {
						return cells.get(first);
					} else if (isCellEmpty(cells.get(second))) {
						return cells.get(second);
					}
				}
			}
		}
		throw new ComputerTurnException("All cells are filled: " + cells);
	}

	protected boolean isCellEmpty(Cell cell) {
		return playingField.getValue(cell.getRowIndex(), cell.getColumnIndex()) == CellValue.EMPTY;
	}

	private class LoggerPattern {
		private final List<Cell> cells;

		LoggerPattern(List<Cell> cells) {
			super();
			this.cells = cells;
		}

		@Override
		public String toString() {
			StringBuilder pattern = new StringBuilder("[");
			for (Cell cell : cells) {
				CellValue cellValue = playingField.getValue(cell.getRowIndex(), cell.getColumnIndex());
				pattern.append(cellValue == CellValue.EMPTY ? "*" : cellValue.getValue());
			}
			pattern.append("]");
			return pattern.toString();
		}
	}

}