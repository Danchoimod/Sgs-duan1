package poly.cafe.entity;

import lombok.*;

/**
 * @author PhuPHam
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class BillDetail {
    private Long id;
    private Long billId;
    private String drinkId;
    private double unitPrice;
    private double discount;
    private int quantity;
    private String DrinkName;
}
