package hello.itemservice.domain.item;

//상품 종류
public enum ItemType {

    //enum 3개 마들기
    BOOK("도서"), FOOD("식품"), ETC("기타");

    //아이템에 대한 설명
    private final String description;

    //생성자
    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
