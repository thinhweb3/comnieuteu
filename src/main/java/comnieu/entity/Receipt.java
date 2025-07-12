package comnieu.entity;

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
public class Receipt {
    private String MaMA;
    private String MaNL;
    private Float SoLuong;
    private String DonVi;
}
