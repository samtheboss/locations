/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utils.DBConnection;
/**
 * @author samue
 */
public class PopUpModel {

  String Description;
  String code;


  public String getDescription() {
    return Description;
  }

  public void setDescription(String Description) {
    this.Description = Description;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public static ObservableList<PopUpModel> loadAccounts(String sql) {
    PreparedStatement pst;
    Connection conn = DBConnection.getConnection();
    ResultSet rs;
    ObservableList<PopUpModel> data = FXCollections.observableArrayList();
    PopUpModel model = new PopUpModel();

    try {

      pst = conn.prepareStatement(sql);
      rs = pst.executeQuery();
      while (rs.next()) {
        model.setDescription(rs.getString("description"));
        model.setCode(rs.getString("code"));
        data.add(model);
        model = new PopUpModel();
      }

    } catch (SQLException ex) {
      Logger.getLogger(PopUpModel.class.getName()).log(Level.SEVERE, null, ex);
    }
    return data;
  }
}
