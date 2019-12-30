//Name - Vinay Sisodiya
//Andrew ID - vsisodiy
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class SearchController extends WordNerdController {


	SearchView searchView;
	ObservableList<String>  gameNameList; // for names in the combobox



	@Override
	void startController() {
		searchView = new SearchView(); 
		searchView.setupView(); //setting up view
		setupBindings(); //setting up bindings
	}

	@Override
	void setupBindings() {
		gameNameList = FXCollections.observableArrayList();
		gameNameList.add("All games");
		gameNameList.add("Hangman");
		gameNameList.add("Twister");

		WordNerdModel wordNerdModel = new WordNerdModel();
		wordNerdModel.readScore();

		searchView.gameComboBox.setItems(gameNameList); //setting values in combo box

		ObservableList<Score> selectedScoreList = FXCollections.observableArrayList(); // to hold filtered data based on the selection and search string

		searchView.gameComboBox.setValue(gameNameList.get(0)); //selecting initial value to all games

		searchView.gameComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedScoreList.clear();
			int index = searchView.gameComboBox.getSelectionModel().getSelectedIndex(); 
			if (searchView.searchTextField.getText().equals("")) // if the search text filed is empty
			{
				if (index == 0) //if it is "all games" then add all the scores
				{		    	
					selectedScoreList.addAll(wordNerdModel.scoreList);
				}
				else
				{
					for (Score s: wordNerdModel.scoreList)
					{
						if (s.getGameId() == index - 1) //if a game is selected then add score that have the corresponding game id
							selectedScoreList.add(s);
					}
				}
			}
			else
			{
				String lowerCaseFilter = searchView.searchTextField.getText().toLowerCase();
				ObservableList<Score> filterdScoreList = FXCollections.observableArrayList();
				for(Score s: wordNerdModel.scoreList)
				{
					if (checkPresence(s.getPuzzleWord().toLowerCase(), lowerCaseFilter)) //calling check presence method to ensure that all the letters are present
					{
						filterdScoreList.add(s); // creating a new list to hold filtered values based on the search string already present before selection
					}
				}

				//same process as above but on filtered data
				if (index == 0)
				{		    	
					selectedScoreList.addAll(filterdScoreList);
				}
				else
				{
					for (Score s: filterdScoreList)
					{
						if (s.getGameId() == index - 1)
							selectedScoreList.add(s);
					}
				}

			}
			searchView.searchTableView.setItems(selectedScoreList); //adding the final data to the table view
		});

		FilteredList<Score> filteredData = new FilteredList<>(selectedScoreList, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		searchView.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			selectedScoreList.clear(); // clearing the list
			int index = searchView.gameComboBox.getSelectionModel().getSelectedIndex();
			
			//following exact same procedure as above
			if (newValue.equals(""))
			{
				if (index == 0)
				{		    	
					selectedScoreList.addAll(wordNerdModel.scoreList);
				}
				else
				{
					for (Score s: wordNerdModel.scoreList)
					{
						if (s.getGameId() == index - 1)
							selectedScoreList.add(s);
					}
				}
			}
			else
			{
				String lowerCaseFilter = newValue.toLowerCase();
				ObservableList<Score> filterdScoreList = FXCollections.observableArrayList();
				for(Score s: wordNerdModel.scoreList)
				{
					if (checkPresence(s.getPuzzleWord().toLowerCase(), lowerCaseFilter))
					{
						filterdScoreList.add(s);
					}
				}

				if (index == 0)
				{		    	
					selectedScoreList.addAll(filterdScoreList);
				}
				else
				{
					for (Score s: filterdScoreList)
					{
						if (s.getGameId() == index - 1)
							selectedScoreList.add(s);
					}
				}

			}

			searchView.searchTableView.setItems(filteredData);
		});

	}

	// check presence method checks if all the distinct letters from the search-string are present in the puzzleWord
	boolean checkPresence(String word, String searchString)
	{
		boolean flag = true;
		for (int i = 0; i < searchString.length(); i++)
		{
			if (!word.contains(String.valueOf(searchString.charAt(i))))
			{
				flag = false; break;
			}
			else flag = true;			

		}
		return flag;
	}

}
