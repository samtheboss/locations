package pops;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class Main extends Application {
    private TableView<Item> tableView;
    private TextField inputField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Allocation App");

        // Initialize table view
        tableView = new TableView<>();
        TableColumn<Item, Double> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());

        TableColumn<Item, Double> allocatedColumn = new TableColumn<>("Allocated");
        allocatedColumn.setCellValueFactory(cellData -> cellData.getValue().allocatedProperty().asObject());
        allocatedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        tableView.getColumns().addAll(valueColumn, allocatedColumn);

        // Initialize input components
        Label inputLabel = new Label("Enter Amount:");
        inputField = new TextField();
        Button allocateButton = new Button("Allocate");
        allocateButton.setOnAction(event -> allocateAmount());

        // Layout setup
        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView, inputLabel, inputField, allocateButton);
        Scene scene = new Scene(vbox, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void allocateAmount() {
        try {
            double amount = Double.parseDouble(inputField.getText());
            Item selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                double remainingAmount = selectedItem.getValue() - selectedItem.getAllocated();
                double allocatedAmount = Math.min(amount, remainingAmount);
                selectedItem.setAllocated(selectedItem.getAllocated() + allocatedAmount);
                tableView.refresh();
            }
        } catch (NumberFormatException e) {
            // Handle invalid input
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}

class Item {
    private final SimpleDoubleProperty value;
    private final SimpleDoubleProperty allocated;

    public Item(double value) {
        this.value = new SimpleDoubleProperty(value);
        this.allocated = new SimpleDoubleProperty(0);
    }

    public double getValue() {
        return value.get();
    }

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    public double getAllocated() {
        return allocated.get();
    }

    public void setAllocated(double allocated) {
        this.allocated.set(allocated);
    }

    public SimpleDoubleProperty allocatedProperty() {
        return allocated;
    }
}
