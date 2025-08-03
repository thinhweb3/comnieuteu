/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.dao;

import comnieu.entity.Dish;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface DishDAO extends CrudDAO<Dish, Integer> {
    List<Dish> findByCategoryId(Integer categoryId);
}
