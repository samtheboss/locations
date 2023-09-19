package locations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pops.PopUpModel;
import Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationModel {
  String MAIN_LOCATION;
  String SUB_LOCATION;
  String LOCATION_NAME;
  String POSTAL_ADDRESS;
  String CITY;
  String CONTACTS;
  String TELEPHONE_NUMBERS;
  String LOCATION_TYPE;
  String MAIN_LEVEL;
  String CHECK_OPTION;
  String LOCATION_ZONE;
  Double DISTANCE;
  Double CHARGE_VALUE;
  Double OTHER_VALUE;
  String MODIFIED_BY;
  String SYNC_STATUS;
  Integer BRANCH_REF;
  Integer CHAT_PORT;
  String CHAT_SERVER;
  String GENERAL_TEXT;

  public String getMAIN_LOCATION() {
    return MAIN_LOCATION;
  }

  public void setMAIN_LOCATION(String MAIN_LOCATION) {
    this.MAIN_LOCATION = MAIN_LOCATION;
  }

  public String getSUB_LOCATION() {
    return SUB_LOCATION;
  }

  public void setSUB_LOCATION(String SUB_LOCATION) {
    this.SUB_LOCATION = SUB_LOCATION;
  }

  public String getLOCATION_NAME() {
    return LOCATION_NAME;
  }

  public void setLOCATION_NAME(String LOCATION_NAME) {
    this.LOCATION_NAME = LOCATION_NAME;
  }

  public String getPOSTAL_ADDRESS() {
    return POSTAL_ADDRESS;
  }

  public void setPOSTAL_ADDRESS(String POSTAL_ADDRESS) {
    this.POSTAL_ADDRESS = POSTAL_ADDRESS;
  }

  public String getCITY() {
    return CITY;
  }

  public void setCITY(String CITY) {
    this.CITY = CITY;
  }

  public String getCONTACTS() {
    return CONTACTS;
  }

  public void setCONTACTS(String CONTACTS) {
    this.CONTACTS = CONTACTS;
  }

  public String getTELEPHONE_NUMBERS() {
    return TELEPHONE_NUMBERS;
  }

  public void setTELEPHONE_NUMBERS(String TELEPHONE_NUMBERS) {
    this.TELEPHONE_NUMBERS = TELEPHONE_NUMBERS;
  }

  public String getLOCATION_TYPE() {
    return LOCATION_TYPE;
  }

  public void setLOCATION_TYPE(String LOCATION_TYPE) {
    this.LOCATION_TYPE = LOCATION_TYPE;
  }

  public String getMAIN_LEVEL() {
    return MAIN_LEVEL;
  }

  public void setMAIN_LEVEL(String MAIN_LEVEL) {
    this.MAIN_LEVEL = MAIN_LEVEL;
  }

  public String getCHECK_OPTION() {
    return CHECK_OPTION;
  }

  public void setCHECK_OPTION(String CHECK_OPTION) {
    this.CHECK_OPTION = CHECK_OPTION;
  }

  public String getLOCATION_ZONE() {
    return LOCATION_ZONE;
  }

  public void setLOCATION_ZONE(String LOCATION_ZONE) {
    this.LOCATION_ZONE = LOCATION_ZONE;
  }

  public Double getDISTANCE() {
    return DISTANCE;
  }

  public void setDISTANCE(Double DISTANCE) {
    this.DISTANCE = DISTANCE;
  }

  public Double getCHARGE_VALUE() {
    return CHARGE_VALUE;
  }

  public void setCHARGE_VALUE(Double CHARGE_VALUE) {
    this.CHARGE_VALUE = CHARGE_VALUE;
  }

  public Double getOTHER_VALUE() {
    return OTHER_VALUE;
  }

  public void setOTHER_VALUE(Double OTHER_VALUE) {
    this.OTHER_VALUE = OTHER_VALUE;
  }

  public String getMODIFIED_BY() {
    return MODIFIED_BY;
  }

  public void setMODIFIED_BY(String MODIFIED_BY) {
    this.MODIFIED_BY = MODIFIED_BY;
  }

  public String getSYNC_STATUS() {
    return SYNC_STATUS;
  }

  public void setSYNC_STATUS(String SYNC_STATUS) {
    this.SYNC_STATUS = SYNC_STATUS;
  }

  public Integer getBRANCH_REF() {
    return BRANCH_REF;
  }

  public void setBRANCH_REF(Integer BRANCH_REF) {
    this.BRANCH_REF = BRANCH_REF;
  }

  public Integer getCHAT_PORT() {
    return CHAT_PORT;
  }

  public void setCHAT_PORT(Integer CHAT_PORT) {
    this.CHAT_PORT = CHAT_PORT;
  }

  public String getCHAT_SERVER() {
    return CHAT_SERVER;
  }

  public String getGENERAL_TEXT() {
    return GENERAL_TEXT;
  }

  public void setGENERAL_TEXT(String GENERAL_TEXT) {
    this.GENERAL_TEXT = GENERAL_TEXT;
  }

  public void setCHAT_SERVER(String CHAT_SERVER) {
    this.CHAT_SERVER = CHAT_SERVER;
  }

  public static ObservableList<LocationModel> loadLocations() {
    String sql = "select * from locations where sync_status in('UPD','ACT','NEW')";
    PreparedStatement pst;
    Connection conn = DBConnection.getConnection();
    ResultSet rs;
    ObservableList<LocationModel> data = FXCollections.observableArrayList();
    LocationModel model = new LocationModel();

    try {

      pst = conn.prepareStatement(sql);
      rs = pst.executeQuery();
      while (rs.next()) {
        model.setSUB_LOCATION(rs.getString("SUB_LOCATION"));
        model.setDISTANCE(rs.getDouble("DISTANCE"));
        model.setCHARGE_VALUE(rs.getDouble("CHARGE_VALUE"));
        model.setMAIN_LOCATION(rs.getString("MAIN_LOCATION"));
        model.setTELEPHONE_NUMBERS(rs.getString("TELEPHONE_NUMBERS"));
        model.setBRANCH_REF(rs.getInt("BRANCH_REF"));
        model.setPOSTAL_ADDRESS(rs.getString("POSTAL_ADDRESS"));
        model.setCHECK_OPTION(rs.getString("CHECK_OPTION"));
        model.setLOCATION_NAME(rs.getString("LOCATION_NAME"));
        model.setCITY(rs.getString("CITY"));
        model.setLOCATION_TYPE(rs.getString("LOCATION_TYPE"));
        model.setGENERAL_TEXT(
            rs.getString("GENERAL_TEXT") == null ? "" : rs.getString("GENERAL_TEXT"));
        data.add(model);
        model = new LocationModel();
      }
      DBConnection.closeConnection(conn);
    } catch (SQLException ex) {
      Logger.getLogger(PopUpModel.class.getName()).log(Level.SEVERE, null, ex);
    }
    return data;
  }
}
