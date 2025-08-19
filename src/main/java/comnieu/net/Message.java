// comnieu/net/Message.java
package comnieu.net;

import java.util.List;

public class Message {
    public String type;        // "BILL_CREATED", "BILL_UPDATED"
    public String billId;      // String (tuỳ bạn, có thể để null khi tạo mới)
    public int tableId;        // ★ thêm: id bàn
    public String tableName;
    public List<Item> items;
    public double total;

    public static class Item {
        public String dishId;
        public String dishName;
        public int quantity;
        public double unitPrice;
        public double discount;
        public double amount;
    }
}
