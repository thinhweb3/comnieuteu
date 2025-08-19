package comnieu.dao.impl;

import comnieu.dao.ImportReceiptDAO;
import comnieu.entity.ImportReceipt;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;
import java.time.LocalDate;
import java.util.Date;

import java.util.List;

/**
 * DAO implementation for ImportReceipt
 * 
 * @author Admin
 */
public class ImportReceiptDAOImpl implements ImportReceiptDAO {

    final String insertSql = """
        INSERT INTO ImportReceipt (ImportDate, SupplierId, EmployeeId)
        VALUES (?, ?, ?)
    """;

    final String updateSql = """
        UPDATE ImportReceipt
        SET ImportDate = ?, SupplierId = ?, EmployeeId = ?
        WHERE Id = ?
    """;

    final String deleteSql = "DELETE FROM ImportReceipt WHERE Id = ?";
    final String findAllSql = "SELECT * FROM ImportReceipt";
    final String findByIdSql = "SELECT * FROM ImportReceipt WHERE Id = ?";
    final String findBySupplierIdSql = "SELECT * FROM ImportReceipt WHERE SupplierId = ?";
    final String findByEmployeeIdSql = "SELECT * FROM ImportReceipt WHERE EmployeeId = ?";

    @Override
    public ImportReceipt create(ImportReceipt entity) {
        Object[] values = {
            entity.getImportDate(),
            entity.getSupplierId(),
            entity.getEmployeeId()
        };
        long id = XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(ImportReceipt entity) {
        Object[] values = {
            entity.getImportDate(),
            entity.getSupplierId(),
            entity.getEmployeeId(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Long id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<ImportReceipt> findAll() {
        return XQuery.getBeanList(ImportReceipt.class, findAllSql);
    }

    @Override
    public ImportReceipt findById(Long id) {
        return XQuery.getSingleBean(ImportReceipt.class, findByIdSql, id);
    }

    @Override
    public List<ImportReceipt> findBySupplierId(Integer supplierId) {
        return XQuery.getBeanList(ImportReceipt.class, findBySupplierIdSql, supplierId);
    }

    @Override
    public List<ImportReceipt> findByEmployeeId(Integer employeeId) {
        return XQuery.getBeanList(ImportReceipt.class, findByEmployeeIdSql, employeeId);
    }
    
    @Override
    public List<ImportReceipt> findByDateRange(LocalDate from, LocalDate to) {
        final String sql = "SELECT * FROM ImportReceipt WHERE ImportDate BETWEEN ? AND ?";
        Date sqlFrom = java.sql.Date.valueOf(from);
        Date sqlTo   = java.sql.Date.valueOf(to);
        return XQuery.getBeanList(ImportReceipt.class, sql, sqlFrom, sqlTo);
    }
}
