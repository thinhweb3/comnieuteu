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

public class BillDAOImpl implements BillDAO {

    private static final String INSERT_WITH_PROMO =
        "INSERT INTO Bill ([CreatedDate],[CheckIn],[CheckOut],[Status],[EmployeeId],[TableId],[PromotionId]) " +
        "VALUES (?,?,?,?,?,?,?)";

    private static final String INSERT_NO_PROMO =
        "INSERT INTO Bill ([CreatedDate],[CheckIn],[CheckOut],[Status],[EmployeeId],[TableId]) " +
        "VALUES (?,?,?,?,?,?)";

    private static final String UPDATE_SQL =
        "UPDATE Bill SET [CreatedDate]=?,[CheckIn]=?,[CheckOut]=?,[Status]=?,[EmployeeId]=?,[TableId]=?,[PromotionId]=? " +
        "WHERE [Id]=?";

    private static final String DELETE_SQL = "DELETE FROM Bill WHERE [Id]=?";

    private static final String FIND_ALL_SQL = "SELECT * FROM Bill";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM Bill WHERE [Id]=?";
    private static final String FIND_BY_USERNAME_SQL =
        "SELECT * FROM Bill WHERE [EmployeeId]=(SELECT [Id] FROM Employee WHERE [Username]=?)";
    private static final String FIND_BY_TABLE_SQL = "SELECT * FROM Bill WHERE [TableId]=?";
    private static final String FIND_BY_TIMERANGE_SQL =
        "SELECT * FROM Bill WHERE [CheckIn] BETWEEN ? AND ? ORDER BY [CheckIn] DESC";
    private static final String FIND_BY_DATERANGE_SQL =
        "SELECT * FROM Bill WHERE CONVERT(DATE,[CheckIn]) BETWEEN ? AND ?";
    private static final String FIND_SERVICING_BY_TABLE_SQL =
        "SELECT TOP 1 * FROM Bill WHERE [TableId]=? AND [Status]=0 ORDER BY [Id] DESC";
    private static final String FIND_BY_USER_TIMERANGE_SQL =
        "SELECT * FROM Bill " +
        "WHERE [EmployeeId]=(SELECT [Id] FROM Employee WHERE [Username]=?) " +
        "  AND [CheckIn] BETWEEN ? AND ?";

    // ===== CRUD =====
    @Override
    public Bill create(Bill e) {
        // Ép đúng kiểu cho SQL Server:
        java.sql.Date created = (e.getCreatedDate() == null)
                ? new java.sql.Date(System.currentTimeMillis())
                : new java.sql.Date(e.getCreatedDate().getTime());
        java.sql.Timestamp checkIn = (e.getCheckIn() == null)
                ? new java.sql.Timestamp(System.currentTimeMillis())
                : new java.sql.Timestamp(e.getCheckIn().getTime());
        java.sql.Timestamp checkOut = (e.getCheckOut() == null)
                ? null
                : new java.sql.Timestamp(e.getCheckOut().getTime());

        long id;
        if (e.getPromotionId() == null) {
            id = XJdbc.executeInsert(INSERT_NO_PROMO,
                    created, checkIn, checkOut, e.getStatus(),
                    e.getEmployeeId(), e.getTableId());
        } else {
            id = XJdbc.executeInsert(INSERT_WITH_PROMO,
                    created, checkIn, checkOut, e.getStatus(),
                    e.getEmployeeId(), e.getTableId(), e.getPromotionId());
        }
        e.setId(id);
        return e;
    }

    @Override
    public void update(Bill e) {
        java.sql.Date created = (e.getCreatedDate() == null)
                ? null
                : new java.sql.Date(e.getCreatedDate().getTime());
        java.sql.Timestamp checkIn = (e.getCheckIn() == null)
                ? null
                : new java.sql.Timestamp(e.getCheckIn().getTime());
        java.sql.Timestamp checkOut = (e.getCheckOut() == null)
                ? null
                : new java.sql.Timestamp(e.getCheckOut().getTime());

        XJdbc.executeUpdate(UPDATE_SQL,
                created, checkIn, checkOut, e.getStatus(),
                e.getEmployeeId(), e.getTableId(), e.getPromotionId(),
                e.getId());
    }

    @Override
    public void deleteById(Long id) {
        XJdbc.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Bill> findAll() {
        return XQuery.getBeanList(Bill.class, FIND_ALL_SQL);
    }

    @Override
    public Bill findById(Long id) {
        return XQuery.getSingleBean(Bill.class, FIND_BY_ID_SQL, id);
    }

    // ===== Queries mở rộng =====
    @Override
    public List<Bill> findByUsername(String username) {
        return XQuery.getBeanList(Bill.class, FIND_BY_USERNAME_SQL, username);
    }

    @Override
    public List<Bill> findByTableId(Integer tableId) {
        return XQuery.getBeanList(Bill.class, FIND_BY_TABLE_SQL, tableId);
    }

    @Override
    public List<Bill> findByTimeRange(Date begin, Date end) {
        // begin/end là java.util.Date – XJdbc sẽ map sang Timestamp
        return XQuery.getBeanList(Bill.class, FIND_BY_TIMERANGE_SQL, begin, end);
    }

    @Override
    public List<Bill> findByDateRange(LocalDate from, LocalDate to) {
        return XQuery.getBeanList(Bill.class, FIND_BY_DATERANGE_SQL, from, to);
    }

    @Override
    public List<Bill> findByUserAndTimeRange(String username, Date begin, Date end) {
        return XQuery.getBeanList(Bill.class, FIND_BY_USER_TIMERANGE_SQL, username, begin, end);
    }

    @Override
    public Bill findServicingByTableId(Integer tableId) {
        return XQuery.getSingleBean(Bill.class, FIND_SERVICING_BY_TABLE_SQL, tableId);
    }
}
