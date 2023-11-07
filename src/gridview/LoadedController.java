package gridview;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadedController implements Initializable {
    @FXML
    private  Label ORDER_NUMBER;
    String orderNumber;
    OrderDataModel orderDataModel ;

    public LoadedController(OrderDataModel orderDataModel) {
        this.orderDataModel = orderDataModel;
    }

    public LoadedController(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    System.out.println(orderDataModel.getOrderNumber());
    ORDER_NUMBER.setText(orderDataModel.getOrderDescription());
    }
}
