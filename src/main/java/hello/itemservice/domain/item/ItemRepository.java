package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//상품 저장소, @Repository안에 @Component있어 @ComponentScan 대상이 됨
@Repository
public class ItemRepository {

    //static 사용, id가 long이여서
    //멀티쓰레드로 동시에 접근하면 HashMap쓰면 안됨, 동시 접근은 ConcurrentHashMap으로
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L; //static 사용

    //저장 메서드, id와 Item객체 넣어주는
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    //id반환
    public Item findById(Long id) {
        return store.get(id);
    }

    //모든 객체 반환, 배열로 감싸서 반환
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    //업데이트 메서드,
    public void update(Long itemId, Item updateParam) {
        //id로 아이템 찾고
        Item findItem = findById(itemId);
        //파라미터로 들어온 id에 맞는 아이템에 새로운 아이템 이름, 가격, 수량 세팅
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());

        //판매 여부, 등록 지역, 상품 종류, 배송 방식
        findItem.setOpen(updateParam.getOpen());
        findItem.setRegions(updateParam.getRegions());
        findItem.setItemType(updateParam.getItemType());
        findItem.setDeliveryCode(updateParam.getDeliveryCode());
    }

    //테스트에서 쓰기 위해 만듬
    public void clearStore() {
        store.clear();
    }
}
