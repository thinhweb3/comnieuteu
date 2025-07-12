package comnieu.entity;

import java.math.BigDecimal;
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
public class BillDetail {
    private String MaHDCT;
    private String MaHD;
    private String MaMA;
    private Integer SL;
    private BigDecimal DonGia;
}
