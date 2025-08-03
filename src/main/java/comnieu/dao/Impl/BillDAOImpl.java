/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.dao.BillDAO;
import comnieu.entity.Bill;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class BillDAOImpl implements BillDAO {

    String createSql = """
        INSERT INTO Bill (CreatedDate, CheckIn, CheckOut, Status, EmployeeId, TableId, PromotionId)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;
    String updateSql = """
        UPDATE Bill SET CreatedDate=?, CheckIn=?, CheckOut=?, Status=?, EmployeeId=?, TableId=?, PromotionId=?
        WHERE Id=?
    """;
    String deleteSql = "DELETE FROM Bill WHERE Id=?";
    String findAllSql = "SELECT * FROM Bill";
    String findByIdSql = "SELECT * FROM Bill WHERE Id=?";

    String findByUsernameSql = "SELECT * FROM Bill WHERE EmployeeId = (SELECT Id FROM Employees WHERE Username=?)";
    String findByTableIdSql = "SELECT * FROM Bill WHERE TableId=?";
    String findByTimeRangeSql = "SELECT * FROM Bill WHERE CheckIn BETWEEN ? AND ? ORDER BY CheckIn DESC";
    String findByDateRangeSql = "SELECT * FROM Bill WHERE CONVERT(DATE, CheckIn) BETWEEN ? AND ?";
    String findServicingByTableIdSql = "SELECT * FROM Bill WHERE TableId=? AND Status=0";
    String findByUserAndTimeRangeSql = """
        SELECT * FROM Bill WHERE EmployeeId = (SELECT Id FROM Employees WHERE Username=?) 
        AND CheckIn BETWEEN ? AND ?
    """;

    @Override
    public Bill create(Bill entity) {
        Object[] values = {
            entity.getCreatedDate(),
            entity.getCheckIn(),
            entity.getCheckOut(),
            entity.getStatus(),
            entity.getEmployeeId(),
            entity.getTableId(),
            entity.getPromotionId()
        };
        long id = XJdbc.executeInsert(createSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(Bill entity) {
        Object[] values = {
            entity.getCreatedDate(),
            entity.getCheckIn(),
            entity.getCheckOut(),
            entity.getStatus(),
            entity.getEmployeeId(),
            entity.getTableId(),
            entity.getPromotionId(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Long id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Bill> findAll() {
        return XQuery.getBeanList(Bill.class, findAllSql);
    }

    @Override
    public Bill findById(Long id) {
        return XQuery.getSingleBean(Bill.class, findByIdSql, id);
    }

    @Override
    public List<Bill> findByUsername(String username) {
        return XQuery.getBeanList(Bill.class, findByUsernameSql, username);
    }

    @Override
    public List<Bill> findByTableId(Integer tableId) {
        return XQuery.getBeanList(Bill.class, findByTableIdSql, tableId);
    }

    @Override
    public List<Bill> findByTimeRange(Date begin, Date end) {
        return XQuery.getBeanList(Bill.class, findByTimeRangeSql, begin, end);
    }

    @Override
    public List<Bill> findByDateRange(LocalDate from, LocalDate to) {
        return XQuery.getBeanList(Bill.class, findByDateRangeSql, from, to);
    }

    @Override
    public Bill findServicingByTableId(Integer tableId) {
        Bill bill = XQuery.getSingleBean(Bill.class, findServicingByTableIdSql, tableId);
        if (bill == null) {
            Bill newBill = Bill.builder()
                .tableId(tableId)
                .createdDate(new Date())
                .checkIn(new Date())
                .status(0)
                .build();
            return this.create(newBill);
        }
        return bill;
    }

    @Override
    public List<Bill> findByUserAndTimeRange(String username, Date begin, Date end) {
        return XQuery.getBeanList(Bill.class, findByUserAndTimeRangeSql, username, begin, end);
    }
    @Override
public Bill findUnpaidByTableId(Long tableId) {
    String sql = "SELECT * FROM Bill WHERE TableId = ? AND Status = 0";
    return XQuery.getSingleBean(Bill.class, sql, tableId);
}

}
