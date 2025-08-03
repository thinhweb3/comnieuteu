/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.entity;

import java.math.BigDecimal;

public class Revenue {

    private String employeeName;
    private BigDecimal totalRevenue;

    public Revenue(BigDecimal totalRevenue, String employeeName) {
        this.employeeName = employeeName;
        this.totalRevenue = totalRevenue;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    // Inner class: PopularDish
    public static class PopularDish {
        private String name;
        private int quantity;
        private BigDecimal total;

        public PopularDish(String name, int quantity, BigDecimal total) {
            this.name = name;
            this.quantity = quantity;
            this.total = total;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }
}