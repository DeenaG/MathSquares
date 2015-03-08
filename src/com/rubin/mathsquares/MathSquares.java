package com.rubin.mathsquares;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MathSquares extends Activity implements AnswerGridFragment.OnAnswerClickedListener, GameGridFragment.OnGridSpaceClickedListener{
	private boolean answerGridSelected = false;
	private boolean eraserClicked = false;
	private int numToPlace;
	GameGridFragment gameGrid; 
	AnswerGridFragment answerGrid; 
	int[]disabled;
	
	Button submit;
	Button switchToGame;
	TextView timer;
	
	private TestCountDownTimer countDownTimer;

    private final long startTime = 600000;
    private final long interval = 1000;
    
    private long millisLeft = startTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_math_squares);
		Log.e("e", "e");
		
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		millisLeft = preferences.getLong("long", millisLeft);
		
		submit = (Button)findViewById(R.id.submit);
		switchToGame = (Button)findViewById(R.id.switchToGame);
		timer = (TextView)findViewById(R.id.timer);
		
		countDownTimer = new TestCountDownTimer(millisLeft, interval);
        countDownTimer.start();
		
		gameGrid = (GameGridFragment)getFragmentManager().findFragmentById(R.id.game_grid);
		answerGrid = (AnswerGridFragment)getFragmentManager().findFragmentById(R.id.answer_grid);
		if (answerGrid != null){
			answerGrid.setInitialDisabled(disabled);
		}
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (gameGrid.isFinished())
					recreate();
				else
					Toast.makeText(getApplicationContext(), "you must fill in all the tiles", Toast.LENGTH_SHORT).show();
				
			}
		});
			
	}

	@Override
	public void onAnswerClicked(int numberSelected) {
		Log.d("listener method", "answer clicked" + numberSelected);
		answerGridSelected = true;
		numToPlace = numberSelected;
	}
	public void onEraserClicked(){
		Log.d("listener method", "eraser clicked");
		answerGridSelected = false;
		eraserClicked = true;
	}

	@Override
	public void onGridSpaceClicked(int numberSelected, int row, int col) {
		if (!answerGridSelected && !eraserClicked)
			Toast.makeText(this, "you must select a number first", Toast.LENGTH_SHORT).show();
		else if (answerGridSelected && numberSelected == 0){
			Log.d("grid space method", "answerGrid selected - swapping" + "numToPlace: " + numToPlace + " numberSelected: " + numberSelected );
			gameGrid.swapImage(numToPlace, row, col);
			answerGrid.setDisabled(numToPlace);
			answerGridSelected = false;
			numToPlace = 0;
		}
		else if(eraserClicked && numberSelected != 0 && !answerGridSelected){
			Log.d("grid space method", "eraser selected "+ "numToPlace: " + numToPlace + " numberSelected: " + numberSelected);
			gameGrid.eraseImage(row, col);
			answerGrid.setEnabled(numberSelected);
			eraserClicked = false;
		}
		else{
			Log.d("in else", "in else");
			Log.d("answerGridSelected", Boolean.toString(answerGridSelected));
			Log.d("eraserClicked", Boolean.toString(eraserClicked));
			Log.d("numberSelected", Integer.toString(numberSelected));
		}
	}

	@Override
	public void setDisabled(int[] disabled) {
		this.disabled = disabled;
	}
	
	@Override
	protected void onPause() 
	{
	  super.onPause();
	  
	  // Store values between instances here
	  SharedPreferences preferences = getPreferences(MODE_PRIVATE);
	  SharedPreferences.Editor editor = preferences.edit();  
	  
	  editor.putLong("long", millisLeft);
	  
	  editor.commit();
	} 

	private class TestCountDownTimer extends CountDownTimer{

		public TestCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			timer.setText("Time's up!");
			millisLeft = startTime;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			millisLeft = millisUntilFinished;
			long minutes = (millisUntilFinished/60000);
			long seconds = millisUntilFinished%60000/1000;
			
			String secondsFormat;
			if (seconds >= 10)
				secondsFormat = Long.toString(seconds);
			else
				secondsFormat = ("0" + seconds);
			timer.setText(minutes + " : " + secondsFormat);
		}
		
	}

}
