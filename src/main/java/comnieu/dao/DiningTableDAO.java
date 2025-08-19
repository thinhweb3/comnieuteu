/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.dao;

import comnieu.entity.DiningTable;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface DiningTableDAO extends CrudDAO<DiningTable, Integer> {
    List<DiningTable> findByStatus(Integer status);
    void updateStatus(Integer tableId, int status);
}
