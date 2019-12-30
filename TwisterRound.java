//Name - Vinay Sisodiya
//Andrew ID - vsisodiy

package hw3;


import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TwisterRound extends GameRound {

	private ObservableList<String> solutionWordsList;
	private ObservableList<ObservableList<String>> solutionListsByWordLength;
	private ObservableList<ObservableList<String>> submittedListsByWordLength;

	TwisterRound() //initializing all the arrays
	{
		solutionWordsList = FXCollections.observableArrayList();
		solutionListsByWordLength = FXCollections.observableArrayList();
		for (int i = 0; i < Twister.TWISTER_MAX_WORD_LENGTH - Twister.TWISTER_MIN_WORD_LENGTH + 1; i++) // creating 5 child array list for each of the possible word length
		{
			ObservableList<String> childList = FXCollections.observableArrayList();
			solutionListsByWordLength.add(childList);
		}
		submittedListsByWordLength = FXCollections.observableArrayList();
		for (int i = 0; i < Twister.TWISTER_MAX_WORD_LENGTH - Twister.TWISTER_MIN_WORD_LENGTH + 1; i++)
		{
			ObservableList<String> childList1 = FXCollections.observableArrayList();
			submittedListsByWordLength.add(childList1);
		}
	}
	//set all the elements of passed list to solution word list
	public void setSolutionWordsList(List<String> solutionWordsList) { this.solutionWordsList.setAll(solutionWordsList); }
	//get solution list in form of an normal array list
	public List<String> getSolutionWordsList() { return solutionWordsList;}
	//returns solution word list in observable list form
	public ObservableList<String> solutionWordsListProperty() { return solutionWordsList; }
    
	//setting a passed word to the corresponding list (depending on the size of the word)
	public void setSolutionListsByWordLength(String word) 
	{ 
		solutionListsByWordLength.get(word.length()-Twister.TWISTER_MIN_WORD_LENGTH).add(word);	
	}

	public ObservableList<ObservableList<String>> getSolutionListsByWordLength()
	{
		return solutionListsByWordLength; //returns complete SolutionListsByWordLength
	}

	public ObservableList<String> getSolutionListsByWordLength(int letterCount)
	{
		return solutionListsByWordLength.get(letterCount); //returns one of the observable list inside SolutionListsByWordLength
	}

	public ObservableList<ObservableList<String>> solutionListsByWordLengthProperty() { return solutionListsByWordLength;}

	//Defining getter and setter for submittedListsByWordLength similar to above
	public void setSubmittedListsByWordLength(String word) 
	{ 
		submittedListsByWordLength.get(word.length()-Twister.TWISTER_MIN_WORD_LENGTH).add(word);	
	}

	public ObservableList<ObservableList<String>> getSubmittedListsByWordLength()
	{
		return submittedListsByWordLength;
	}

	public ObservableList<String> getSubmittedListsByWordLength(int letterCount)
	{
		return submittedListsByWordLength.get(letterCount);
	}

	public ObservableList<ObservableList<String>> submittedListsByWordLengthProperty() { return submittedListsByWordLength;}
}
