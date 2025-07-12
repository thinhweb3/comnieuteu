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
public class Bill {
    private Integer MaHD;
    private Integer MaNV;
    @Builder.Default
    private Date checkin = new Date();
    private Date checkout;
    private int status;
    
    public static final String DATE_PATTERN = "HH:mm:ss dd-MM-yyyy";
}
