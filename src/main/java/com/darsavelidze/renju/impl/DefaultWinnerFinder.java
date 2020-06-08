package com.darsavelidze.renju.impl;

import static com.darsavelidze.renju.impl.DefaultConstants.WIN_COUNT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.darsavelidze.renju.Cell;
import com.darsavelidze.renju.CellValue;
import com.darsavelidze.renju.GameStatus;
import com.darsavelidze.renju.PlayingField;
import com.darsavelidze.renju.WinnerFinder;

public class DefaultWinnerFinder implements WinnerFinder {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWinnerFinder.class);
	private PlayingField playingField;

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
	public GameStatus isWinnerFound(CellValue cellValue) {
		Objects.requireNonNull(cellValue, "Cell value can't be null");
		LOGGER.trace("Try to find winner in rows: is {} winner?", cellValue);
		List<Cell> resultCells;
		resultCells = findWinnerInRows(cellValue);
		if (resultCells != null) {
			LOGGER.debug("Winner is {}. By row {}", cellValue, resultCells);
			return new DefaultGameStatus(resultCells);
		}
		LOGGER.trace("Try to find winner in columns: is {} winner?", cellValue, resultCells);
		resultCells = findWinnerInColumns(cellValue);
		if (resultCells != null) {
			LOGGER.debug("Winner is {}. By column {}", cellValue, resultCells);
			return new DefaultGameStatus(resultCells);
		}
		LOGGER.trace("Try to find winner in main diagonals: is {} winner?", cellValue, resultCells);
		resultCells = findWinnerInMainDiagonals(cellValue);
		if (resultCells != null) {
			LOGGER.debug("Winner is {}. By main diagonal {}", cellValue, resultCells);
			return new DefaultGameStatus(resultCells);
		}
		LOGGER.trace("Try to find winner in not main diagonals: is {} winner?", cellValue, resultCells);
		resultCells = findWinnerInNotMainDiagonals(cellValue);
		if (resultCells != null) {
			LOGGER.debug("Winner is {}. By not main diagonal {}", cellValue, resultCells);
			return new DefaultGameStatus(resultCells);
		}
		LOGGER.trace("Winner not found");
		return new DefaultGameStatus(null);
	}

	protected List<Cell> findWinnerInRows(CellValue cellValue) {
		for (int i = 0; i < playingField.getSize(); i++) {
			List<Cell> checkedCells = new ArrayList<Cell>(WIN_COUNT);
			for (int j = 0; j < playingField.getSize(); j++) {
				if (playingField.getValue(i, j) == cellValue) {
					checkedCells.add(new Cell(i, j));
					if (checkedCells.size() == WIN_COUNT) {
						return checkedCells;
					}
				} else {
					checkedCells.clear();
					if (j > playingField.getSize() - WIN_COUNT) {
						break;
					}
				}
			}
		}
		return null;
	}

	protected List<Cell> findWinnerInColumns(CellValue cellValue) {
		for (int i = 0; i < playingField.getSize(); i++) {
			List<Cell> checkedCells = new ArrayList<Cell>(WIN_COUNT);
			for (int j = 0; j < playingField.getSize(); j++) {
				if (playingField.getValue(j, i) == cellValue) {
					checkedCells.add(new Cell(j, i));
					if (checkedCells.size() == WIN_COUNT) {
						return checkedCells;
					}
				} else {
					checkedCells.clear();
					if (j > playingField.getSize() - WIN_COUNT) {
						break;
					}
				}
			}
		}
		return null;
	}

	protected List<Cell> findWinnerInMainDiagonals(CellValue cellValue) {
		for (int i = 0; i <= playingField.getSize() - WIN_COUNT; i++) {
			for (int j = 0; j <= playingField.getSize() - WIN_COUNT; j++) {
				List<Cell> checkedCells = new ArrayList<Cell>(WIN_COUNT);
				for (int k = 0; k < WIN_COUNT; k++) {
					if (playingField.getValue(i + k, j + k) == cellValue) {
						checkedCells.add(new Cell(i + k, j + k));
						if (checkedCells.size() == WIN_COUNT) {
							return checkedCells;
						}
					} else {
						break;
					}
				}
			}
		}
		return null;
	}

	protected List<Cell> findWinnerInNotMainDiagonals(CellValue cellValue) {
		for (int i = 0; i <= playingField.getSize() - WIN_COUNT; i++) {
			for (int j = WIN_COUNT - 1; j < playingField.getSize(); j++) {
				List<Cell> checkedCells = new ArrayList<Cell>(WIN_COUNT);
				for (int k = 0; k < WIN_COUNT; k++) {
					if (playingField.getValue(i + k, j - k) == cellValue) {
						checkedCells.add(new Cell(i + k, j - k));
						if (checkedCells.size() == WIN_COUNT) {
							return checkedCells;
						}
					} else {
						break;
					}
				}
			}
		}
		return null;
	}

	private static class DefaultGameStatus implements GameStatus {
		private final List<Cell> winnerCells;

		DefaultGameStatus(List<Cell> winnerCells) {
			if (winnerCells != null) {
				this.winnerCells = Collections.unmodifiableList(winnerCells);
			} else {
				this.winnerCells = Collections.emptyList();
			}
		}

		@Override
		public boolean winnerExists() {
			return winnerCells.size() > 0;
		}

		@Override
		public List<Cell> getWinnerCells() {
			return winnerCells;
		}

	}
}
