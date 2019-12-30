//Name - Vinay Sisodiya
//Andrew ID - vsisodiy

package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WordNerdModel {

	public static String[] wordsFromFile;
	
	public static final String SCORE_FILE_NAME = "data\\scores.csv";

	public static final String WORDS_FILE_NAME = "data\\miniWordsFile.txt"; //to be used across classes
	
	ObservableList<Score> scoreList;

	@SuppressWarnings("resource")
	static boolean readWordsFile(String wordsFileName)
	{
		boolean flag = false; //this flag tells whether the file was succesfully read
		StringBuilder fileContent = new StringBuilder();
		Scanner fileScanner = null;
		try 
		{
			File file = new File(wordsFileName);
			if (file.length() == 0) throw (new InvalidWordSourceException("Check word source format!")); //if file is empty
			
			fileScanner = new Scanner(file);
			while (fileScanner.useDelimiter("\n").hasNext())
			{
				String token = fileScanner.next().trim();
				if (token.matches("[a-zA-Z]+"))
					fileContent.append(token + "\n");
				else throw (new InvalidWordSourceException("Check word source format!")); //if any of the word is of bad format
			}
			
			wordsFromFile = fileContent.toString().split("\n"); //if all of the above steps ran successfully then only wordsFromFile is populated else it remains the same as previous
			flag = true; //if all of the above steps ran successfully then only flag is set to true
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (InvalidWordSourceException e1)
		{
			e1.showAlert();
		}
		
		return flag;
	}
	
	void writeScore(String scoreString)
	{
		try {
			BufferedWriter outputfile = new BufferedWriter(new FileWriter(SCORE_FILE_NAME, true));// ensuring it is not overwritten
			outputfile.write(scoreString);
			outputfile.write("\n");
			outputfile.close();
		}
		 catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e1) { 
	        e1.printStackTrace(); 
	    }
	}
	
	//Reads the csv and populates the observable list
	void readScore()
	{
		scoreList = FXCollections.observableArrayList();
		
		StringBuilder fileContent = new StringBuilder();
		Scanner fileScanner = null;
		try 
		{
			File file = new File(SCORE_FILE_NAME);
			
			fileScanner = new Scanner(file);
			while (fileScanner.useDelimiter("\n").hasNext())
			{
				fileContent.append(fileScanner.next().trim() + "\n");
			}
			
			String[] scoreAll = fileContent.toString().split("\n");
			for (String s: scoreAll)
			{
				//creating score object from each row
				int gameId = Integer.parseInt(s.split(",")[0].trim());
				int time = Integer.parseInt(s.split(",")[2].trim());
				float roundScore = Float.parseFloat(s.split(",")[3].trim());
				Score score = new Score(gameId, s.split(",")[1].trim(), time, roundScore);
				scoreList.add(score);
			}
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		
	}

}
