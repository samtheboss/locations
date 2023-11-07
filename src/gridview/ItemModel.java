package gridview;

public class ItemModel {
    String itemCode;
    String itemSerial;
    Double allocatedQty;
    Double availableQty;
    String itemLocation;
    Double issueQty;

    public ItemModel() {
    }

    public ItemModel(String itemCode, String itemSerial, Double allocatedQty, Double availableQty) {
        this.itemCode = itemCode;
        this.itemSerial = itemSerial;
        this.allocatedQty = allocatedQty;
        this.availableQty = availableQty;
    }

    public ItemModel(String itemCode, String itemSerial, Double allocatedQty, Double availableQty, String itemLocation, Double issueQty) {
        this.itemCode = itemCode;
        this.itemSerial = itemSerial;
        this.allocatedQty = allocatedQty;
        this.availableQty = availableQty;
        this.itemLocation = itemLocation;
        this.issueQty = issueQty;
    }

    public ItemModel(String itemCode, String itemSerial, Double allocatedQty, Double availableQty, String itemLocation) {
        this.itemCode = itemCode;
        this.itemSerial = itemSerial;
        this.allocatedQty = allocatedQty;
        this.availableQty = availableQty;
        this.itemLocation = itemLocation;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemSerial() {
        return itemSerial;
    }

    public void setItemSerial(String itemSerial) {
        this.itemSerial = itemSerial;
    }

    public Double getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(Double allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public Double getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(Double availableQty) {
        this.availableQty = availableQty;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public Double getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Double issueQty) {
        this.issueQty = issueQty;
    }
}