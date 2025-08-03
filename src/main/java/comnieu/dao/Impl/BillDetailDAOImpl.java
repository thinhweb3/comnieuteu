/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.dao.BillDetailDAO;
import comnieu.entity.BillDetail;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class BillDetailDAOImpl implements BillDetailDAO {

    String createSql = """
        INSERT INTO BillDetail (BillId, DishId, Quantity, UnitPrice)
        VALUES (?, ?, ?, ?)
    """;

    String updateSql = """
        UPDATE BillDetail
        SET BillId = ?, DishId = ?, Quantity = ?, UnitPrice = ?
        WHERE Id = ?
    """;

    String deleteSql = "DELETE FROM BillDetail WHERE Id = ?";

    String findAllSql = "SELECT * FROM BillDetail";
    String findByIdSql = "SELECT * FROM BillDetail WHERE Id = ?";
    String findByBillIdSql = "SELECT * FROM BillDetail WHERE BillId = ?";
    String findByDishIdSql = "SELECT * FROM BillDetail WHERE DishId = ?";

    @Override
    public BillDetail create(BillDetail entity) {
        Object[] values = {
            entity.getBillId(),
            entity.getDishId(),
            entity.getQuantity(),
            entity.getUnitPrice()
        };
        long id = XJdbc.executeInsert(createSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(BillDetail entity) {
        Object[] values = {
            entity.getBillId(),
            entity.getDishId(),
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Long id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<BillDetail> findAll() {
        return XQuery.getBeanList(BillDetail.class, findAllSql);
    }

    @Override
    public BillDetail findById(Long id) {
        return XQuery.getSingleBean(BillDetail.class, findByIdSql, id);
    }

    @Override
    public List<BillDetail> findByBillId(Long billId) {
        return XQuery.getBeanList(BillDetail.class, findByBillIdSql, billId);
    }

    @Override
    public List<BillDetail> findByDishId(String dishId) {
        return XQuery.getBeanList(BillDetail.class, findByDishIdSql, dishId);
    }


}
