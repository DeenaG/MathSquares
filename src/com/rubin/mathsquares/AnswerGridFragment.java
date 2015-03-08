package com.rubin.mathsquares;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

public class AnswerGridFragment extends Fragment {
	
	public interface OnAnswerClickedListener{
		public void onAnswerClicked(int numberSelected);
		public void onEraserClicked();
	}
	
	//the main activity
	private OnAnswerClickedListener listener;

	//all answer drawables
	private int [] drawables = {R.drawable.button_one, R.drawable.button_two, R.drawable.button_three, R.drawable.button_four, R.drawable.button_five, R.drawable.button_six, R.drawable.button_seven, R.drawable.button_eight, R.drawable.button_nine, R.drawable.button_eraser};
	//tiles in 2x5 array to be displayed to screen
	private Tile[][] tiles = new Tile[2][5];
	//indexes of array
	private int i, j;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GridLayout view = (GridLayout)inflater.inflate(R.layout.answer_grid_fragment, container, false);

		int drawableIndex = 0;
		
		//create tiles and set listener
		for (i=0; i<tiles.length; i++){
			for (j=0; j<tiles[i].length; j++){
				tiles[i][j] = new Tile (i, j, drawableIndex+1, drawables[drawableIndex], view);
				tiles[i][j].setLayoutParams(view, 50, 50, i, j);
				tiles[i][j].getImageView().setOnClickListener(new View.OnClickListener() {
					Tile temp = tiles[i][j];
					@Override
					public void onClick(View v) {
						for (int i=0; i<tiles.length; i++){
							for (int j=0; j<tiles[i].length; j++){
								tiles[i][j].getImageView().setSelected(false);
							}
						}
						temp.getImageView().setSelected(true);
						if (temp.getValue() < 10){
							Log.d("answer listener", "value is a number: " + temp.getValue());
							listener.onAnswerClicked(temp.getValue());
						}
						else{
							Log.d("answer listener", "value is eraser: " + temp.getValue());
							listener.onEraserClicked();
						}
					}
				});

				view.addView(tiles[i][j].getImageView());
				drawableIndex++;
			}
		}
		return view;
	}
	
	@Override
	//check to make sure main activity implements interface
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnAnswerClickedListener){
			listener = (OnAnswerClickedListener)activity;
		}
		else{
			throw new ClassCastException (activity.toString()+ "must implement AnswerGridFragment.OnAnswerClickedListener");
		}
	}
	
	/**
	 * set an answergrid tile to disabled
	 * @param num value of tile to be disabled
	 */
	public void setDisabled(int num){
		Tile tile = null;
		for (int i=0; i<tiles.length; i++)
			for (int j=0; j<tiles[i].length; j++)
				if (tiles[i][j].getValue() == num){
					tile = tiles[i][j];
					tile.getImageView().setEnabled(false);
					tile.getImageView().setSelected(false);
				}
	}
	
	/**
	 * set an answergird tile to enabled
	 * @param num value of tile to be enabled
	 */
	public void setEnabled (int num){
		for (int i=0; i<tiles.length; i++)
			for (int j=0; j<tiles[i].length; j++)
				if (tiles[i][j].getValue() == num){
					tiles[i][j].getImageView().setEnabled(true);
				}
		tiles[1][4].getImageView().setSelected(false);
	}
	
	/**
	 * @param disabled an array of tiles to disable
	 */
	public void setInitialDisabled (int[] disabled){
		for (int i=0; i<tiles.length; i++){
			for (int j=0; j<tiles[i].length; j++){
				for (int k=0; k<disabled.length; k++){
					if (tiles[i][j].getValue() == disabled[k]){
						tiles[i][j].setValue(disabled[k]);
						tiles[i][j].getImageView().setEnabled(false);
					}
				}
			}
		}
		
	}

}
