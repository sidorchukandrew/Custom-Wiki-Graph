package assignment3.view;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import assignment3.control.GraphUtilities;
import assignment3.control.Main;
import assignment3.model.Edge;
import assignment3.model.Node;
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
	private LineChart pathChart;
	private Label websiteLabelForHover;
	private XYChart.Series series;
	
	private Main main;
	
	ObservableList<String> observableSitesOne;
	ObservableList<String> observableSitesTwo;

	@FXML
	public void initialize() {
		
		ArrayList<String> websites = GraphUtilities.getAllWebsites(Main.getGraphRoot());
		
		for(Edge e : Main.getGraphRoot().getEdges()) {
			websites.remove(e.getDst().getName());
			websites.add(0, e.getDst().getName());
		}
			
		websites.remove(Main.getGraphRoot().getName());
		websites.add(0, Main.getGraphRoot().getName());
		observableSitesOne = FXCollections.observableArrayList(websites);
		sitesOne.setItems(observableSitesOne);		
		sitesOne.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
//				System.out.println(sitesOne.getSelectionModel().getSelectedItem());
			}
		});
		
		observableSitesTwo = FXCollections.observableArrayList(websites);
		sitesTwo.setItems(observableSitesTwo);
		sitesTwo.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
//				System.out.println(sitesTwo.getSelectionModel().getSelectedItem());
			}
		});
		
		findButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				series.getData().clear();
				Node src = GraphUtilities.find(Main.getGraphRoot(), sitesOne.getSelectionModel().getSelectedItem().toString());
				Stack<String> path = GraphUtilities.dijkstra(src, sitesTwo.getSelectionModel().getSelectedItem());
				
				int x = 1;
				float y = 4.5f;
				
				if(path.isEmpty())
					websiteLabelForHover.setText("No path exists with this graph");
				
				while(!path.isEmpty()) { 

					String site = path.pop();
					
					Random r = new Random();
					XYChart.Data dataPoint = new XYChart.Data(x, y, site);
					series.getData().add(dataPoint);
					
					dataPoint.getNode().setOnMouseEntered(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							websiteLabelForHover.setText("Website : " + dataPoint.getExtraValue());
						}
					});
					
					dataPoint.getNode().setId("chart-symbol");
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
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
	private void handleCloseAppPressed(ActionEvent e) {
		main.getStage().close();
	}

}
