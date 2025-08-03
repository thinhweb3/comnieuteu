package comnieu.dao.impl;

import comnieu.dao.SupplierDAO;
import comnieu.entity.Supplier;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

/**
 * DAO implementation cho Supplier
 * 
 * @author Admin
 */
public class SupplierDAOImpl implements SupplierDAO {

    final String insertSql = "INSERT INTO Supplier (Name, Address, Phone) VALUES (?, ?, ?)";
    final String updateSql = "UPDATE Supplier SET Name=?, Address=?, Phone=? WHERE Id=?";
    final String deleteSql = "DELETE FROM Supplier WHERE Id=?";
    final String findAllSql = "SELECT * FROM Supplier";
    final String findByIdSql = "SELECT * FROM Supplier WHERE Id=?";
    final String findByPhoneSql = "SELECT * FROM Supplier WHERE Phone=?";
    final String findByNameSql = "SELECT * FROM Supplier WHERE Name LIKE ?";

    @Override
    public Supplier create(Supplier entity) {
        Object[] values = {
            entity.getName(),
            entity.getAddress(),
            entity.getPhone()
        };
        int id = (int) XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(Supplier entity) {
        Object[] values = {
            entity.getName(),
            entity.getAddress(),
            entity.getPhone(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Supplier> findAll() {
        return XQuery.getBeanList(Supplier.class, findAllSql);
    }

    @Override
    public Supplier findById(Integer id) {
        return XQuery.getSingleBean(Supplier.class, findByIdSql, id);
    }

    @Override
    public Supplier findByPhone(String phone) {
        return XQuery.getSingleBean(Supplier.class, findByPhoneSql, phone);
    }

    @Override
    public List<Supplier> findByName(String keyword) {
        return XQuery.getBeanList(Supplier.class, findByNameSql, "%" + keyword + "%");
    }
}
