package assignment3.view;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import assignment3.control.GraphUtilities;
import assignment3.control.Main;
import assignment3.model.Edge;
import assignment3.model.Node;
import assignment3.utilities.IOUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class WebPageDisplayerController 
{

	@FXML private ListView<String> sitesOne;
	@FXML private ListView<String> sitesTwo;
	@FXML private Button closeAppButton;
	@FXML private Button findButton;
	@FXML private AnchorPane pane;
	@FXML private Label medoidOne;  
	@FXML private Label medoidTwo;
	@FXML private Label medoidThree;
	@FXML private Label medoidFour;
	@FXML private Label medoidFive;
	@FXML private CheckBox isMedoidOne;
	@FXML private CheckBox isMedoidTwo;
	@FXML private CheckBox isMedoidThree;
	@FXML private CheckBox isMedoidFour;
	@FXML private CheckBox isMedoidFive;
	@FXML private Button revert;
	@FXML private Button substitute;
	private LineChart pathChart;
	private Label websiteLabelForHover;
	private XYChart.Series series;
	
	private Main main;
	
	ObservableList<String> observableSitesOne;
	ObservableList<String> observableSitesTwo;

	@FXML
	public void initialize() {
		
		ArrayList<String> medoids = IOUtilities.readMedoids("C:\\Users\\Andrew Sidorchuk\\CSC365 Workspace\\Assignment 3 Application\\medoids.txt");
		ArrayList<String> subMedoids = IOUtilities.readMedoids("C:\\Users\\Andrew Sidorchuk\\CSC365 Workspace\\Assignment 3 Application\\subMedoids.txt");
		setMedoids(medoids);
		revert.setDisable(true);
		
		ArrayList<String> websites = GraphUtilities.getAllWebsites(Main.getGraphRoot());
		
		for(Edge e : Main.getGraphRoot().getEdges()) {
			websites.remove(e.getDst().getName());
			websites.add(0, e.getDst().getName());
		}
			
		websites.remove(Main.getGraphRoot().getName());
		websites.add(0, Main.getGraphRoot().getName());
		observableSitesOne = FXCollections.observableArrayList(websites);
		sitesOne.setItems(observableSitesOne);		

		observableSitesTwo = FXCollections.observableArrayList(websites);
		sitesTwo.setItems(observableSitesTwo);
		
		findButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				series.getData().clear();
				clearCheckBoxes();
				Node src = GraphUtilities.find(Main.getGraphRoot(), sitesOne.getSelectionModel().getSelectedItem().toString());
				Stack<String> path = GraphUtilities.dijkstra(src, sitesTwo.getSelectionModel().getSelectedItem());
				
				int x = 1;
				float y = 4.5f;
				
				if(path.isEmpty())
					websiteLabelForHover.setText("No path exists with this graph");
				
				while(!path.isEmpty()) { 
					
					boolean medoidPassed = false;
					String site = path.pop();
					
					if(site.compareTo(medoidOne.getText()) == 0) {
						isMedoidOne.setSelected(true);
						medoidPassed = true;
					}
					else if(site.compareTo(medoidTwo.getText()) == 0) {
						isMedoidTwo.setSelected(true);
						medoidPassed = true;
					}
					else if(site.compareTo(medoidThree.getText()) == 0) {
						isMedoidThree.setSelected(true);
						medoidPassed = true;
					}
					else if(site.compareTo(medoidFour.getText()) == 0) {
						isMedoidFour.setSelected(true);
						medoidPassed = true;
					}
					else if(site.compareTo(medoidFive.getText()) == 0) {
						isMedoidFive.setSelected(true);
						medoidPassed = true;
					}
					
					Random r = new Random();
					XYChart.Data dataPoint = new XYChart.Data(x, y, site);
					series.getData().add(dataPoint);
					
					dataPoint.getNode().setOnMouseEntered(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							websiteLabelForHover.setText("Website : " + dataPoint.getExtraValue());
						}
					});
					
					if(medoidPassed)
						dataPoint.getNode().setStyle("-fx-stroke: red;"
								+ " -fx-background-radius: 20px ;\r\n" + 
								"    -fx-padding: 20px ;");
					else
						dataPoint.getNode().setStyle(" -fx-background-radius: 10px ;\r\n" + 
								"    -fx-padding: 10px ;");
					
					Tooltip tooltip = new Tooltip();
					tooltip.setText(dataPoint.getExtraValue().toString());
					Tooltip.install(dataPoint.getNode(), tooltip);
					
					x++;
					y = r.nextInt((int) y) + 1f;
					
				}
			}
		});
		
		NumberAxis yAxis = new NumberAxis(0, 5, 1);
		NumberAxis xAxis = new NumberAxis(0, 5, 1);
		pathChart = new LineChart<>(xAxis, yAxis);
		pathChart.setTitle("Path");
		pathChart.setLegendVisible(false);
		pathChart.setPrefWidth(550);
		pathChart.setPrefHeight(600);
		pathChart.setLayoutX(813);
		
		pane.getChildren().add(pathChart);
		pane.setTopAnchor(pathChart, 30.0);
		
		series = new XYChart.Series();
		pathChart.getData().add(series);
        
        websiteLabelForHover = new Label("Website : ");
        pane.getChildren().add(websiteLabelForHover);
        websiteLabelForHover.setLayoutX(825);
        pane.setTopAnchor(websiteLabelForHover, pathChart.getPrefHeight() + 40);
        
        substitute.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setMedoids(subMedoids);
				substitute.setDisable(true);
				revert.setDisable(false);
			}
		});
        
        revert.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setMedoids(medoids);
				substitute.setDisable(false);
				revert.setDisable(true);
			}
        	
        });
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
	private void handleCloseAppPressed(ActionEvent e) {
		main.getStage().close();
	}
	
	private void clearCheckBoxes() {
		isMedoidOne.setSelected(false);
		isMedoidTwo.setSelected(false);
		isMedoidThree.setSelected(false);
		isMedoidFour.setSelected(false);
		isMedoidFive.setSelected(false);
	}
	
	private void setMedoids(ArrayList<String> medoids) {
		medoidOne.setText(medoids.get(0));
		medoidTwo.setText(medoids.get(1));
		medoidThree.setText(medoids.get(2));
		medoidFour.setText(medoids.get(3));
		medoidFive.setText(medoids.get(4));
	}

}
