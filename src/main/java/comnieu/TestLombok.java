/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu;

/**
 *
 * @author Admin
 */
import lombok.Data;

@Data
public class TestLombok {
    private String name;
     private int age;
     public static void main(String[] args) {
        TestLombok test = new TestLombok();
        test.setName("Cơm Niêu");
        test.setAge(2025);
        
        System.out.println("Tên: " + test.getName());
        System.out.println("Tuổi: " + test.getAge());
    }
}
