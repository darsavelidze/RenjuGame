package com.darsavelidze.renju.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.darsavelidze.renju.Cell;
import com.darsavelidze.renju.CellValue;
import com.darsavelidze.renju.HumanTurn;
import com.darsavelidze.renju.PlayingField;

public class DefaultHumanTurn implements HumanTurn{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHumanTurn.class);
	private PlayingField playingField;

	@Override
	public void setPlayingField(PlayingField playingField) {
		Objects.requireNonNull(playingField, "Playing field can't be null");
		this.playingField = playingField;
	}

	@Override
	public Cell makeTurn(int rowIndex, int columnIndex) {
		playingField.setValue(rowIndex, columnIndex, CellValue.HUMAN);
		Cell cell = new Cell(rowIndex, columnIndex);
		LOGGER.info("Human turn is {}", cell);
		return cell;
	}	
}