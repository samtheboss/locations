/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pops;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author samue
 */
public class PopUpController implements Initializable {

  @FXML private Button btnSearch;
  @FXML private TableView<PopUpModel> tableView;
  @FXML private TableColumn<?, ?> Colcode;
  @FXML private JFXTextField txSearch;
  @FXML private TableColumn<?, ?> ColDescription;
  PopUpModel popmodel = new PopUpModel();

  String sql;

  public PopUpController(String sql) {
    this.sql = sql;
  }

  /** Initializes the controller class. */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO

    tableView.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 2) {
            getRow();
          }
        });
    txSearch.setOnKeyReleased(
        e -> {
          filter();
        });

    initTable();
  }

  private void getRow() {
    if (!tableView.getSelectionModel().getSelectedItem().getDescription().isEmpty()) {
      popmodel = tableView.getSelectionModel().getSelectedItem();
      closeWindow();
    }
  }

  public PopUpModel getSelectedItem() {
    return popmodel;
  }

  private void closeWindow() {
    ((Stage) tableView.getScene().getWindow()).close();
  }

  public void initTable() {
    Colcode.setCellValueFactory(new PropertyValueFactory("code"));
    ColDescription.setCellValueFactory(new PropertyValueFactory("Description"));
    new Thread(
            () -> {
              ObservableList<PopUpModel> list = PopUpModel.loadAccounts(sql);
              Platform.runLater(
                  () -> {
                    tableView.setItems(list);
                  });
            })
        .start();
  }

  private void filter() {
    FilteredList<PopUpModel> filteredData = new FilteredList<>(tableView.getItems(), p -> true);
    txSearch
        .textProperty()
        .addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
              filteredData.setPredicate(
                  popupmodel -> {
                    if (newValue == null || newValue.isEmpty()) {

                      return true;

                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return Stream.of(popupmodel.getCode(), popupmodel.getDescription())
                        .filter(Objects::nonNull)
                        .anyMatch(value -> value.toLowerCase().contains(lowerCaseFilter));
                  });
            });
    SortedList<PopUpModel> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(tableView.comparatorProperty());
    tableView.setItems(sortedData);
  }
  public void reflection(){

  }
}

