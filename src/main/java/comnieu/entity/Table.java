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
public class Table {
    private String MaBan;
    private String TenBan;
    private String TrangThai;
}
