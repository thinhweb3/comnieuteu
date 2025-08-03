package comnieu.dao;

import comnieu.entity.Ingredient;
import java.util.List;

/**
 * DAO interface cho Ingredient
 * 
 * @author Admin
 */
public interface IngredientDAO extends CrudDAO<Ingredient, Integer> {
    List<Ingredient> findBySupplierId(Integer supplierId);
    List<Ingredient> findByStatus(Integer status);
}
