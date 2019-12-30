//Name - Vinay Sisodiya
//Andrew ID - vsisodiy

package hw3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Twister extends Game {

	static final int SOLUTION_LIST_COUNT = 5;
	static final int TWISTER_MAX_WORD_LENGTH = 7;	
	static final int TWISTER_MIN_WORD_LENGTH = 3; 
	static final int NEW_WORD_BUTTON_INDEX = 0; // index for new button
	static final int TWIST_BUTTON_INDEX = 1; // index for twist button
	static final int CLEAR_BUTTON_INDEX = 2; // index for clear button
	static final int SUBMIT_BUTTON_INDEX = 3; // // index for submit button
	static final int CLUE_BUTTON_SIZE = 75;
	static final int TWISTER_GAME_TIME = 120;
	static final int MIN_SOLUTION_WORDCOUNT = 10; //to ensure that selected word has at least 10 words that can be made out of it 

	TwisterRound twisterRound;

	@Override
	TwisterRound setupRound() {
		// TODO Auto-generated method stub
		Random r = new Random();
		String word = WordNerdModel.wordsFromFile[r.nextInt(WordNerdModel.wordsFromFile.length)]; // chosing a random word
		List<String> possibleWords = new ArrayList<>(); // initializing an arraylist to store possible words
		List<Character> wordCharArray = new ArrayList<>();
		for (char c : word.toCharArray()) 
		{
			wordCharArray.add(c); //characters of the chosen word stored in an arraylist
		}

		//looping through all the words to check what all words can be made out of the chosen word
		for (String s: WordNerdModel.wordsFromFile)
		{
			if (s.length()>= TWISTER_MIN_WORD_LENGTH && s.length() <= word.length()) //ensuring that each selected word from the file has length between 3 and length of the chosen word
			{
				List<Character> clonedWord = new ArrayList<>(wordCharArray); //making a copy of the array because I am going to remove a character if it matches

				int flag = 0;
				for (int i = 0; i < s.length(); i++)
				{
					if (!clonedWord.contains(s.charAt(i)))
					{
						flag = 0;
						break; //even if one letter doesn't match break the loop and go to next word
					}
					else
					{
						flag =1;
						clonedWord.remove(Character.valueOf(s.charAt(i))); //removing the letter from the array if it matches
					}

				}
				if (flag == 1) // if all the letters match
				{
					possibleWords.add(s); // add the word
				}
			}
		}

		//ensuring that the chosen word has length between 3 and 7 and at least 10 words can be made out of it
		if (word.length() >= TWISTER_MIN_WORD_LENGTH && word.length() <= TWISTER_MAX_WORD_LENGTH && possibleWords.size() >=MIN_SOLUTION_WORDCOUNT)
		{   
			//setting up the twisterRound including the lists
			twisterRound = new TwisterRound();
			twisterRound.setPuzzleWord(word);
			twisterRound.setSolutionWordsList(possibleWords);
			twisterRound.setClueWord(makeAClue(twisterRound.getPuzzleWord())); //making clue
			for (String s: twisterRound.getSolutionWordsList())
			{
				twisterRound.setSolutionListsByWordLength(s);
			}
			twisterRound.setIsRoundComplete(false);
		}
		else
		{
			setupRound();
		}
		return twisterRound;
	}

	@Override
	String makeAClue(String puzzleWord) {
		// TODO Auto-generated method stub
		char[] characters = puzzleWord.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			Random r = new Random();
			int randomIndex = r.nextInt(characters.length);
			char temp = characters[i];
			characters[i] = characters[randomIndex]; //shuffling
			characters[randomIndex] = temp;
		}
		return new String(characters);
	}

	@Override
	String getScoreString() {
		// TODO Auto-generated method stub
		int guessedWords = 0;
		int possibleWords = twisterRound.getSolutionWordsList().size();

		for (int i = 0; i < twisterRound.getSubmittedListsByWordLength().size(); i++)
		{
			if (twisterRound.getSubmittedListsByWordLength(i).isEmpty())
			{
				guessedWords +=0;
			}
			else
			{
				guessedWords += twisterRound.getSubmittedListsByWordLength(i).size();
			}
		}
		//this string goes as the top text message
		return "Twist to find " + String.valueOf(possibleWords-guessedWords) + " of " + String.valueOf(possibleWords) + " words";
	}

	@Override
	int nextTry(String guess) {
		// TODO Auto-generated method stub
		if (twisterRound.getSubmittedListsByWordLength(guess.length()-TWISTER_MIN_WORD_LENGTH).contains(guess))
		{
			return 3; //repeat index if the guess is already present in the solution word list
		}
		else if (twisterRound.getSolutionListsByWordLength(guess.length()-TWISTER_MIN_WORD_LENGTH).contains(guess)) // guess matches with one of the solution
		{
			twisterRound.setSubmittedListsByWordLength(guess); //updating the submitted word list corresponding to the guessed word length
			Collections.sort(twisterRound.getSubmittedListsByWordLength(guess.length()-TWISTER_MIN_WORD_LENGTH)); // sorting after updating
			int guessedWords = 0;
			for (int i = 0; i < twisterRound.getSubmittedListsByWordLength().size(); i++)
			{
				guessedWords += twisterRound.getSubmittedListsByWordLength(i).size();
			}
			if (guessedWords == twisterRound.getSolutionWordsList().size()) //checking if all the words have been guessed
			{
				return 0; //smiley
			}
			else
			{
				return 1; //thumbsUp
			}
		}
		else 
		{
			return 2; //thumbsDown
		}

	}

}
