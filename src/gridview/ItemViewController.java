package gridview;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import locations.run;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemViewController implements Initializable {
  @FXML private Label lblOrderNumber;
  @FXML private Label lblOrderDate;
  @FXML private Label lblQuantity;
  @FXML private Label lblITotalSalePrice;
  @FXML private Label lblOrderDescription;
  @FXML private HBox hbOrderNumber;

   Double itemCount;
   Double orderAmount;
   String orderDate;
   Integer orderNumber;
   String order_Description;
  OrderDataModel model;

  public ItemViewController(
      Double itemCount,
      Double orderAmount,
      String orderDate,
      Integer orderNumber,
      String order_Description) {
    this.orderNumber = orderNumber;
    this.itemCount = itemCount;
    this.orderAmount = orderAmount;
    this.orderDate = orderDate;
    this.order_Description = order_Description;
  }


  public ItemViewController(OrderDataModel model) {
    this.model =model;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setListeners();
  }

  void setListeners() {
//    lblITotalSalePrice.setText(model.getOrderAmount().toString());
//    lblOrderDate.setText(model.getOrderDate());
//    lblOrderNumber.setText(model.getOrderNumber().toString());
//    lblQuantity.setText(model.getOrderNumber().toString());
//    lblOrderDescription.setText(model.getOrderDescription());

    lblITotalSalePrice.setText(orderAmount.toString());
    lblOrderDate.setText(orderDate);
    lblOrderNumber.setText(orderNumber.toString());
    lblQuantity.setText(itemCount.toString());
    lblOrderDescription.setText(order_Description);



    hbOrderNumber.setOnMouseClicked(
        e -> {
         // System.out.println(model.getOrderNumber());
          String path = "/gridview/loaded.fxml";
          run.loadUi(path, new LoadedController(model),this);
        });
  }
}
