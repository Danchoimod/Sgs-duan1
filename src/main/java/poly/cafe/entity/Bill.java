package poly.cafe.entity;

import java.util.Date;
import lombok.*;

/**
 * Thông tin hóa đơn (Bill)
 * Tự động sinh getter, setter, toString, builder... nhờ Lombok
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private Long id;
    private String username;
    private Integer cardId;

    @Builder.Default
    private Date checkin = new Date();

    private Date checkout;
    private int status;
}
