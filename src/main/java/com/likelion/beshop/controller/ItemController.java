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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){ // 이미지들의 리스트를 받아오기 위한 파라미터 추가

        if(bindingResult.hasErrors()){ // 검증 오류 발생 시
            System.out.println(bindingResult.getAllErrors().toString());
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){ // 상품 등록 시, 첫 번째 이미지가 없다면
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try { // 상품 저장 로직 호출하여 매개변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemFormDto와 itemImgFileList를 넘겨줌
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm"; // 상품 등록 페이지로 전환
        }

        return "redirect:/"; // 정상 등록 시, 메인 페이지로 이동 = /(root)로 리다이렉트
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) { // 상품의 ID에 따라 경로가 달라져야하므로 그에 맞는 어노테이션과 함께 매개변수 설정

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 상품의 ID로 상품 데이터 불러오기
        model.addAttribute("item", itemFormDto); // 모델에 불러온 상품 데이터를 item이라는 이름으로 담아 데이터 넘겨주기
        return "item/itemDtl";
    }

    @GetMapping(value = "/admin/item/{itemId}") // 관리자만 접근 가능하게 설정
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // itemService의 getItemDtl을 이용해 아이템 정보를 가져와 itemFormDto에 넣기
            model.addAttribute("itemFormDto", itemFormDto); // 해당 itemFormDto를 모델에 담아 뷰로 전달
        } catch (EntityNotFoundException e){ // try-catch문 사용해 엔티티가 존재하지 않는 경우 예외처리
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto()); // 예외처리시 뷰로 전달하는 itemFormDto는 새로 생성
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, //이 때, itemFormDto 제약조건 검증하고 itemImgFileList는 @RequestParam 사용해 itemImgFile을 매개변수로 받아오기
                             @RequestParam("itemImgFile") List<MultipartFile> itemFileList, Model model){
        if(bindingResult.hasErrors()){ // bindingResult 이용해 유효성 검증시 에러가 발생했을 때 itemForm 페이지 유지
            return "item/itemForm";
        }

        // itemImgFileList의 0번째 인덱스가 비어있거나 itemFormDto에 상품 아이디가 존재
        // 하지 않을 경우 "첫번째 상품 이미지는 필수 입력 값입니다" 에러 메시지 띄우고
        // itemForm 페이지 유지
        if(itemFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        // try-catch문으로 상품 수정 로직 추가하고 에러 예외처리
        try{
            itemService.updateItem(itemFormDto, itemFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; // 모든 로직이 성공했을 시 상위 경로로 돌아가기
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) // 아이템 리스트 볼 수 있는 경로와 아이템 상세 페이지(수정페이지) 볼 수 있는 경로 2가지로 설정
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); // 한 페이지에 볼 수 있는 아이템 개수 3개로 제한

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); // 조회 조건과 페이징 정보를 파라미터로 넘겨 Page<Item> 객체 반환 받기

        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달(items)
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("maxPage", 5); // 상품 관리 메뉴 하단에 보여줄 페이지 번호 최대 개수 5로 설정(maxPage)
        return "item/itemMng"; // 최종적으로 item/itemMng로 이동
    }

}
