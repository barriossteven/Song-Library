package application;

import java.util.ArrayList;

import application.view.Song;
import application.view.ViewC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/application/view/view.fxml"));
			    ViewC ViewC = loader.getController();
				
				Pane root =  (Pane) loader.load();
				
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("SongLib");
				
				primaryStage.setResizable(false);  
				primaryStage.show();
			
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
