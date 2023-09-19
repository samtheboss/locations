/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author smartApps
 */
public class DBConnection {
  private static final BasicDataSource dataSource;

  static {
    dataSource = new BasicDataSource();
    dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
    dataSource.setUrl("jdbc:db2://localhost:50000/lubes");
    dataSource.setUsername("maliplus");
    dataSource.setPassword("Boss@3318");
    dataSource.setInitialSize(5);
    dataSource.setMaxTotal(20);
    dataSource.setMaxIdle(10);
  }

  public static Connection getConnection() {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      System.out.println("Connected");
    } catch (SQLException ex) {
      Logger.getLogger(DBConnection.class.getName())
          .log(Level.SEVERE, "Error getting a connection", ex);
    }
    return conn;
  }

  public static void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException ex) {
        Logger.getLogger(DBConnection.class.getName())
            .log(Level.SEVERE, "Error closing the connection", ex);
      }
    }
  }

  public Object singleValue(String sql) {
    Object name = "";
    Connection dbCon = getConnection();
    try {
      PreparedStatement pst = dbCon.prepareStatement(sql);
      ResultSet result = pst.executeQuery();
      while (result.next()) {
        name = result.getObject(1);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return name;
  }

  public static void updateData(String sql) {

    try {
      Connection connection = getConnection();
      Statement statement = connection.createStatement();
      int rowsAffected = statement.executeUpdate(sql);
      if (rowsAffected >= 1) {
        System.out.println("Update successful. " + rowsAffected + " rows affected.");
        closeConnection(connection);
      }

    } catch (SQLException e) {
      System.out.println(e.getLocalizedMessage());
    }
  }
}
