//Name - Vinay Sisodiya
//Andrew ID - vsisodiy
package hw3;

import java.util.List;

import javafx.scene.chart.LineChart;

public class ScoreController extends WordNerdController{


	ScoreView scoreView;

	@Override
	void startController() {
		scoreView = new ScoreView();
		scoreView.setupView();
		WordNerdModel wordNerdModel = new WordNerdModel();
		wordNerdModel.readScore();// reading csv and populating the list

		ScoreChart scoreChart = new ScoreChart();

		List<LineChart<Number,Number>> charts = scoreChart.drawChart(wordNerdModel.scoreList); //creating charts

		//attaching charts to the scoregrid
		scoreView.scoreGrid.add(charts.get(WordNerd.HANGMAN_ID), 0, 1, 2, 1);
		scoreView.scoreGrid.add(charts.get(WordNerd.TWISTER_ID), 0, 2, 2, 1);

		WordNerd.root.setTop(null); //removing the menu bar from the score chart display window
		WordNerd.root.setCenter(scoreView.scoreGrid);//attaching scoregrid to the root
	}

	@Override
	void setupBindings() {


	}
	//not needed


}
