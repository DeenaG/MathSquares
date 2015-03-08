package com.rubin.mathsquares;

import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

public class Tile {
	private int row;
	private int col;
	private int value;
	private ImageView imageView;
	
	/**
	 * @param row row index of tile
	 * @param col col index of tile
	 * @param value numeric value of tile
	 * @param drawableID id of drawable object
	 * @param view 
	 */
	
	public Tile (int row, int col, int value, int drawableID, View view){
		this.row = row;
		this.col = col;
		this.value = value;
		this.imageView = new ImageView(view.getContext());
		this.imageView.setImageResource(drawableID);
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getValue() {
		return value;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public void setImageResource(int id){
		imageView.setImageResource(id);
	}
	
	/**
	 * @param view
	 * @param height height of drawable
	 * @param width width of drawable
	 * @param row row in gridlayout
	 * @param col column in gridlayout
	 */
	public void setLayoutParams(View view, int height, int width, int row, int col){
		GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
		layoutParams.rowSpec = GridLayout.spec(row);
		layoutParams.columnSpec = GridLayout.spec(col);
		int heightDIP = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, view.getResources().getDisplayMetrics());
		int widthDIP = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, view.getResources().getDisplayMetrics());
		imageView.setLayoutParams(layoutParams);
		imageView.getLayoutParams().height = heightDIP;
		imageView.getLayoutParams().width = widthDIP;
	}
	
}
