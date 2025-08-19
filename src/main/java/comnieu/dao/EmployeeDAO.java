/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.dao;

import comnieu.entity.Employee;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface EmployeeDAO extends CrudDAO<Employee, Integer> {
    Employee findByUsernameAndGmail(String username, String gmail);
    Employee findByGmail(String gmail);
    Employee findByUsername(String username);
    List<Employee> findByRole(Integer role);
}
