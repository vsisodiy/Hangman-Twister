//Name - Vinay Sisodiya
//Andrew ID - vsisodiy
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Score {
	//defining all the instance variables and properties
	private IntegerProperty gameId = new SimpleIntegerProperty();
	private StringProperty puzzleWord = new SimpleStringProperty();
	private IntegerProperty timeStamp = new SimpleIntegerProperty();
	private FloatProperty score = new SimpleFloatProperty();
	
	public Score() {} //not required
	
	public Score (int gameId, String puzzleWord, int timeStamp, float score)
	{
		this.gameId.set(gameId);
		this.puzzleWord.set(puzzleWord);
		this.timeStamp.set(timeStamp);
		this.score.set(score);
		
	}
	
	//getters and setters for score private variables
	public void setGameId(int gameId) { this.gameId.set(gameId); }
	public int getGameId() { return gameId.get(); }
	public IntegerProperty gameIdProperty() { return gameId; }
	
	public void setPuzzleWord(String puzzleWord) { this.puzzleWord.set(puzzleWord); }
	public String getPuzzleWord() { return puzzleWord.get(); }
	public StringProperty puzzleWordProperty() { return puzzleWord; }
	
	public void setTimeStamp(int timeStamp) { this.timeStamp.set(timeStamp); }
	public int getTimeStamp() { return timeStamp.get(); }
	public IntegerProperty timeStampProperty() { return timeStamp; }
	
	public void setScore(float score) { this.score.set(score); }
	public float getScore() { return score.get(); }
	public FloatProperty scoreProperty() { return score; }

}
