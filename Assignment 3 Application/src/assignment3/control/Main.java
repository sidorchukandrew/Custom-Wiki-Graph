package assignment3.control;
	
import java.io.IOException;
import java.util.PriorityQueue;

import assignment3.model.Graph;
import assignment3.model.Node;
import assignment3.utilities.IOUtilities;
import assignment3.view.WebPageDisplayerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private BorderPane root;
	private static Stage stage;
	private static Graph graph;

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		initializeGraph();
		
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource("../view/RootView.fxml"));
			Scene scene = new Scene(root,1500,900);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			showWebPageDisplayerView();
		} 
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showWebPageDisplayerView()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/WebPageDisplayer.fxml"));
			AnchorPane webPageDisplayer = (AnchorPane) loader.load();
			
			root.setCenter(webPageDisplayer);
			WebPageDisplayerController controller = loader.getController();
			controller.setMain(this);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Stage getStage() {
		
		return stage;
	}
	
	public void initializeGraph() {

		try {
			graph = IOUtilities.readActualGraph("C:\\Users\\Andrew Sidorchuk\\CSC365 Workspace\\Assignment 3 Loader\\serializedTEMP.txt");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Graph getGraph() {
		return graph;
	}
}
