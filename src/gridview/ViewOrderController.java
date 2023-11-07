package gridview;

import Utils.DBConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ViewOrderController implements Initializable {
  PreparedStatement pst;
  Connection conn = DBConnection.getConnection();
  ResultSet rs;
  @FXML private TextField filter;

  @FXML private VBox flow_menu_pane;

  @FXML private ScrollPane scrollpane;

  @FXML private FlowPane flowPane;
  @FXML private Label lblSumOrderAmount;
  @FXML private Button btnPrevPage;
  @FXML private Button btnNextPage;
  @FXML private JFXTextField txtPageNumber;
  private static final Logger LOG = Logger.getLogger(ViewOrderController.class.getName());

  ObservableList<OrderDataModel> order_data = FXCollections.observableArrayList();
  ObservableList<OrderDataModel> filtered_data = FXCollections.observableArrayList();
  OrderDataModel model;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setListeners();
  }

  void setListeners() {
    filter.setOnKeyReleased(
        event -> {
          String s = filter.getText();
          if (s != null && !s.isEmpty()) filter(s);
          else drawUi(order_data);
        });
    new Thread(
            () -> {
              Platform.runLater(
                  () -> {
                    loadDataFromDb("ACT", "cash_orders", 0);
                    drawUi(order_data);
                    System.out.println("drawing ui");
                  });
            })
        .start();
    btnNextPage.setOnAction(
        e -> {
          loadPage(true);
        });
    btnPrevPage.setOnAction(
        e -> {
          loadPage(false);
        });
  }

  void loadPage(boolean nextPage) {
    if (!(txtPageNumber.getText() == null && txtPageNumber.getText().isEmpty())) {
      int currentPage = Integer.parseInt(txtPageNumber.getText());
      int page = nextPage ? currentPage + 1 : currentPage - 1;
      txtPageNumber.setText(String.valueOf(page));
      order_data.clear();
      flowPane.getChildren().clear();
      loadDataFromDb("ACT", "cash_orders", page);
      drawUi(order_data);
    }
  }

  private void drawUi(List<OrderDataModel> data) {

    // loadDataFromDb("ACT");
    flowPane.getChildren().clear();
    double amount = 0;
    for (int i = 0; i < data.size(); i++) {
      try {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/gridview/item_view.fxml"));
        loader.setController(
            new ItemViewController(
                data.get(i).getItemCount(),
                data.get(i).getOrderAmount(),
                data.get(i).getOrderDate(),
                data.get(i).getOrderNumber(),
                data.get(i).getOrderDescription()));

        //        model.setOrderNumber(order_data.get(i).getOrderNumber());
        //    loader.setController(new ItemViewController(model));
        HBox root = loader.load();

        amount = amount + data.get(i).getOrderAmount();
        root.setUserData(loader);
        flowPane.getChildren().add(root);
      } catch (IOException ex) {
        LOG.log(Level.INFO, null, ex);
      }
    }
    lblSumOrderAmount.setText(String.valueOf(amount));
  }

  private void loadDataFromDb(String ORDER_STATUS, String table, int page) {

    // String sql = "SELECT * FROM CASH_ORDERS WHERE ORDER_STATUS = '" + ORDER_STATUS + "' limit
    // 500";
    String sql =
        "SELECT * FROM "
            + table
            + " WHERE ORDER_STATUS = ? order by order_number limit 50 offset   "
            + page * 50;
    try {
      pst = conn.prepareStatement(sql);
      pst.setString(1, ORDER_STATUS);
      rs = pst.executeQuery();
      OrderDataModel orm = new OrderDataModel();
      while (rs.next()) {
        orm.setOrderAmount(rs.getDouble("order_amount"));
        orm.setItemCount(rs.getDouble("order_count"));
        orm.setOrderDescription(rs.getString("DESCRIPTION"));
        orm.setOrderNumber(rs.getInt("order_number"));
        orm.setOrderDate(rs.getString("order_date"));
        order_data.add(orm);
        orm = new OrderDataModel();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void filter(String str) {
    // order_data.clear();
    String s = str.toLowerCase();
    drawUi(
        order_data.stream()
            .filter(
                o ->
                    String.valueOf(o.getOrderNumber()).contains(s)
                        || o.orderDescription.toLowerCase().contains(s)
                        || String.valueOf(o.getOrderAmount()).contains(s))
            .collect(Collectors.toList()));
  }

  private List<OrderDataModel> listOfDataFromDb(String ORDER_STATUS) {
    List<OrderDataModel> data = new ArrayList<>();
    String sql = "SELECT * FROM CASH_ORDERS WHERE ORDER_STATUS = ? LIMIT 100";

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
      pst.setString(1, ORDER_STATUS);
      ResultSet rs = pst.executeQuery();

      while (rs.next()) {
        OrderDataModel order = new OrderDataModel();
        order.setOrderAmount(rs.getDouble("order_amount"));
        order.setItemCount(rs.getDouble("order_count"));
        order.setOrderDescription(rs.getString("DESCRIPTION"));
        order.setOrderNumber(rs.getInt("order_number"));
        order.setOrderDate(rs.getString("order_date"));
        data.add(order);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return data;
  }

  private void drawUi2() {
    double amount = 0;
    flowPane.getChildren().clear();

    for (OrderDataModel order : order_data) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gridview/item_view.fxml"));
        loader.setController(
            new ItemViewController(
                order.getItemCount(),
                order.getOrderAmount(),
                order.getOrderDate(),
                order.getOrderNumber(),
                order.getOrderDescription()));
        flowPane.getChildren().add(loader.load());

        amount += order.getOrderAmount();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    lblSumOrderAmount.setText(String.valueOf(amount));
  }
}
