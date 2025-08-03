/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.dao.RevenueDAO;
import comnieu.entity.Revenue;
import comnieu.util.XJdbc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class RevenueDAOImpl implements RevenueDAO {

    @Override
    public List<Revenue> getRevenueByEmployee(Date fromDate, Date toDate) {
        List<Revenue> list = new ArrayList<>();
        String sql = """
            SELECT e.FullName AS EmployeeName, 
                   SUM(bd.Quantity * bd.UnitPrice) AS TotalRevenue
            FROM Bill b
            JOIN BillDetail bd ON b.Id = bd.BillId
            JOIN Employee e ON b.EmployeeId = e.Id
            WHERE b.CreatedDate BETWEEN ? AND ? AND b.Status = 1
            GROUP BY e.FullName
        """;

        try {
            ResultSet rs = XJdbc.executeQuery(sql,
                new java.sql.Date(fromDate.getTime()), 
                new java.sql.Date(toDate.getTime()));
            while (rs.next()) {
                String employeeName = rs.getString("EmployeeName");
                BigDecimal total = rs.getBigDecimal("TotalRevenue");
                list.add(new Revenue(total, employeeName));
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Revenue.PopularDish> getPopularDishes(Date fromDate, Date toDate) {
        List<Revenue.PopularDish> list = new ArrayList<>();

        String sqlMost = """
            SELECT TOP 3 d.Name, SUM(bd.Quantity) AS TotalQuantity, 
                         SUM(bd.Quantity * bd.UnitPrice) AS TotalRevenue
            FROM Bill b
            JOIN BillDetail bd ON b.Id = bd.BillId
            JOIN Dish d ON bd.DishId = d.Id
            WHERE b.CreatedDate BETWEEN ? AND ? AND b.Status = 1
            GROUP BY d.Name
            ORDER BY SUM(bd.Quantity) DESC
        """;

        String sqlLeast = """
            SELECT TOP 3 d.Name, SUM(bd.Quantity) AS TotalQuantity, 
                         SUM(bd.Quantity * bd.UnitPrice) AS TotalRevenue
            FROM Bill b
            JOIN BillDetail bd ON b.Id = bd.BillId
            JOIN Dish d ON bd.DishId = d.Id
            WHERE b.CreatedDate BETWEEN ? AND ? AND b.Status = 1
            GROUP BY d.Name
            ORDER BY SUM(bd.Quantity) ASC
        """;

        try {
            // Món bán chạy nhất
            ResultSet rsMost = XJdbc.executeQuery(sqlMost,
                new java.sql.Date(fromDate.getTime()),
                new java.sql.Date(toDate.getTime())
            );
            while (rsMost.next()) {
                list.add(new Revenue.PopularDish(
                    rsMost.getString("Name"),
                    rsMost.getInt("TotalQuantity"),
                    rsMost.getBigDecimal("TotalRevenue")
                ));
            }
            rsMost.getStatement().getConnection().close();

            // Món ít bán nhất
            ResultSet rsLeast = XJdbc.executeQuery(sqlLeast,
                new java.sql.Date(fromDate.getTime()),
                new java.sql.Date(toDate.getTime())
            );
            while (rsLeast.next()) {
                list.add(new Revenue.PopularDish(
                    rsLeast.getString("Name"),
                    rsLeast.getInt("TotalQuantity"),
                    rsLeast.getBigDecimal("TotalRevenue")
                ));
            }
            rsLeast.getStatement().getConnection().close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}