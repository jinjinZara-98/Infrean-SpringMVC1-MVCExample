package hello.itemservice;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {

	ItemRepository itemRepository = new ItemRepository();

	//테스트할때 마다 저장소 비워주게 설정
	@AfterEach
	void afterEach() {
		itemRepository.clearStore();
	}

	@Test
	void save() {
		//given, Item객체 생성
		Item item = new Item("itemA", 10000, 10);

		//when
		Item savedItem = itemRepository.save(item);

		//then, 저장된 값과 조회된 값이 같은지
		Item findItem = itemRepository.findById(item.getId());
		//alt enter 쳐서 static import
		assertThat(findItem).isEqualTo(savedItem);
	}
	@Test
	void findAll() {
		//given
		Item item1 = new Item("item1", 10000, 10);
		Item item2 = new Item("item2", 20000, 20);
		itemRepository.save(item1);
		itemRepository.save(item2);

		//when
		List<Item> result = itemRepository.findAll();

		//then, 배열 크기와, 객체들을 갖고있는가
		assertThat(result.size()).isEqualTo(2);
		assertThat(result).contains(item1, item2);
	}
	@Test
	void updateItem() {
		//given, 먼저 바꿀 아이템 저장
		Item item = new Item("item1", 10000, 10);
		Item savedItem = itemRepository.save(item);
		//자장된 Item객체 아이디 가져와 대입
		Long itemId = savedItem.getId();

		//when, 원래 저장했던 Item Id 갖고오고 새로운 객체 update메서드에 대입
		Item updateParam = new Item("item2", 20000, 30);
		itemRepository.update(itemId, updateParam);
		//id를 파라미터로 넣어 Item객체가 달라졌는지 확인
		Item findItem = itemRepository.findById(itemId);

		//then, 저장소에 넣기 전 후 Item객체 이름, 가격, 수량이 같은지 비교
		assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
		assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
		assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
	}
}
