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
public class Promotion {
    private String MaKM;
    private String TenKM;
    private Date NgayBatDau;
    private Date NgayKetThuc;
    private Float GiamGia;
}
