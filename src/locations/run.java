/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locations;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author samue
 */
public class run extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    String path = "LocationUI.fxml";
    loadUi(path, new LocationController());
  }

  void loadUi(String path, Object object) {
    try {
      FXMLLoader loader = new FXMLLoader(this.getClass().getResource(path));

      loader.setController(object);
      Parent parent = loader.load();
      Stage stage = new Stage(StageStyle.DECORATED);
      stage.setScene(new Scene(parent));
      stage.show();
    } catch (IOException ex) {
      Logger.getLogger(run.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
