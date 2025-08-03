/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.ui.manager;

import java.util.Date;

public interface PromotionStatsController {
    void open(); // gọi khi dialog mở
    void selectTimeRange(String option); // khi chọn Hôm nay/Tháng này/Năm nay
    void filter(Date from, Date to); // khi bấm nút Lọc
}