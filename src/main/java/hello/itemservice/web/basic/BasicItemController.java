package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
//basic/items로 들어오면
@RequestMapping("/basic/items")
//lombok애노테이션, final이 붙은 필드 생성자를 자동으로 만들어줌
//때문에 의존주입을 위해 final 절대 떼면 안됨
//ItemRepository클래스는 @Repository가 붙어 빈으로 등록되므로 생성자가 하나여서 자동으로 @Autowired 붙음
@RequiredArgsConstructor
public class BasicItemController {

    //스프링을 쓰니 final로
    private final ItemRepository itemRepository;

    //basic/items로 들어오면
    //목록을 출력하는
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        //items이름으로 items값을 넣어 추가
        model.addAttribute("items", items);
        //basic/items논리경로 이쪽으로 보내기
        return "basic/items";
    }

    //상품 등록 폼의 URL과 실제 상품 등록을 처리하는 URL을 똑같이 맞추고 HTTP 메서드로 두 기능을 구분
    //이렇게 하면 하나의 URL로 등록 폼과, 등록 처리를 깔끔하게 처리할 수 있다.

    //Get방식으로 add로 들어오면
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
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
     * RedirectAttributes 개씨발중요
     */
    //상품을 저장하고 상품 상세 화면으로 리다이렉트 한 것 까지는 좋았다.
    //그런데 고객 입장에서 저장이 잘 된 것인지 안 된 것인지 확신이 들지 않는다. 그래서 저장이 잘 되었으면 상품 상세 화면에
    // "저장되었습니다"라는 메시지를 보여달라는 요구사항이 왔다. 간단하게 해결
    //RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible , 쿼리 파라미터까지 처리
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        //redirect할때 파라미터를 붙여 보내는

        //저장된 item객체를 갖고옴
        Item savedItem = itemRepository.save(item);
        //저장된 item객체의 id를 값으로
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        //status가 true면 저장이 되서 넘어온거다
        redirectAttributes.addAttribute("status", true);

        //itemId가 치환이 되어 넘어가 바인딩, url에 못들어가면 쿼리파라미터 ?status=true 이렇게
        // /basic/items/{itemId} 이 페이지에 작업 해야함
        return "redirect:/basic/items/{itemId}";
    }

    //Get으로 itemId들어오고 edit들어오면, 상품 수정 폼
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        //id에 맞는 Item객체 갖고와 대입
        Item item = itemRepository.findById(itemId);
        //Model객체에 추가
        model.addAttribute("item", item);
        return "basic/editForm";
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
        return "redirect:/basic/items/{itemId}";
    }

    //url에 itemId 들어오면
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        //id에 맞는 item객체 찾기
        Item item = itemRepository.findById(itemId);
        //Model객체에 item이라는 이름으로 item객체 담음
        model.addAttribute("item", item);

        return "basic/item";
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
