//Name - Vinay Sisodiya
//Andrew ID - vsisodiy

package hw3;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class TwisterController extends WordNerdController {

	TwisterView twisterView;
	Twister twister;

	WordNerdModel wordNerdModel = new WordNerdModel();

	@Override
	void startController() 
	{
		//starts the game
		twisterView = new TwisterView();
		twister = new Twister();
		twister.twisterRound = twister.setupRound(); // setting up twister round - choosing word, creating clue, setting up lists
		twisterView.refreshGameRoundView(twister.twisterRound); // refreshes view and clears old values
		twisterView.topMessageText.setText(twister.getScoreString()); //resets the top text
		setupBindings(); // redefines the bindings

		VBox lowerPanel = new VBox();
		lowerPanel.getChildren().add(twisterView.bottomGrid);
		lowerPanel.getChildren().add(WordNerd.exitButton);
		lowerPanel.setAlignment(Pos.CENTER);

		WordNerd.root.setTop(twisterView.topMessageText);
		WordNerd.root.setCenter(twisterView.topGrid);
		WordNerd.root.setBottom(lowerPanel);

		//setting up action for play buttons
		twisterView.playButtons[Twister.NEW_WORD_BUTTON_INDEX].setOnAction(new NewButtonHandler());
		twisterView.playButtons[Twister.TWIST_BUTTON_INDEX].setOnAction(new TwistButtonHandler());
		twisterView.playButtons[Twister.CLEAR_BUTTON_INDEX].setOnAction(new ClearButtonHandler());
		twisterView.playButtons[Twister.SUBMIT_BUTTON_INDEX].setOnAction(new SubmitButtonHandler());

	}

	@Override
	void setupBindings() {

		//When timer runs out, set smiley to sadly, isRoundComplete to true
		GameView.wordTimer.timeline.setOnFinished(event -> { 
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SADLY_INDEX]);
			twister.twisterRound.setIsRoundComplete(true);
			//writing in the csv as time has ran out and the round is complete
			StringBuilder scroreWriter = new StringBuilder(); //holds the score String to be passed to model's read method 
			scroreWriter.append(WordNerd.TWISTER_ID + ",");
			scroreWriter.append(twister.twisterRound.getPuzzleWord() + ",");
			int timeLeft = Integer.parseInt(GameView.wordTimer.timerButton.getText());
			scroreWriter.append(String.valueOf(120-timeLeft) + ",");
			int guessedWords = 0;
			float possibleWords = twister.twisterRound.getSolutionWordsList().size();

			for (int i = 0; i < twister.twisterRound.getSubmittedListsByWordLength().size(); i++)
			{
				if (twister.twisterRound.getSubmittedListsByWordLength(i).isEmpty())
				{
					guessedWords +=0;
				}
				else
				{
					guessedWords += twister.twisterRound.getSubmittedListsByWordLength(i).size();
				}
			}
			scroreWriter.append(guessedWords/possibleWords);
			wordNerdModel.writeScore(scroreWriter.toString());			
		});

		// disable clue grid if the round is complete
		twisterView.clueGrid.disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		for (int i = 1; i < twisterView.playButtons.length; i++) //disabling the play buttons except the new button
		{
			twisterView.playButtons[i].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		}	

		for (int i =0; i < Twister.SOLUTION_LIST_COUNT; i++) // attaching the submitted word list to the list view
		{
			twisterView.solutionListViews[i].setItems(twister.twisterRound.getSubmittedListsByWordLength(i));
		}

		for (int i = 0; i < twister.twisterRound.getSubmittedListsByWordLength().size(); i++ ) 
		{ 
			//setting up intial test for the word score labels
			twisterView.wordScoreLabels[i].setText(String.format("%d" + "/" + "%d", 
					twister.twisterRound.getSubmittedListsByWordLength(i).size(), twister.twisterRound.getSolutionListsByWordLength(i).size()));

			//attaching listener to each of the submitted word list and updating the change in the corresponding word score label
			twister.twisterRound.getSubmittedListsByWordLength(i).addListener((ListChangeListener<String>) c ->
			{
				for (int j = 0; j < twisterView.wordScoreLabels.length; j++ )
				{twisterView.wordScoreLabels[j].setText(String.format("%d" + "/" + "%d", 
						twister.twisterRound.getSubmittedListsByWordLength(j).size(), twister.twisterRound.getSolutionListsByWordLength(j).size()));
				}
			});
		}

		//setting action for the clue button
		for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++)
		{
			twisterView.clueButtons[i].setOnAction(new ClueButtonHandler());
		}

		//setting action for the answer button
		for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++)
		{
			twisterView.answerButtons[i].setOnAction(new AnswerButtonHandler());
		}
	}

	class NewButtonHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event) 
		{
			//sets up new round, refreshes view, resets the timer, the top text and the smiley button, sets up the bindings again
			twister.twisterRound = twister.setupRound();
			twisterView.refreshGameRoundView(twister.twisterRound);
			GameView.wordTimer.restart(Twister.TWISTER_GAME_TIME);
			twisterView.topMessageText.setText(twister.getScoreString());
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SMILEY_INDEX]);
			setupBindings();
		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent> 
	{
		@Override
		//Clears the letters in answerButtons, if any, and moves them to empty slots in clueButtons
		public void handle(ActionEvent event) {
			for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++)
			{
				if (twisterView.answerButtons[i].getText() != "")
				{
					for (int j = 0; j < twister.twisterRound.getClueWord().length(); j++) 
					{
						if (twisterView.clueButtons[j].getText() == "")
						{
							twisterView.clueButtons[j].setText(twisterView.answerButtons[i].getText());
							twisterView.answerButtons[i].setText("");
							break;
						}
					}
				}
			}
		}
	}

	class AnswerButtonHandler implements EventHandler<ActionEvent>
	{
		@Override
		//Takes the alphabet in clicked answerButton, finds the next empty slot starting from left in clueButtons, and puts the alphabet in it. 
		//Clears the clicked answerButton
		public void handle(ActionEvent event) 
		{
			Button b = (Button)event.getSource();
			for (int j = 0; j < twister.twisterRound.getClueWord().length(); j++) 
			{
				if (twisterView.clueButtons[j].getText() == "")
				{
					twisterView.clueButtons[j].setText(b.getText());
					break;
				}
			}
			b.setText("");			
		}
	}

	class ClueButtonHandler implements EventHandler<ActionEvent>
	{
		@Override
		//Takes the alphabet in clicked clueButton, finds the next empty slot starting from left in answerButtons, and puts the alphabet in it. 
		//Clears the clicked clueButton
		public void handle(ActionEvent event) 
		{
			Button b = (Button)event.getSource();
			for (int j = 0; j < twister.twisterRound.getClueWord().length(); j++) 
			{
				if (twisterView.answerButtons[j].getText() == "")
				{
					twisterView.answerButtons[j].setText(b.getText());
					break;
				}
			}
			b.setText("");			
		}
	}

	class TwistButtonHandler implements EventHandler<ActionEvent>
	{
		@Override
		//Twists the word currently displayed in clueButtons
		public void handle(ActionEvent event) 
		{
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < twister.twisterRound.getClueWord().length(); j++) 
			{
				if (twisterView.clueButtons[j].getText() != "")
				{
					sb.append(twisterView.clueButtons[j].getText());
					twisterView.clueButtons[j].setText("");
				}
			}
			String newClue = twister.makeAClue(sb.toString());
			for (int j = 0; j < newClue.length(); j++) 
			{
				twisterView.clueButtons[j].setText(String.valueOf(newClue.charAt(j)));
			}
		}
	}

	class SubmitButtonHandler implements EventHandler<ActionEvent>
	{
		@SuppressWarnings("static-access")
		@Override
		/*the game takes the word from answerButtons and checks if it is of TWISTER_MIN_WORD_LENGTH. If not, the smileyButton displays the image at THUMBS_DOWN_INDEX.
		b. If the submitted word's length is at least TWISTER_MIN_WORD_LENGTH, it checks if it is one of the solutionWords (loaded from words file) that has not been entered before. If yes, then the smileyButton displays the image at THUMBS_UP_INDEX. The game adds the word to one of the solutionlistViews that has words of submitted word length. The submitted word is displayed in the position of its sorted order. The game also updates the wordScoreLabel on the right of the listView as well as the scoreString at the top in topMessageText.
		c. If the submittedWord is correct but was entered before, then smileyButton displays image at REPEAT_INDEX
		d. If it is not a correct word (i.e. not in words file), then the smileyButton displays image at THUMBS_DOWN_INDEX
		e. If the player completes all words in solutionWordList, then smileyButton displays image at SMILEY_INDEX.
		f. It also writes in the csv file whenever a round is completed*/
		public void handle(ActionEvent event) 
		{
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < twister.twisterRound.getClueWord().length(); j++) 
			{
				if (twisterView.answerButtons[j].getText() != "")
				{
					sb.append(twisterView.answerButtons[j].getText());
					twisterView.answerButtons[j].setText("");
				}
			}
			if (sb.toString().length() < Twister.TWISTER_MIN_WORD_LENGTH)
			{
				twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[twisterView.THUMBS_DOWN_INDEX]);
			}
			else
			{
				int index = twister.nextTry(sb.toString());
				twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[index]);
				if (index == GameView.SMILEY_INDEX || index == GameView.SADLY_INDEX) {
					GameView.wordTimer.timeline.stop();
					twister.twisterRound.setIsRoundComplete(true);
					StringBuilder scroreWriter = new StringBuilder(); //holds the score String to be passed to model's read method 
					scroreWriter.append(WordNerd.TWISTER_ID + ",");
					scroreWriter.append(twister.twisterRound.getPuzzleWord() + ",");
					int timeLeft = Integer.parseInt(GameView.wordTimer.timerButton.getText());
					scroreWriter.append(String.valueOf(120-timeLeft) + ",");
					int guessedWords = 0;
					float possibleWords = twister.twisterRound.getSolutionWordsList().size();

					for (int i = 0; i < twister.twisterRound.getSubmittedListsByWordLength().size(); i++)
					{
						if (twister.twisterRound.getSubmittedListsByWordLength(i).isEmpty())
						{
							guessedWords +=0;
						}
						else
						{
							guessedWords += twister.twisterRound.getSubmittedListsByWordLength(i).size();
						}
					}
					scroreWriter.append(guessedWords/possibleWords);
					wordNerdModel.writeScore(scroreWriter.toString());
				}
			}
			twisterView.topMessageText.setText(twister.getScoreString());

			String newClue = twister.makeAClue(twister.twisterRound.getPuzzleWord());
			for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) 
			{
				twisterView.clueButtons[i].setText(Character.toString(newClue.charAt(i)));
			}
		}
	}
}
