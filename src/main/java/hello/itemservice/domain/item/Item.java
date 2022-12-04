package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//게터 세터 자동으로 생성
@Data
public class Item {

    private Long id;

    private String itemName;

    private Integer price;

    //수량, null로 들어갈 수도 있기 때문에 Integer
    private Integer quantity;

    private Boolean open; //판매 여부

    private List<String> regions; //등록 지역

    private ItemType itemType; //상품 종류

    private String deliveryCode; //배송 방식

    //기본 생성자
    public Item() {

    }

    //id를 제외한 생성자
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
