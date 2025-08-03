package comnieu.dao;

import comnieu.entity.DishIngredient;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface DishIngredientDAO {
    DishIngredient create(DishIngredient entity);
    void update(DishIngredient entity);
    void delete(Integer dishId, Integer ingredientId);
    List<DishIngredient> findAll();
    DishIngredient findById(Integer dishId, Integer ingredientId);
    List<DishIngredient> findByDishId(Integer dishId);
    List<DishIngredient> findByIngredientId(Integer ingredientId);
}
