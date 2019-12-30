//Name - Vinay Sisodiya
//Andrew ID - vsisodiy

package hw3;

import java.util.Random;

public class Hangman extends Game{
	static final int MIN_WORD_LENGTH = 5; //minimum length of puzzle word
	static final int MAX_WORD_LENGTH = 10; //maximum length of puzzle word
	static final int HANGMAN_TRIALS = 10;  // max number of trials in a game
	static final int HANGMAN_GAME_TIME = 30; // max time in seconds for one round of game

	HangmanRound hangmanRound;

	/** setupRound() is a replacement of findPuzzleWord() in HW1. 
	 * It returns a new HangmanRound instance with puzzleWord initialized randomly drawn from wordsFromFile.
	 * The puzzleWord must be a word between HANGMAN_MIN_WORD_LENGTH and HANGMAN_MAX_WORD_LEGTH. 
	 * Other properties of Hangmanround are also initialized here. 
	 */

	@Override
	HangmanRound setupRound() {
		Random r = new Random();
		String word = WordNerdModel.wordsFromFile[r.nextInt(WordNerdModel.wordsFromFile.length)]; //randomly choosing a word from the file
		if (word.length() >= MIN_WORD_LENGTH && word.length() <= MAX_WORD_LENGTH) //ensuring that the chosen word has length between 5 and 10
		{
			hangmanRound = new HangmanRound();
			hangmanRound.setPuzzleWord(word); //setting the puzzle word
			hangmanRound.setClueWord(makeAClue(word)); //setting the clue word
			hangmanRound.setHitCount(0);
			hangmanRound.setMissCount(0);
			hangmanRound.setIsRoundComplete(false);
		}
		else
		{
			setupRound(); // call again to choose another word
		}

		return hangmanRound;
	}


	/** Returns a clue that has at least half the number of letters in puzzleWord replaced with dashes.
	 * The replacement should stop as soon as number of dashes equals or exceeds 50% of total word length. 
	 * Note that repeating letters will need to be replaced together.
	 * For example, in 'apple', if replacing p, then both 'p's need to be replaced to make it a--le */
	@Override
	String makeAClue(String puzzleWord) {
		int blanks;
		Random r = new Random();
		if (puzzleWord.length()%2 == 0)
		{
			blanks = puzzleWord.length()/2; // for word with even number of character
		}
		else
		{
			blanks = (puzzleWord.length()+1)/2; // for word with odd number of character
		}

		while(countDashes(puzzleWord) < blanks)
		{
			char c = puzzleWord.charAt(r.nextInt(puzzleWord.length())); //choosing a random character in the puzzleword
			puzzleWord = puzzleWord.replace(c, '_'); // replaces all occurrences of c
		}
		return puzzleWord;
	}

	/** countDashes() returns the number of dashes in a clue String */ 
	int countDashes(String word) {
		int count = 0;
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) == '_')
			{
				count++;
			}
		}
		return count;
	}

	/** getScoreString() returns a formatted String with calculated score to be displayed after
	 * each trial in Hangman. See the handout and the video clips for specific format of the string. */
	@Override
	String getScoreString() {
		float d = hangmanRound.getMissCount(); // converting the denominator to float for a float division
		float score;
		if (d == 0)
		{
			score = hangmanRound.getHitCount(); // taking care of the zero division
		}
		else
		{
			score = hangmanRound.getHitCount()/d;
		}
		return ("Hit: " + hangmanRound.getHitCount() + " Miss: " + hangmanRound.getMissCount() + " Score: " + score); //this goes as the top message
	}

	/** nextTry() takes next guess and updates hitCount, missCount, and clueWord in hangmanRound. 
	 * Returns INDEX for one of the images defined in GameView (SMILEY_INDEX, THUMBS_UP_INDEX...etc. 
	 * The key change from HW1 is that because the keyboardButtons will be disabled after the player clicks on them, 
	 * there is no need to track the previous guesses made in userInputs*/
	@Override
	int nextTry(String guess) {  
		int x;
		String puzzleWord = hangmanRound.getPuzzleWord(); //getting the puzzleword
		String clueWord = hangmanRound.getClueWord(); // getting the clueword

		if (hangmanRound.getPuzzleWord().contains(guess)) // check if the selected letter is present in the puzzle word
		{
			for (int i = 0; i < puzzleWord.length(); i++)
			{
				if (puzzleWord.charAt(i) == guess.charAt(0)) //for the all the places the chosen character is present
				{
					clueWord = clueWord.substring(0, i) 
							+ guess.charAt(0) 
							+ clueWord.substring(i + 1); //place the character at its designated position
				}
			}
			hangmanRound.setClueWord(clueWord); //updating the clue word
			hangmanRound.setHitCount(hangmanRound.getHitCount()+1); // incrementing the hit count by 1

			if (countDashes(clueWord) == 0) // if the clue word is completely guessed i.e. zero dashes
			{
				x = 0; //smiley
			}
			else if (hangmanRound.getHitCount() + hangmanRound.getMissCount() == HANGMAN_TRIALS) //if number of trials are finished
			{
				x = 4; // sadly
			}
			else
			{
				x = 1; //thumbsup
			}
		}
		else // if guessed character didn't match with any
		{
			hangmanRound.setMissCount(hangmanRound.getMissCount()+1); // incrementing the miss count by 1
			if (hangmanRound.getHitCount() + hangmanRound.getMissCount() == HANGMAN_TRIALS) //check if the trails are finished
			{
				x = 4; //sadly
			}
			else
			{
				x = 2; //thumbsdown
			}
		}
		return x;
	}
}
