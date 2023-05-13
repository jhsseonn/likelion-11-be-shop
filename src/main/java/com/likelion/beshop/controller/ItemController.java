package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // HttpMethod=Get
    @GetMapping(value="/admin/item/new") // 경로 /admin/item/new
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());   //모델에 ItemFormDto를 itemFormDto라는 이름으로 담아서 데이터 넘겨줌
        return "item/itemForm"; // return 값은 itemForm.html
    }

    // HttpMethod=Post
    @PostMapping(value="/admin/item/new") // 경로 /admin/item/new
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        // 검증 오류 발생 시
        if (bindingResult.hasErrors()) {
            return "item/itemForm"; // itemForm.html 리턴
        }

        // 상품 등록 시, 첫 번째 이미지가 없다면
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다."); //에러 메시지와 함께
            return "item/itemForm"; // itemForm.html 값 리턴하여 상품 등록 페이지로 전환
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList); // 상품 정보를 가진 itemFormDto랑 상품 이미지 정보를 가진 itemImgFileList를 넘겨줌
        } catch (Exception e) { // 에러 발생 시
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다."); // 에러 메시지와 함께
            return "item/itemForm"; // itemForm.html 값 리턴하여 상품 등록 페이지로 전환
        }
        return "redirect:/";    // 정상 등록 시 메인 페이지로 이동 (root로 리다이렉트)
    }

    // HttpMethod=Get
    @GetMapping(value = "/item/{itemId}") //경로 {} 를 통해 url의 경로를 파라미터로 받아와 지정 가능
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){    // 상품 id에 따라 경로가 달라져야 해서 어노테이션과 함께 매개변수 설정
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);   //상품id로 상품 데이터 불러오기
        model.addAttribute("item", itemFormDto);    // 모델에 불러온 상품 데이터를 item이라는 이름으로 담아 데이터 넘겨주기
        return "item/itemDtl";  //itemDtl.html 리턴
    }

    @GetMapping(value="/admin/item/{itemId}") // 관리자만 접근 가능
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            // itemService의 getItemDtl을 이용해 아이템 정보를 가져와 itemFormDto에 넣기
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            // 해당 itemFormDto를 모델에 담아 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e) { //엔티티가 존재하지 않는 경우 예외처리
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model ) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get(): 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

}
