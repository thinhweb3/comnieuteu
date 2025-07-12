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
public class ReceiptDetail {
    private String MaCTPN;
    private String MaPN;
    private String MaNL;
    private Float SL;
    private BigDecimal DonGia;
}
