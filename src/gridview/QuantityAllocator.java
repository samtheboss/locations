package gridview;

import Utils.DBConnection;
import Utils.WhereBy;
import Utils.utils;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuantityAllocator {
  public static class AllocationResult {
    List<ItemModel> allocatedItems;

    public AllocationResult(List<ItemModel> allocatedItems) {
      this.allocatedItems = allocatedItems;
    }
  }

  public static void main(String[] args) {
    List<ItemModel> items = items("A0088");
    double requiredQuantity = 20;
    AllocationResult allocationResult = allocateQuantities2(items, requiredQuantity);
    ItemModel update =new ItemModel();
    System.out.println("Allocated items: ");
    for (ItemModel item : allocationResult.allocatedItems) {
     // System.out.println(utils.generateInsertSQL(item, "ITEM_TRACKS"));
      update.setAvailableQty(item.getAvailableQty());
      List<WhereBy> where =
          Arrays.asList(
              new WhereBy("ITEM_CODE", item.getItemCode()),
                  new WhereBy("ITEM_LOCATION", item.getItemLocation()),
                  new WhereBy("ITEM_SERIAL", item.getItemSerial()));
      System.out.println(utils.genUpdateSQL(update, where, "ITEM_TRACKS"));

    }
  }

  public static List<ItemModel> items(String itemCode) {
    List<ItemModel> items = new ArrayList<>();
    ItemModel itemModel = new ItemModel();
    String select = "SELECT * FROM ITEM_TRACKS WHERE ITEM_CODE =?";

    Connection conn = DBConnection.getConnection();
    try {
      PreparedStatement pst = conn.prepareStatement(select);
      pst.setString(1, itemCode);
      ResultSet rs = pst.executeQuery();

      while (rs.next()) {
        itemModel.setItemCode(rs.getString("ITEM_CODE"));
        itemModel.setItemSerial(rs.getString("TRACK_SERIAL"));
        itemModel.setAvailableQty(rs.getDouble("QUANTITY"));
        itemModel.setItemLocation(rs.getString("ITEM_LOCATION"));
        items.add(itemModel);
        itemModel = new ItemModel();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return items;
  }

  public static AllocationResult allocateQuantities2(
      List<ItemModel> items, Double requiredQuantity) {
    List<ItemModel> allocatedItems = new ArrayList<>();
    if (items.isEmpty()) {
      return new AllocationResult(allocatedItems);
    }
    double totalAvailableQuantity = items.stream().mapToDouble(ItemModel::getAvailableQty).sum();
    if (requiredQuantity > totalAvailableQuantity || totalAvailableQuantity == 0) {
      System.out.println(" no enough stocks");
      return new AllocationResult(allocatedItems);
    }
    for (ItemModel currentItem : items) {
      double availableQuantity = currentItem.getAvailableQty();
      double allocatedQuantity = Math.min(availableQuantity, requiredQuantity);
      allocatedItems.add(
          new ItemModel(
              currentItem.getItemCode(),
              currentItem.getItemSerial(),
              allocatedQuantity,
              availableQuantity,currentItem.getItemLocation()));
      requiredQuantity -= allocatedQuantity;
      if (requiredQuantity <= 0) {
        break;
      }
    }
    return new AllocationResult(allocatedItems);
  }
}
