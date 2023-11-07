package gridview;

import Utils.DBConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
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

public class ViewOrderController2 implements Initializable {
  @FXML private TextField filter;
  @FXML private VBox flow_menu_pane;
  @FXML private Label lblSumOrderAmount;
  @FXML private Pagination pgItems;

  @FXML private Button btnPrevPage;
  @FXML private Button btnNextPage;

  @FXML private JFXTextField txtPageNumber;
  private final List<OrderDataModel> orderData = new ArrayList<>();
  Connection conn = DBConnection.getConnection();
  int pageSize = 40;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setListeners();
    loadDataFromDbAsync("PST", "CORDER_HISTORY", 0);
  }

  private void setListeners() {
    filter.setOnKeyReleased(
        e -> {
          filter("PST", "CORDER_HISTORY",0);
        });
    btnNextPage.setOnAction(
        e -> {
          if (!(txtPageNumber.getText() == null && txtPageNumber.getText().equals(""))) {
            int page = Integer.parseInt(txtPageNumber.getText()) + 1;
            loadDataFromDbAsync("PST", "CORDER_HISTORY", page);
          }
        });
  }

  private void loadDataFromDbAsync(String ORDER_STATUS, String table, int page) {
 loadDataFromDb(ORDER_STATUS, table, page).clear();
    Task<List<OrderDataModel>> loadDataTask =
        new Task<List<OrderDataModel>>() {
          @Override
          protected List<OrderDataModel> call() {
            return loadDataFromDb(ORDER_STATUS, table, page);
          }
        };

    loadDataTask.setOnSucceeded(
        event -> {
          orderData.addAll(loadDataTask.getValue());
          initItemsPage();
        });

    loadDataTask.setOnFailed(
        event -> {
          Throwable exception = loadDataTask.getException();
          if (exception != null) {
            exception.printStackTrace();
          }
        });

    new Thread(loadDataTask).start();
  }

  private List<OrderDataModel> loadDataFromDb(String ORDER_STATUS, String table, int page) {
    List<OrderDataModel> data = new ArrayList<>();
    String sql =
        "SELECT * FROM "
            + table
            + " WHERE ORDER_STATUS = ? order by order_number limit 200 offset   "
            + page * 200;

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
      pst.setString(1, ORDER_STATUS);
      ResultSet rs = pst.executeQuery();

      while (rs.next()) {
        OrderDataModel order = new OrderDataModel();
        order.setOrderAmount(rs.getDouble("ORDER_AMOUNT"));
        order.setItemCount(rs.getDouble("ORDER_COUNT"));
        order.setOrderDescription(rs.getString("DESCRIPTION"));
        order.setOrderNumber(rs.getInt("ORDER_NUMBER"));
        order.setOrderDate(rs.getString("ORDER_DATE"));
        data.add(order);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return data;
  }

  private void initItemsPage() {

    pgItems.setPageCount(orderData.size() / pageSize + 1);

    // pgItems.setStyle("-fx-border-color:red;");
    pgItems.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
    pgItems.setPageFactory(this::createPage);
  }

  private ScrollPane createPage(int pageIndex) {
    int fromIndex = pageIndex * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, orderData.size());
    ObservableList<OrderDataModel> subItems =
        FXCollections.observableList(orderData.subList(fromIndex, toIndex));
    ScrollPane box = new ScrollPane();
    box.setFitToHeight(true);
    box.setFitToWidth(true);
    box.setPadding(new Insets(5, 5, 5, 5));
    TilePane tlPane = new TilePane();
    tlPane.setHgap(5);
    tlPane.setVgap(5);
    tlPane.getChildren().clear();
    Node[] nodes = new Node[subItems.size()];
    for (int i = 0; i < subItems.size(); i++) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gridview/item_view.fxml"));
        loader.setController(new ItemViewController(subItems.get(i)));
        nodes[i] = loader.load();
        tlPane.getChildren().add(nodes[i]);
      } catch (IOException ex) {
        Logger.getLogger(ViewOrderController2.class.getName()).log(Level.WARNING, null, ex);
      }
    }

    box.setContent(tlPane);

    return box;
  }

  private void filter(String orderStatus, String table,int page) {
    orderData.clear();

    loadDataFromDb(orderStatus, table,page).stream()
        .filter(
            (task) -> {
              filter.getText();
              return false;
            })
        .forEachOrdered(
            (task) -> {
              orderData.add(task);
              System.out.println("filter");
            });
  }
}
