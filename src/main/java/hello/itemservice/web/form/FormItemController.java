package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//log남기기위해 log객체 생성해주는
@Slf4j
//반환값 논리경로
@Controller
//basic/items로 들어오면
@RequestMapping("/form/items")
//lombok애노테이션, final이 붙은 필드 생성자를 자동으로 만들어줌
//때문에 의존주입을 위해 final 절대 떼면 안됨
//ItemRepository클래스는 @Repository가 붙어 빈으로 등록되므로 생성자가 하나여서 자동으로 @Autowired 붙음
@RequiredArgsConstructor
public class FormItemController {

    //스프링을 쓰니 final로
    private final ItemRepository itemRepository;

    /**
     * 이 컨트롤러 FormItemController의 어떤걸 호출하든 @ModelAttribute에 명시한 regions이름으로 반환값 regions객체를 넘겨줌
     * model.addAttribute(regions, regions), 자동으로 모델에 담기는
     * 상세, 수정, 아이템 모두 같이 보여줘야 하기 때문에
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        //순서를 보장하기 위해LinkedHashMap사용
        Map<String, String> regions = new LinkedHashMap<>();

        //키, 값
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }

    //itemTypes 를 등록 폼, 조회, 수정 폼에서 모두 사용하므로 @ModelAttribute
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {

        //ItemType.values()는 enum인 ItemType의 타입 BOOK, FOOD, ETC를 다 념겨줌
        return ItemType.values();
    }

    //DeliveryCode 등록 폼, 조회, 수정 폼에서 모두 사용하므로 @ModelAttribute
    //이 FormItemController에 있는 메서드가 호출 될 때마다 호출되 리스트가 만들어짐
    //때문에 어딘가에서 미리 생성해놓고 생성된걸 불러쓰는게 효율적
    //static식으로 만들어놓고 공유해서 쓰는게 나음
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        //DeliveryCode타입의 리스트
        List<DeliveryCode> deliveryCodes = new ArrayList<>();

        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));

        return deliveryCodes;
    }

    //basic/items로 들어오면
    //목록을 출력하는
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        //items이름으로 items값을 넣어 추가
        model.addAttribute("items", items);
        //basic/items논리경로 이쪽으로 보내기
        return "form/items";
    }

    //상품 등록 폼의 URL과 실제 상품 등록을 처리하는 URL을 똑같이 맞추고 HTTP 메서드로 두 기능을 구분
    //이렇게 하면 하나의 URL로 등록 폼과, 등록 처리를 깔끔하게 처리할 수 있다.

    //Get방식으로 add로 들어오면
    @GetMapping("/add")
    //타임리프를 쓰기 위해 모델을 넘겨줌, form/addForm에서 th:object로 받음
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

        return "form/addForm";
    }

    //Post방식으로 add로 들어오면
    //@RequestParam으로 요청 파라미터 받는 방식 설명
    //파라미터 다 변수로 받아 Item객체에 세팅
    //객체 저장소에 저장하고 Model객체에 추가
//    @PostMapping("/add")
//    public String addItemV1(@RequestParam String itemName,
//                            @RequestParam int price,
//                            @RequestParam Integer quantity,
//                            Model model) {
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);
//        itemRepository.save(item);
//        model.addAttribute("item", item);
//
//        return "basic/item";
//    }

    /**
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
//    //Post방식으로 add로 들어오면
//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
//        //@ModelAttribute 파라미터 이름 설정 안하면
//        //기본값은 지정해준 클래스의 첫글자만 소문자로 바꿔 넣어줌 즉 똑같이 item
//        //@ModelAttribute가 Item객체 만들고 값 세팅 다 해줌
//        //@ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력
//        //한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute로 지정한 객체를 자동으로 넣어준다
//
//        itemRepository.save(item);
//        //model.addAttribute("item", item);
//        //자동 추가, 생략 가능 @ModelAttribute가 자동으로 넣어줌
//        //@ModelAttribute 파라미터 이름과 addAttribute 파라미터 이름 같음
//       return "basic/item";
//    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
//    @PostMapping("/add")
//    public String addItemV4(Item item) {
//        itemRepository.save(item);
//        return "basic/item";
//    }

    /**
     * PRG - Post/Redirect/Get
     */
    //Post방식으로 add로 들어오면
    //String이나 단순 타입이 오면 @RequestParam 적용
    //Item같이 직접 만든 타입은 @ModelAttribute 적용
//    @PostMapping("/add")
//    public String addItemV5(Item item) {
//        //@ModelAttribute 기본값은 지정해준 클래스의 첫글자만 소문자로 바꿔 넣어줌 즉 똑같이 item
//        itemRepository.save(item);
//
//        //redirect로 해주지 않으면 상품 등록 후 상품 상세 화면 http://localhost:8080/basic/items/add에서
//        //새로고침 할 때 마다 같은 item 추가됨
//        //그래서 등록하면 add가 아닌 /basic/items/id로
//       return "redirect:/basic/items/" + item.getId();
//    }

    /**
     * RedirectAttributes
     */
    //상품을 저장하고 상품 상세 화면으로 리다이렉트 한 것 까지는 좋았다.
    //그런데 고객 입장에서 저장이 잘 된 것인지 안 된 것인지 확신이 들지 않는다.
    //그래서 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다" 라는 메시지를 보여달라는 요구사항이 왔다. 간단하게 해결
    //RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible , 쿼리 파라미터까지 처리
    @PostMapping("/add")
    public String addItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        //redirect할때 파라미터를 붙여 보내는

        //상품 등록 폼에서 판매 오픈 체크를 하고 등록하면 이 로그 출력, 체크 안하면 null출력
        //체크 박스를 체크하면 HTML Form에서 open=on 이라는 값이 넘어간다.
        //스프링은 on 이라는 문자를 true 타입으로 변환해준다. 스프링 타입 컨버터가 이 기능을 수행
        //HTML checkbox는 선택이 안되면 클라이언트에서 서버로 값 자체를 보내지 않는다. 수정의 경우에는
        //상황에 따라서 이 방식이 문제가 될 수 있다. 사용자가 의도적으로 체크되어 있던 값을 체크를 해제해도
        //저장시 아무 값도 넘어가지 않기 때문에, 서버 구현에 따라서 값이 오지 않은 것으로 판단해서 값을 변경하지
        //않을 수도 있다. 이런 문제를 해결하기 위해서 스프링 MVC는 약간의 트릭을 사용하는데, 히든 필드를 하나 만들어서,
        //_open 처럼 기존 체크 박스 이름 앞에 언더스코어( _ )를 붙여서 전송하면 체크를 해제했다고 인식할 수 있다.
        //히든 필드는 항상 전송된다. 따라서 체크를 해제한 경우 여기에서 open 은 전송되지 않고, _open 만 전송되는데,
        //이 경우 스프링 MVC는 체크를 해제했다고 판단, db에 true false인거 판단 후 넣기 때문에
        log.info("item.open={}", item.getOpen());

        //item클래스의 리스트 regions에 들어간걸 추출
        log.info("item.regions={}", item.getRegions());

        //배열이 아니라 단일 선택이므로 하나만 들어갈 수 있음, 체크 안하면 null들어감, 체크 안해도됨, 히든 필드 안만듬
        log.info("item.itemType={}", item.getItemType());

        //저장된 item객체를 갖고옴
        Item savedItem = itemRepository.save(item);

        //저장된 item객체의 id를 값으로
        redirectAttributes.addAttribute("itemId", savedItem.getId());

        //status가 true면 저장이 되서 넘어온거다
        redirectAttributes.addAttribute("status", true);

        //itemId가 치환이 되어 넘어가 바인딩, url에 못들어가면 쿼리파라미터 ?status=true 이렇게
        // /form/items/{itemId} 이 페이지에 작업 해야함
        return "redirect:/form/items/{itemId}";
    }

    //Get으로 itemId들어오고 edit들어오면, 상품 수정 폼
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {

        //id에 맞는 Item객체 갖고와 대입
        Item item = itemRepository.findById(itemId);

        //Model객체에 추가
        model.addAttribute("item", item);
        return "form/editForm";
    }

    //Post로 itemId들어오고 edit들어오면, 상품 수정 처리
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {

        //같은id에 다른 item객체를 넣어 값 바꾸기
        itemRepository.update(itemId, item);

        //스프링은 redirect:/... 으로 편리하게 리다이렉트를 지원
        //redirect로 이동, url자체가 완전 바뀜
        //@PathVariable에 있는 itemId 쓸 수 있게 해줌
        //상품 수정 폼에서 저장 버튼 누르면 바뀜
        //HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용할 수 있다.
        //PUT, PATCH는 HTTP API 전송시에 사용
        //스프링에서 HTTP POST로 Form 요청할 때
        // 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청상 POST 요청
        return "redirect:/form/items/{itemId}";
    }

    //url에 itemId 들어오면
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {

        //id에 맞는 item객체 찾기
        Item item = itemRepository.findById(itemId);

        //Model객체에 item이라는 이름으로 item객체 담음
        model.addAttribute("item", item);

        return "form/item";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

}
