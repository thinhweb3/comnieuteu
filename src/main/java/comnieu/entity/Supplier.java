/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author amyas
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
    public class Supplier {
        private String MaNCC;
        private String TenNCC;
        private String DiaChi;
        private Integer SDT;

    @Override
    public String toString() {
        return MaNCC + " - " + TenNCC;
    }
}
