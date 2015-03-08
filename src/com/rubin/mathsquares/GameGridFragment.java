package com.rubin.mathsquares;


import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameGridFragment extends Fragment {
	
	public interface OnGridSpaceClickedListener{
		/**
		 * @param num value of the tile clicked
		 * @param row row of num clicked
		 * @param col column of num clicked
		 */
		public void onGridSpaceClicked(int num, int row, int col);
		/**
		 * @param disabled array of numbers to be disabled in answergrid
		 */
		public void setDisabled(int[]disabled);
	}
	
	//MathSquares activity
	private OnGridSpaceClickedListener listener;

	public static final int ADD = 0;
	public static final int SUBTRACT = 1;
	
	//number of tile to disable by default
	private static final int DISABLED_SIZE = 3;
	
	private Tile[][] tiles = new Tile[3][3];
	private int[][] signGrid = new int[6][2];
	private int i, j, indexRow, indexCol;
	private int[][] numberGrid = new int[3][3];
	private int[] answerList = new int[6];
	private int [] drawables = {R.drawable.button_one, R.drawable.button_two, R.drawable.button_three, R.drawable.button_four, R.drawable.button_five, R.drawable.button_six, R.drawable.button_seven, R.drawable.button_eight, R.drawable.button_nine, R.drawable.button_eraser};
	private int[] disabled = new int[DISABLED_SIZE];
	
	Random rand = new Random();
	
	
	//text views for the answers in columns and rows
	private TextView rowZero;
	private TextView rowTwo;
	private TextView rowFour;
	private TextView colZero;
	private TextView colTwo;
	private TextView colFour;
	
	//imageviews for the signs
	private ImageView zeroLeft;
	private ImageView zeroRight;
	private ImageView oneLeft;
	private ImageView oneMiddle;
	private ImageView oneRight;
	private ImageView twoLeft;
	private ImageView twoRight;
	private ImageView threeLeft;
	private ImageView threeMiddle;
	private ImageView threeRight;
	private ImageView fourLeft;
	private ImageView fourRight;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GridLayout view = (GridLayout)inflater.inflate(R.layout.game_grid_fragment, container, false);
		fillGrid(); //fill grid with random numbers 1 - 9
		fillSigns(); //randomly fill the sign imageviews with signs
		fillAnswers(); //fill the answers in columns and rows
		createUIAnswers(view); //create the UI of answers
		createUISigns(view); //create the UI with signs
		int value=1;
		//create the number tiles and set listeners
		for (i=0, indexRow=0; i<tiles.length; i++, indexRow+=2){
			for (j=0, indexCol=0; j<tiles[i].length; j++, indexCol+=2){
				tiles[i][j] = new Tile(i, j, 0, R.drawable.question, view);
				tiles[i][j].setLayoutParams(view, 50, 50, indexRow, indexCol);
				tiles[i][j].getImageView().setOnClickListener(new View.OnClickListener() {
					Tile temp = tiles[i][j];
					@Override
					public void onClick(View v) {
						listener.onGridSpaceClicked(temp.getValue(), temp.getRow(), temp.getCol());
						Log.d("gamegrid click", "tile clicked " + temp.getValue());
					}
				});
				
				view.addView(tiles[i][j].getImageView());
				value++;

			}
		}
		
		
		//set the initial disabled tiles
		for (int i=0; i<DISABLED_SIZE; i++){
			int x = rand.nextInt(3);
			int y = rand.nextInt(3);
			if (tiles[x][y].getImageView().isEnabled()){
				tiles[x][y].setValue(numberGrid[x][y]);
				tiles[x][y].setImageResource(drawables[tiles[x][y].getValue()-1]);
				tiles[x][y].getImageView().setEnabled(false);
				disabled[i] = tiles[x][y].getValue();
			}
			else
				i--;
		}
		return view;
	}
	
	public boolean isFinished(){
		for (int i=0; i<tiles.length; i++){
			for (int j=0; j<tiles[i].length; j++){
				if (tiles[i][j].getValue() == 0)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * fill a 3x3 grid randomly with numbers 1-9
	 */
	private void fillGrid() {
		Random rand = new Random();
		int index = 0;
		int []tempGrid = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int i=tempGrid.length-1; i>0; i--){
			int r = rand.nextInt(i+1);
			int temp = tempGrid[r];
			tempGrid[r] = tempGrid[i];
			tempGrid[i] = temp;
		}
		for(int i=0; i<numberGrid.length; i++){
			for (int j=0; j<numberGrid[i].length; j++){
				numberGrid[i][j] = tempGrid[index];
				index++;
				Log.d(Integer.toString(i), Integer.toString(numberGrid[i][j]));
			}			
		}
	}
	
	/**
	 * fill a 2x5 grid randomly with plus and minus signs
	 */
	private void fillSigns(){
		Random rand = new Random();
		for (int i=0; i<signGrid.length; i++)
			for (int j=0; j<signGrid[i].length; j++){
				int r = rand.nextInt(2);
				signGrid[i][j] = r;	
			}
	}
	
	/**
	 * create an array of answers
	 */
	private void fillAnswers(){
		int i=0, j=0, k=0, l=0, answer = 0, answerRow =0, answerCol=0, answerCountRow=0, answerCountCol=0, m=0;
		for (i=0, k=0; i<numberGrid.length; i++, k++){
			answer = numberGrid[i][0];
			for(j=1, l=0; j<numberGrid[i].length; j++, l++){
				if (signGrid[k][l]==ADD)
					answer += numberGrid[i][j];
				else
					answer -= numberGrid[i][j];
			}
			answerList[i] = answer;
		}
		int answerCount = 3;
		for (i=0, k=3; i<numberGrid.length; i++, k++){
			answer = numberGrid[0][i];
			for(j=1, l=0; j<numberGrid[i].length; j++, l++){
				if (signGrid[k][l]==ADD)
					answer += numberGrid[j][i];
				else
					answer -= numberGrid[j][i];
			}
			answerList[answerCount] = answer;
			answerCount++;
		}
		
	}
	
	/**
	 * display answers in UI
	 * @param view
	 */
	private void createUIAnswers(View view) {
		rowZero = (TextView)view.findViewById(R.id.answer_row_zero);
		rowTwo = (TextView)view.findViewById(R.id.answer_row_two);
		rowFour = (TextView)view.findViewById(R.id.answer_row_four);
		colZero = (TextView)view.findViewById(R.id.answer_col_zero);
		colTwo = (TextView)view.findViewById(R.id.answer_col_two);
		colFour = (TextView)view.findViewById(R.id.answer_col_four);
		TextView[] answerTextViews = {rowZero, rowTwo, rowFour, colZero, colTwo, colFour};
		for (int i=0; i<answerTextViews.length; i++)
			answerTextViews[i].setText(Integer.toString(answerList[i]));
	}
	
	/**
	 * display signs in UI
	 * @param view
	 */
	private void createUISigns(View view){
		zeroLeft = (ImageView)view.findViewById(R.id.sign_zero_left);
		zeroRight = (ImageView)view.findViewById(R.id.sign_zero_right);
		oneLeft = (ImageView)view.findViewById(R.id.sign_one_left);
		oneMiddle = (ImageView)view.findViewById(R.id.sign_one_middle);
		oneRight = (ImageView)view.findViewById(R.id.sign_one_right);
		twoLeft = (ImageView)view.findViewById(R.id.sign_two_left);
		twoRight = (ImageView)view.findViewById(R.id.sign_two_right);
		threeLeft = (ImageView)view.findViewById(R.id.sign_three_left);
		threeMiddle = (ImageView)view.findViewById(R.id.sign_three_middle);
		threeRight = (ImageView)view.findViewById(R.id.sign_three_right);
		fourLeft = (ImageView)view.findViewById(R.id.sign_four_left);
		fourRight = (ImageView)view.findViewById(R.id.sign_four_right);
		
		ImageView[][] signs = {{zeroLeft, zeroRight}, {twoLeft, twoRight}, {fourLeft, fourRight}, {oneLeft, threeLeft}, {oneMiddle, threeMiddle}, {oneRight, threeRight}};
		for (int i=0; i<signs.length; i++){
			for (int j=0; j<signs[i].length; j++){
				if (signGrid[i][j] == ADD)
					signs[i][j].setImageResource(R.drawable.plus);
				else
					signs[i][j].setImageResource(R.drawable.minus);
			}
		}
	}
	
	@Override
	//check to make sure main activity implements interface
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnGridSpaceClickedListener){
			listener = (OnGridSpaceClickedListener)activity;
			listener.setDisabled(disabled);
		}
		else{
			throw new ClassCastException (activity.toString()+ "must implement GameGridFragment.OnGameGridSpaceClickedListener");
		}
	}
	
	/**
	 * swap an image for a number
	 * @param num value of tile to swap
	 * @param row row of tile
	 * @param col col of tile
	 */
	public void swapImage(int num, int row, int col){
		if (num < 10){
			tiles[row][col].setImageResource(drawables[num-1]);
			tiles[row][col].setValue(num);
		}
	}
	/**
	 * replace numer with question mark
	 * @param row of tile
	 * @param col col or tile
	 */
	public void eraseImage(int row, int col){
		tiles[row][col].setImageResource(R.drawable.question);
		tiles[row][col].setValue(0);
	}
	

}
