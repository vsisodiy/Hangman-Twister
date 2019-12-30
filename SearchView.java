//Name - Vinay Sisodiya
//Andrew ID - vsisodiy
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SearchView{

	ComboBox<String> gameComboBox = new ComboBox<>(); //shows drop down for filtering the tableView data
	TextField searchTextField = new TextField();  //for entering search letters
	TableView<Score> searchTableView = new TableView<>();  //displays data from scores.csv
	WordNerdModel wordNerdModel = new WordNerdModel();

	//CallBack for name of the game - displays name corresponding to the gameId
	Callback <CellDataFeatures<Score, String>, ObservableValue<String>> gameNameCallBack = 
			new Callback <CellDataFeatures<Score, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Score, String> arg0) {
			if (arg0.getValue().getGameId() == 0)
				return new SimpleStringProperty("Hangman");
			else
				return new SimpleStringProperty("Twister");						
		}};

    //CallBack for name of the score - rounds score value to 2 decimal points
	Callback <CellDataFeatures<Score, String>, ObservableValue<String>> scoreCallBack = 
			new Callback <CellDataFeatures<Score, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Score, String> arg0) {
			return new SimpleStringProperty(String.valueOf(Math.round(arg0.getValue().getScore()*100.0)/100.0));
		}
	};

	/**setupView() sets up the GUI components
	 * for Search functionality
	 */
	void setupView() {

		VBox searchVBox = new VBox();  //searchVBox contains searchLabel and searchHBox
		Text searchLabel = new Text("Search");
		searchVBox.getChildren().add(searchLabel);

		HBox searchHBox = new HBox();  //searchHBox contain gameComboBox and searchTextField
		searchHBox.getChildren().add(gameComboBox);
		searchHBox.getChildren().add(new Text("Search letters"));
		searchHBox.getChildren().add(searchTextField);
		searchVBox.getChildren().add(searchHBox);

		searchLabel.setStyle( "-fx-font: 30px Tahoma;" + 
				" -fx-fill: linear-gradient(from 0% 50% to 50% 100%, repeat, lightgreen 0%, lightblue 50%);" +
				" -fx-stroke: gray;" +
				" -fx-background-color: gray;" +
				" -fx-stroke-width: 1;") ;
		searchHBox.setPrefSize(WordNerd.GAME_SCENE_WIDTH, WordNerd.GAME_SCENE_HEIGHT / 3);
		gameComboBox.setPrefWidth(200);
		searchTextField.setPrefWidth(300);
		searchHBox.setAlignment(Pos.CENTER);
		searchVBox.setAlignment(Pos.CENTER);
		searchHBox.setSpacing(10);

		setupSearchTableView();

		WordNerd.root.setPadding(new Insets(10, 10, 10, 10));
		WordNerd.root.setTop(searchVBox);
		WordNerd.root.setCenter(searchTableView);
		WordNerd.root.setBottom(WordNerd.exitButton);
	}

	@SuppressWarnings("unchecked")
	void setupSearchTableView() {
		searchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //ensuring only 4 columns

		//defining table view cloumns and setting their property value factory
		TableColumn<Score, String> column1 = new TableColumn<>("Game");
		column1.setCellValueFactory(gameNameCallBack);

		TableColumn<Score, String> column2 = new TableColumn<>("Word");
		column2.setCellValueFactory(
				new PropertyValueFactory<Score, String>("puzzleWord"));

		TableColumn<Score, Integer> column3 = new TableColumn<>("Time(sec)");
		column3.setCellValueFactory(
				new PropertyValueFactory<Score, Integer>("timeStamp"));

		TableColumn<Score, String> column4 = new TableColumn<>("Score");
		column4.setCellValueFactory(scoreCallBack);


		wordNerdModel.readScore(); //reading the csv in score observable list
		//setting data
		searchTableView.setItems(wordNerdModel.scoreList); 
		searchTableView.getColumns().setAll(column1, column2, column3, column4);
	}
}
