package comnieu.dao.impl;

import java.util.List;
import comnieu.dao.CategoryDAO;
import comnieu.entity.Category;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

public class CategoryDAOImpl implements CategoryDAO {

    String createSql = "INSERT INTO Category(Id, Name) VALUES(?, ?)";
    String updateSql = "UPDATE Category SET Name=? WHERE Id=?";
    String deleteSql = "DELETE FROM Category WHERE Id=?";
    String findAllSql = "SELECT * FROM Category";
    String findByIdSql = "SELECT * FROM Category WHERE Id=?";

    @Override
    public Category create(Category entity) {
        Object[] values = {
            entity.getId(),
            entity.getName()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(Category entity) {
        Object[] values = {
            entity.getName(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Category> findAll() {
        return XQuery.getBeanList(Category.class, findAllSql);
    }

    @Override
    public Category findById(String id) {
        return XQuery.getSingleBean(Category.class, findByIdSql, id);
    }
}
