/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.entity.PromotionStats;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import comnieu.util.XJdbc;
import comnieu.dao.PromotionStatsDAO;

public class PromotionStatsDAOImpl implements PromotionStatsDAO {

    @Override
    public List<PromotionStats> getPromotionsUsed(Date from, Date to) {
        List<PromotionStats> list = new ArrayList<>();
        String sql = """
            SELECT 
                p.Id,
                p.Name,
                p.DiscountRate,
                COUNT(b.Id) AS UsedCount
            FROM Bill b
            JOIN Promotion p ON b.PromotionId = p.Id
            WHERE b.Status = 1 AND b.CreatedDate BETWEEN ? AND ?
            GROUP BY p.Id, p.Name, p.DiscountRate
            ORDER BY UsedCount DESC
        """;

        try {
            ResultSet rs = XJdbc.executeQuery(
                sql,
                new java.sql.Date(from.getTime()),
                new java.sql.Date(to.getTime())
            );

            while (rs.next()) {
                PromotionStats promotion = new PromotionStats(
                    rs.getInt("Id"),
                    rs.getString("Name"),
                    rs.getFloat("DiscountRate"),
                    rs.getInt("UsedCount")
                );
                list.add(promotion);
            }

            rs.getStatement().getConnection().close(); // Đóng connection nếu chưa tự đóng
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}