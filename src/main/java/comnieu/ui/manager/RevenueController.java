/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.ui.manager;

import java.util.Date;

public interface RevenueController {
    void open(); // Khi form mở
    void selectTimeRange(); // Khi chọn Hôm nay / Tháng này / Năm nay
    void fillRevenueByEmployee(Date from, Date to); // Tab 1
    void fillPopularDishes(Date from, Date to);     // Tab 2
    void fillAll(); // Gọi đúng fill theo tab đang mở
}