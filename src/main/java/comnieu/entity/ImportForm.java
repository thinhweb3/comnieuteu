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
public class ImportForm {
    private String MaPN;
    private Date NgayNhap;
    private String MaNCC;
    private String MaNV;
}
