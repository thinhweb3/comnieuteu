/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.dao.DiningTableDAO;
import comnieu.entity.DiningTable;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

/**
 *
 * @author Admin
 */
public class DiningTableDAOImpl implements DiningTableDAO {

    final String insertSql = "INSERT INTO DiningTable (Name, Status) VALUES (?, ?)";
    final String updateSql = "UPDATE DiningTable SET Name = ?, Status = ? WHERE Id = ?";
    final String deleteSql = "DELETE FROM DiningTable WHERE Id = ?";
    final String findAllSql = "SELECT * FROM DiningTable";
    final String findByIdSql = "SELECT * FROM DiningTable WHERE Id = ?";
    final String findByStatusSql = "SELECT * FROM DiningTable WHERE Status = ?";

    @Override
    public DiningTable create(DiningTable entity) {
        Object[] values = {
            entity.getName(),
            entity.getStatus()
        };
        int id = (int) XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(DiningTable entity) {
        Object[] values = {
            entity.getName(),
            entity.getStatus(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<DiningTable> findAll() {
        return XQuery.getBeanList(DiningTable.class, findAllSql);
    }

    @Override
    public DiningTable findById(Integer id) {
        return XQuery.getSingleBean(DiningTable.class, findByIdSql, id);
    }

    @Override
    public List<DiningTable> findByStatus(Integer status) {
        return XQuery.getBeanList(DiningTable.class, findByStatusSql, status);
    }
    
    @Override
    public void updateStatus(Integer tableId, int status) {
    String sql = "UPDATE DiningTable SET Status = ? WHERE Id = ?";
    XJdbc.executeUpdate(sql, status, tableId);
    }
}
