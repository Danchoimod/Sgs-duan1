package poly.cafe.entity;

import lombok.*;

/**
 * Thông tin thẻ bàn hoặc khu vực
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Integer id;
    private int status;
}
