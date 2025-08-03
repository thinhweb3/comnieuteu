/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.dao;

import comnieu.entity.Revenue;
import java.util.Date;
import java.util.List;

public interface RevenueDAO {
    List<Revenue> getRevenueByEmployee(Date fromDate, Date toDate);
    List<Revenue.PopularDish> getPopularDishes(Date fromDate, Date toDate);
}
