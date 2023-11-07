package locations;

import Utils.DBConnection;

import Utils.WhereBy;
import Utils.utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pops.PopUpController;
import pops.PopUpModel;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class LocationController implements Initializable {

  @FXML private JFXButton btn_add;
  @FXML private JFXButton btn_clear;
  @FXML private JFXButton btn_update;

  @FXML private JFXButton btn_delete;

  @FXML private JFXButton btn_refresh;

  @FXML private JFXButton btn_export;

  @FXML private JFXTextField txt_location_code;

  @FXML private JFXTextField txt_location_name;

  @FXML private JFXTextField txt_branch_code;

  @FXML private JFXTextField txt_location_type;

  @FXML private Button btn_loc_type;

  @FXML private JFXTextField txt_city;

  @FXML private Button btn_city;

  @FXML private JFXTextField txt_location_zone;

  @FXML private Button btn_location_zone;

  @FXML private JFXCheckBox chk_stock_control;

  @FXML private JFXTextField txt_telphones;

  @FXML private JFXTextField txt_distance;

  @FXML private JFXTextField txt_charge;
  @FXML private JFXTextField txSearch;

  @FXML private JFXTextField txt_receipt_footer;

  @FXML private TextArea txa_address;
  @FXML private TableView<LocationModel> tableView;
  @FXML private TableColumn<LocationModel, String> tc_lacoation_code;

  @FXML private TableColumn<LocationModel, String> tc_location_name;

  @FXML private TableColumn<LocationModel, String> tc_branch_coode;

  @FXML private TableColumn<LocationModel, String> tc_address;

  @FXML private TableColumn<LocationModel, String> tc_city;

  @FXML private TableColumn<LocationModel, String> tc_stock_control;
    @FXML private TableColumn<LocationModel, String> tc_general_text;
  String locationType;
  String city;
  String zoneArea;
  PopUpModel popUpModel = new PopUpModel();
  DBConnection dbutil = new DBConnection();

  Utils.utils utils = new utils();
  boolean updateOrSave = false;
  String username = "test";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setListeners();
    initTable();
    disable();
  }

  void disable() {
    if (updateOrSave) {
      btn_add.setDisable(true);
      btn_delete.setDisable(false);
      btn_update.setDisable(false);
    } else {
      btn_add.setDisable(false);
      btn_delete.setDisable(true);
      btn_update.setDisable(true);
    }
  }

  void setListeners() {
    txSearch.setOnKeyReleased(
        e -> {
          filter();
        });
    setDataToFields();
    Utils.utils.restrictToNumbersOnly(txt_branch_code, txt_charge, txt_distance);
    Utils.utils.fillWithZeroIfEmpty("0", txt_branch_code, txt_distance, txt_charge);
    btn_add.setOnAction(
        e -> {
          saveOrUpdate();
        });
    chk_stock_control.setOnAction(
        e -> {
          chk_stock_control.setText(chk_stock_control.isSelected() ? "Y" : "N");
        });
    btn_loc_type.setOnAction(
        e -> {
          String sql =
              "SELECT MINOR_CODE AS CODE, DESCRIPTION FROM LIST_CONTROL WHERE "
                  + "REFERENCE_CODE = 'LOCATION_TYPES' AND MAJOR_CODE='SY2'";

          popUpModel = selectCode(this, sql);
          if (popUpModel != null) {
            txt_location_type.setText(popUpModel.getDescription());
            locationType = popUpModel.getCode();
          }
        });

    btn_city.setOnAction(
        e -> {
          String sql =
              "SELECT MINOR_CODE AS CODE, DESCRIPTION from LIST_CONTROL WHERE "
                  + "REFERENCE_CODE = 'TOWNS' and LIST_TYPE=1 and MAJOR_CODE='SY2'";

          popUpModel = selectCode(this, sql);
          if (popUpModel != null) {
            txt_city.setText(popUpModel.getDescription());
            city = popUpModel.getDescription();
            System.out.println(city);
          }
        });
    btn_clear.setOnAction(
        e -> {
          clearFields();
        });
    btn_update.setOnAction(
        e -> {
          saveOrUpdate();
        });
    btn_delete.setOnAction(
        e -> {
          boolean confirm =
              Utils.utils.showConfirmationDialog(
                  "Delete Dialog", "Are you sure you want to Delete?", "");
          if (confirm) {
            String del =
                "update locations set sync_status ='DEL' WHERE MAIN_LOCATION ='"
                    + txt_location_code.getText()
                    + "'";
            DBConnection.updateData(del);
            clearFields();
            initTable();
          }
        });
  }

  void clearFields() {
    txt_city.clear();
    txt_location_type.clear();
    txt_distance.setText("0.0");
    txt_telphones.clear();
    txt_location_code.clear();
    txt_location_name.clear();
    txt_charge.setText("0.0");
    txt_branch_code.clear();
    txt_receipt_footer.clear();
    txa_address.clear();
    txt_location_zone.clear();
    updateOrSave = false;

    txt_location_code.setEditable(true);
    disable();
  }

  private void saveOrUpdate() {

    boolean empty =
        Utils.utils.isAnyTextFieldEmpty(
            txt_branch_code, txt_city, txt_location_code, txt_location_type);
    if (!empty) {
      LocationModel model = new LocationModel();
      Integer branch_code = Integer.valueOf(txt_branch_code.getText());
      String locationName = txt_location_name.getText();
      String locationCode = txt_location_code.getText();
      String postalAddress = txa_address.getText();
      String telephone = txt_telphones.getText();
      String receiptFooter = txt_receipt_footer.getText();
      String stockCheck = chk_stock_control.getText();
      Double distanceFromBase = Double.valueOf(txt_distance.getText());
      Double chargePerKm = Double.valueOf(txt_charge.getText());

      model.setGENERAL_TEXT(receiptFooter);
      model.setBRANCH_REF(branch_code);
      model.setLOCATION_NAME(locationName);
      model.setMAIN_LOCATION(locationCode);
      model.setPOSTAL_ADDRESS(postalAddress);
      model.setTELEPHONE_NUMBERS(telephone);
      model.setDISTANCE(distanceFromBase);
      model.setCHARGE_VALUE(chargePerKm);
      model.setSUB_LOCATION(locationCode);
      model.setCITY(city);
      model.setLOCATION_TYPE(locationType);
      model.setCHECK_OPTION(stockCheck);
      model.setMODIFIED_BY(username);

      String sql = "select count from locations where MAIN_LOCATION='" + locationCode + "' ";
      int count = (int) dbutil.singleValue(sql);
      if (count >= 1 && !updateOrSave) {
        utils.Notification("Failed", "Location Already Exist", "error");
        initTable();
      } else {
        //        List<WhereBy> wheres = new ArrayList<>();
        List<WhereBy> wheres =
            Collections.singletonList(new WhereBy("MAIN_LOCATION", locationCode));
       // wheres.add(new WhereBy("MAIN_LOCATION", locationCode));
        String insertSql = Utils.utils.generateInsertSQL(model, "locations");
        String updateSql = Utils.utils.genUpdateSQL(model, wheres, "locations");
        System.out.println(updateSql);
        if (updateOrSave) {
          DBConnection.updateData(updateSql);
          utils.Notification("Success", "Location update successful", "");
        } else {
          DBConnection.updateData(insertSql);
          utils.Notification("Success", "Location Created successful", "");
        }
        initTable();

        clearFields();
      }
    }
  }

  private void initTable() {
    tc_lacoation_code.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getMAIN_LOCATION())));
    tc_address.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getPOSTAL_ADDRESS())));
    tc_branch_coode.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getBRANCH_REF())));
    tc_location_name.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getLOCATION_NAME())));
    tc_stock_control.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getCHECK_OPTION())));
    tc_city.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getCITY())));
      tc_general_text.setCellValueFactory(
        d -> new ReadOnlyObjectWrapper<>(String.valueOf(d.getValue().getGENERAL_TEXT())));
    tableView.setItems(LocationModel.loadLocations());
  }

  public static PopUpModel selectCode(Initializable controller, String sql) {
    PopUpModel selectPopup = null;
    try {
      FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("/pops/PopUpUi.fxml"));
      loader.setController(new PopUpController(sql));
      Parent parent = loader.load();
      Stage stage = new Stage(StageStyle.DECORATED);
      stage.setTitle("SELECT POPUP");
      stage.setScene(new Scene(parent));
      stage.setResizable(false);
      stage.centerOnScreen();
      stage.setAlwaysOnTop(true);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
      selectPopup = loader.<PopUpController>getController().getSelectedItem();
    } catch (IOException ex) {
      Logger.getLogger(LocationController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return selectPopup;
  }

  public void setDataToFields() {
    tableView.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 2) {
            updateOrSave = true;
            disable();
            LocationModel locationModel = tableView.getSelectionModel().getSelectedItem();
            if (locationModel != null) {
              txa_address.setText(locationModel.getPOSTAL_ADDRESS());
              txt_charge.setText(String.valueOf(locationModel.getCHARGE_VALUE()));
              txt_location_name.setText(locationModel.getLOCATION_NAME());
              txt_location_code.setText(locationModel.getMAIN_LOCATION());
              txt_branch_code.setText(String.valueOf(locationModel.getBRANCH_REF()));
              txt_city.setText(String.valueOf(locationModel.getCITY()));
              txt_telphones.setText(locationModel.getTELEPHONE_NUMBERS());
              txt_distance.setText(String.valueOf(locationModel.getDISTANCE()));
              String ch = locationModel.getCHECK_OPTION();
              chk_stock_control.setSelected(!"N".equals(ch));
              chk_stock_control.setText(chk_stock_control.isSelected() ? "Y" : "N");
              txt_receipt_footer.setText(locationModel.getGENERAL_TEXT());
              String locationType = locationModel.getLOCATION_TYPE();
              String locationTypeName =
                  (String)
                      dbutil.singleValue(
                          "SELECT  DESCRIPTION from LIST_CONTROL WHERE "
                              + "REFERENCE_CODE = 'LOCATION_TYPES' and MAJOR_CODE='SY2' and "
                              + "MINOR_CODE ='"
                              + locationType
                              + "'");

              txt_location_type.setText(locationTypeName);
              txt_location_code.setEditable(false);
            }
          }
        });
  }

  private void filter() {
    FilteredList<LocationModel> filteredData = new FilteredList<>(tableView.getItems(), p -> true);
    txSearch
        .textProperty()
        .addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
              filteredData.setPredicate(
                  locationModel -> {
                    if (newValue == null || newValue.isEmpty()) {
                      return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return Stream.of(
                            locationModel.getLOCATION_NAME(),
                            locationModel.getMAIN_LOCATION(),
                            locationModel.getLOCATION_TYPE(),
                            String.valueOf(locationModel.getBRANCH_REF()))
                        .filter(Objects::nonNull)
                        .anyMatch(value -> value.toLowerCase().contains(lowerCaseFilter));
                  });
            });
    SortedList<LocationModel> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(tableView.comparatorProperty());
    tableView.setItems(sortedData);
  }
}
