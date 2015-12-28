import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class contains the Sudoku_Solver class.
 * @author Zachary Wilkins
 *
 */
public class Sudoku_Solver {
	
	private static int[][] puzzle = new int[9][9];

	@SuppressWarnings("serial")
	public static class GUI extends JFrame{
		private final int WINDOW_WIDTH = 450;
		private final int WINDOW_HEIGHT = 500;
		private final static int NUM = 81;
		private static JTextField[] field = new JTextField[NUM];
		private JPanel[] panels = new JPanel[NUM];
		private JButton solve;
		private JButton clear;
		
		public GUI(){
			setTitle("Zach's Sudoku Solver");
			setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new GridLayout(10, 9));
			boolean check = true;
			int row = 1;
			for (int i = 0, k = 0; i < NUM; ++i, ++k){
				panels[i] = new JPanel();
				field[i] = new JTextField(1);
				panels[i].add(field[i]);
				panels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				if (check == true){
					if (row < 4 || row > 6)
						panels[i].setBackground(Color.GRAY);
				}
				if (check == false){
					if (row > 3 && row < 7)
						panels[i].setBackground(Color.GRAY);
				}
				add(panels[i]);
				switch (k){
					case 2: 	if (check == true)
									check = false;
								else
									check = true;
								break;
					case 5:		if (check == true)
									check = false;
								else
									check = true;
								break;
					case 8: 	k = -1;
								++row;
								break;
				}
			}
			solve = new JButton("Solve");
			clear = new JButton("Clear");
			solve.addActionListener(new SolveButtonListener());
			clear.addActionListener(new ClearButtonListener());
			add(solve);
			add(clear);
			setVisible(true);
		}
		
		private static class SolveButtonListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				int k = 0;
				for (int i = 0; i < 9; ++i){
					for (int j = 0; j < 9; ++j){
						try{
							puzzle[j][i] = Integer.parseInt(field[k].getText());
						}
						catch(Exception NumberFormatException){
							puzzle[j][i] = 0;
						}
						finally{
							++k;
						}
					}
				}
				
				k = 0;
								
				if (Search(puzzle)){
					for (int i = 0; i < 9; ++i){
						for (int j = 0; j < 9; ++j){
							field[k].setText(Integer.toString(puzzle[j][i]));
							++k;
						}
					}
					
					//Clean up
					k = 0;
					JOptionPane.showMessageDialog(null, "Solved!");
				}
				else{
					JOptionPane.showMessageDialog(null, "Unable to solve :(");
				}
			}
		}
		
		private static class ClearButtonListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				for (int i = 0, k = 0; i < 9; ++i){
					for (int j = 0; j < 9; ++j){
						field[k].setText("");
						++k;
					}
				}				
			}
			
		}
	}
	
	public static void main(String[] args) {
		new GUI();
	}
	
	/**
	 * This recursive method takes a 2D sudoku array and returns true when the puzzle
	 * is solved or false if the puzzle is unsolvable.
	 * @param S a 2D integer array representing a Sudoku puzzle
	 * @return true on puzzle completion, false otherwise
	 */
	public static boolean Search(int[][] S){
		
		if (isInvalid(S))
			return false;
		
		if (isComplete(S))
			return true;
		
		int pencil = 1;
		// Keep going 'til the puzzle is finished.
			
		// Find a zero to work on.
		for (int i = 0; i < 9; ++i){
			for (int j = 0; j < 9; ++j){
				if (S[j][i] != 0){}
				else{
					boolean keepLoop = true;
					while (keepLoop){
						// if it's a zero
						S[j][i] = pencil;
						
						if (Search(S)){
							keepLoop = false;
							return true;
						}
						else{
							//undo!
							S[j][i] = 0;
							if (pencil < 9)
								++pencil;
							else
								return false;
						}
					}
				}
			}
		}
		
		// if all else fails...
		return false;
	}
	
	/**
	 * This method checks if the solution is invalid.
	 * @param solution a 2D array to be checked
	 * @return true if, and only if, the solution is invalid. False otherwise.
	 */
	public static boolean isInvalid(int[][] solution){
		
		int i = 0;
		ArrayList<Integer> checkList = new ArrayList<Integer>(9);
		
		// The X value remains frozen, so this is checking a column.
		while (i < 9){
			// Clear the list every time a new column begins
			checkList.clear();
			// Always look at the first value to compare by.
			checkList.add(solution[i][0]);
			for (int j = 1; j < 9; ++j){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[i][j] == checkList.get(k) && solution[i][j] != 0)
						return true;
					else{}
				}
				checkList.add(solution[i][j]);
			}
			++i;
		}
		
		// Clean up...
		i = 0;
		checkList.clear();
		
		// The Y value remains frozen, so this is checking a row.
		while (i < 9){
			// Clear the list every time a new row begins
			checkList.clear();
			// Always look at the first value to compare by.
			checkList.add(solution[0][i]);
			for (int j = 1; j < 9; ++j){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[j][i] == checkList.get(k) && solution[j][i] != 0)
						return true;
					else{}
				}
				checkList.add(solution[j][i]);
			}
			++i;
		}
		
		// Clean up...
		i = 0;
		checkList.clear();
		
		// Top left
		checkList.clear();
		for (int o = 0; o < 3; ++o){
			for (int p = 0; p < 3; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Top mid
		checkList.clear();
		for (int o = 0; o < 3; ++o){
			for (int p = 3; p < 6; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Top right
		checkList.clear();
		for (int o = 0; o < 3; ++o){
			for (int p = 6; p < 9; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Mid left
		checkList.clear();
		for (int o = 3; o < 6; ++o){
			for (int p = 0; p < 3; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Mid mid
		checkList.clear();
		for (int o = 3; o < 6; ++o){
			for (int p = 3; p < 6; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Mid right
		checkList.clear();
		for (int o = 3; o < 6; ++o){
			for (int p = 6; p < 9; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Bottom left
		checkList.clear();
		for (int o = 6; o < 9; ++o){
			for (int p = 0; p < 3; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Bottom mid
		checkList.clear();
		for (int o = 6; o < 9; ++o){
			for (int p = 3; p < 6; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		// Bottom right
		checkList.clear();
		for (int o = 6; o < 9; ++o){
			for (int p = 6; p < 9; ++p){
				for (int k = 0; k < checkList.size(); ++k){
					if (solution[p][o] == checkList.get(k) && solution[p][o] != 0){
						return true;
					}
					else{}
				}
				checkList.add(solution[p][o]);
			}
		}
		
		return false;
	}
	
	/**
	 * This method checks if a solution is complete. That is,
	 * every element is filled.
	 * @param solution a 2D array to be checked
	 * @return true if, and only if, the solution is complete. False otherwise.
	 */
	public static boolean isComplete(int[][] solution){
		return !hasZero(solution);
	}
	
	/**
	 * This method checks a solution for any remaining zeroes (incomplete elements).
	 * @param solution a 2D array to be checked
	 * @return true if zeroes remain, false otherwise.
	 */
	public static boolean hasZero(int[][] solution){
		for (int i = 0; i < 9; ++i){
			for (int j = 0; j < 9; ++j){
				if (solution[j][i] == 0){
					return true;
				}
				else{}
			}
		}
		// Else return false.
		return false;
	}
}
