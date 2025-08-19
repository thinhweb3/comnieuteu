package comnieu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BillDetail {
    private long id;
    private Long billId;
    private Integer dishId;
    private String dishName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
