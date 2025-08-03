/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Bill {
    private Long id;
    private Date createdDate;
    private Date checkIn;
    private Date checkOut;
    private Integer status;
    private Integer employeeId;
    private Integer tableId;
    private Integer promotionId;
}