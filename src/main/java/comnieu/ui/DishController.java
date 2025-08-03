/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.ui;

import comnieu.entity.Bill;

/**
 *
 * @author Admin
 */
public interface DishController {
    void fillCategories(); 
 
    void setBill(Bill bill); // nhận bill từ BillJDialog  
void open(); // hiển thị loại và đồ uống 
void fillDishes(); //  tải và hiển thị đồ uống 
void addDishToBill(); // thêm đồ uống vào bill 
}