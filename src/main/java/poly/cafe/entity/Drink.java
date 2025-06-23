package poly.cafe.entity;

import lombok.*;

/**
 * Thông tin thức uống trong menu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drink {
    private String id;
    private String name;

    @Builder.Default
    private String image = "product.png";

    private double unitPrice;
    private double discount;
    private boolean available;
    private String categoryId;
}
