package lmp;

public class Ingredientlmp {
    private String id;
    private String name;
    private double quantity;
    private String unit;
    private String status;
    private String supplierId;

    public Ingredientlmp(String id, String name, double quantity, String unit, String status, String supplierId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
        this.supplierId = supplierId;
    }

    public Object[] toRow() {
        return new Object[]{false, id, name, quantity, unit, status, supplierId};
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getStatus() { return status; }
    public String getSupplierId() { return supplierId; }
}
