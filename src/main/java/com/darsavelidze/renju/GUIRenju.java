package com.darsavelidze.renju;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.darsavelidze.renju.impl.DefaultComputerTurn;
import com.darsavelidze.renju.impl.DefaultHumanTurn;
import com.darsavelidze.renju.impl.DefaultPlayingField;
import com.darsavelidze.renju.impl.DefaultWinnerFinder;

public class GUIRenju extends JFrame {
	private static final long serialVersionUID = -5164429115387109570L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GUIRenju.class);
	private final JLabel cells[][];
	private final transient PlayingField playingField;
	private final transient HumanTurn humanTurn;
	private final transient ComputerTurn computerTurn;
	private final transient WinnerFinder winnerFinder;
	private boolean humanMakeFirstTurn;

	public GUIRenju() throws HeadlessException {
		super("Renju");
		// start configuration section ------------------------ <
		playingField = new DefaultPlayingField();
		humanTurn = new DefaultHumanTurn();
		computerTurn = new DefaultComputerTurn();
		winnerFinder = new DefaultWinnerFinder();
		// end configuration sections ------------------------- <
		initializeGameComponents();
		humanMakeFirstTurn = true;
		cells = new JLabel[playingField.getSize()][playingField.getSize()];
		createPlayingUIField();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOGGER.info("Game stopped with playing field {}x{}", playingField.getSize(), playingField.getSize());
				System.exit(0);
			}
		});
	}

	protected void initializeGameComponents() {
		humanTurn.setPlayingField(playingField);
		computerTurn.setPlayingField(playingField);
		winnerFinder.setPlayingField(playingField);
	}

	protected void createPlayingUIField() {
		setLayout(new GridLayout(playingField.getSize(), playingField.getSize()));
		for (int i = 0; i < playingField.getSize(); i++) {
			for (int j = 0; j < playingField.getSize(); j++) {
				final int row = i;
				final int column = j;
				cells[i][j] = new JLabel();
				cells[i][j].setPreferredSize(new Dimension(40, 40));
				cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				cells[i][j].setVerticalAlignment(SwingConstants.CENTER);
				cells[i][j].setFont(new Font(Font.SERIF, Font.PLAIN, 35));
				cells[i][j].setForeground(Color.BLACK);
				cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				add(cells[i][j]);
				cells[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						handleHumanTurn(row, column);
					}
				});
			}
		}
	}

	protected void handleHumanTurn(int rowIndex, int columnIndex) {
		try {
			if (playingField.isCellFree(rowIndex, columnIndex)) {
				Cell humanCell = humanTurn.makeTurn(rowIndex, columnIndex);
				drawCurrentCellValue(humanCell);
				GameStatus gameStatus = winnerFinder.isWinnerFound(CellValue.HUMAN);
				if (gameStatus.winnerExists()) {
					fillWinnerCells(gameStatus.getWinnerCells());
					LOGGER.info("Human win: {}", gameStatus.getWinnerCells());
					gameOverHandler("Game over: You win!\nNew game?");
					return;
				}
				if (!playingField.isEmptyCellExists()) {
					LOGGER.info("Nobody wins - draw");
					gameOverHandler("Game over: Draw!\nNewGame?");
					return;
				}
				Cell computerCell = computerTurn.makeTurn();
				drawCurrentCellValue(computerCell);
				gameStatus = winnerFinder.isWinnerFound(CellValue.COMPUTER);
				if (gameStatus.winnerExists()) {
					fillWinnerCells(gameStatus.getWinnerCells());
					LOGGER.info("Computer wins: {}", gameStatus.getWinnerCells());
					gameOverHandler("Game over: Computer wins!\nNew game?");
					return;
				}
				if (!playingField.isEmptyCellExists()) {
					LOGGER.info("Nobody wins - draw");
					gameOverHandler("Game over: Draw!\nNew game?");
					return;
				}
			} else {
				LOGGER.warn("Cell {}:{} is not empty", rowIndex, columnIndex);
				JOptionPane.showMessageDialog(this, "Cell is not empty! Click on empty cell!");
			}
		} catch (RuntimeException e) {
			LOGGER.error("Error in the game: " + e.getMessage(), e);
		}
	}

	protected void drawCurrentCellValue(Cell currentCell) {
		CellValue currentCellValue = playingField.getValue(currentCell.getRowIndex(), currentCell.getColumnIndex());
		cells[currentCell.getRowIndex()][currentCell.getColumnIndex()].setText(currentCellValue.getValue());
		if (currentCellValue == CellValue.COMPUTER) {
			cells[currentCell.getRowIndex()][currentCell.getColumnIndex()].setForeground(Color.RED);
		} else {
			cells[currentCell.getRowIndex()][currentCell.getColumnIndex()].setForeground(Color.BLUE);
		}
	}

	protected void fillWinnerCells(List<Cell> winnerCells) {
		for (int i = 0; i < winnerCells.size(); i++) {
			Cell cell = winnerCells.get(i);
			cells[cell.getRowIndex()][cell.getColumnIndex()].setForeground(Color.CYAN);
			cells[cell.getRowIndex()][cell.getColumnIndex()].setFont(new Font(Font.SERIF, Font.BOLD, 35));
		}
	}

	protected void gameOverHandler(String message) {
		if (JOptionPane.showConfirmDialog(this, message) == JOptionPane.YES_OPTION) {
			startNewGame();
		} else {
			stopGame();
		}
	}

	protected void startNewGame() {
		humanMakeFirstTurn = !humanMakeFirstTurn;
		playingField.reInitialize();
		for (int i = 0; i < playingField.getSize(); i++) {
			for (int j = 0; j < playingField.getSize(); j++) {
				cells[i][j].setText(playingField.getValue(i, j).getValue());
				cells[i][j].setFont(new Font(Font.SERIF, Font.PLAIN, 35));
				cells[i][j].setForeground(Color.BLACK);
			}
		}
		if (!humanMakeFirstTurn) {
			Cell computerCell = computerTurn.makeFirstTurn();
			drawCurrentCellValue(computerCell);
		}
		LOGGER.info("New game started with playing field {}x{} {}", playingField.getSize(), playingField.getSize(),
				humanMakeFirstTurn ? "" : CellValue.COMPUTER + " made the first turn");
	}

	protected void stopGame() {
		for (int i = 0; i < playingField.getSize(); i++) {
			for (int j = 0; j < playingField.getSize(); j++) {
				cells[i][j].removeMouseListener(cells[i][j].getMouseListeners()[0]);
			}
		}
		LOGGER.info("Game disabled with playing field {}x{}", playingField.getSize(), playingField.getSize());
	}

	public static void main(String[] args) {
		GUIRenju renju = new GUIRenju();
		renju.setResizable(false);
		renju.pack();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		renju.setLocation(dimension.width / 2 - renju.getSize().width / 2,
				dimension.height / 2 - renju.getSize().height / 2);
		renju.setVisible(true);
		LOGGER.info("New game started with playing field {}x{}", renju.playingField.getSize(),
				renju.playingField.getSize());
	}
}
