/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.ui;

import comnieu.entity.Bill;

/**
 *
 * @author Admin
 */
public interface BillController {
       void fillBillDetails();
        void setBill(Bill bill); // truyền bill vào cửa sổ để hiển thị 
void open(); // Hiển thị bill 
void close(); // Xóa bill nếu ko chứa đồ uống nào 
void showDrinkJDialog(); // Hiển thị cửa sổ bổ sung đồ uống vào bill 
void removeDrinks(); // Xóa đồ uống khỏi bill
void updateQuantity(); // Thay đổi số lượng đồ uống 
    void checkout(); // Thanh toán 
    void cancel(); // Hủy bill 
}
