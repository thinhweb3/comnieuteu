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
public class Promotion {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    
    private Integer Id;
    private String Name;
    private Date StartDate;
    private Date EndDate;
    private Double DiscountRate;
}
