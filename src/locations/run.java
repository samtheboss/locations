/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locations;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import gridview.ViewOrderController;
import gridview.ViewOrderController2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.util.*;
import javax.swing.Timer;

/**
 * @author samue
 */
public class run extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    String path = "/gridview/viewOrder2.fxml";
    loadUi1(path, new ViewOrderController());
  }

  public static void loadUi(String path, Object object, Initializable controller) {
    try {
      FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(path));

      loader.setController(object);
      Parent parent = loader.load();
      Stage stage = new Stage(StageStyle.DECORATED);
      stage.setScene(new Scene(parent));
      stage.show();
    } catch (IOException ex) {
      Logger.getLogger(run.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  public  void loadUi1(String path, Object object) {
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
