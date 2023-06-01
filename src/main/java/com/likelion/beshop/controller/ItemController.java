package com.likelion.beshop.controller;

import com.likelion.beshop.dto_.ItemFormDto;
import com.likelion.beshop.dto_.ItemSearchDto;
import com.likelion.beshop.dto_.MemberFormDto;
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
import javax.validation.constraints.Null;
//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService; //final 변수 -> 한번 초기화되면 그 이후로는 값 변경 x

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){ //이미지들의 리스트를 받아오기 위한 파라미터 추가
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors().toString());
            return"item/itemForm"; //검증 오류 발생시, itemForm.html 리턴
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() ==null){ //상품 등록시 첫번째 이미지가 없다면 에러 메세지와 함께
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다,");
            return "item/itemForm"; //itemForm.html 값 리턴하여 상품 등록 페이지로 전환
        }

        try{
            System.out.println("실패");
            itemService.saveItem(itemFormDto,itemImgFileList); //상품 저장 로직 호출, 매개변수로 상품 정보 + 이미지정보 담고 있는 디티오+ 리스트 넘겨줌
            System.out.println("성공");
        }catch (Exception e){
            model.addAttribute("errorMassage", "상품 등록 중 에러가 발생하였습니다");
            return "item/itemForm"; //상품 등록 페이지로 전환
        }

        return "redirect:/"; //정상 등록 시 , 메인 페이지로 이동
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){


        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);

        return "item/itemDtl";

    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){

        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMassage","존재하지 않는 상품입니다");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile")List<MultipartFile> itemFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemFileList.get(0).isEmpty() && itemFormDto.getId()== null){
            model.addAttribute("errorMassage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto,itemFileList);
        }catch(Exception e){
            model.addAttribute("errorMassage","오류오류");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) //아이템 리스트 볼 수 있는 경로, 아이템 상세 페이지 볼 수 있는 경로
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page") Optional<Integer> page,
                             Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); //한페이지에 볼 수 있는 아이템 개수 3개로 제한
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto,pageable); //조회 조건과 페이징 정보를 파라미터로 넘겨 page<item> 객체 반환 받기
        model.addAttribute("items",items); //조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("itemSearchDto",itemSearchDto); //페이지 전환시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("maxPage",5); //상품 관리 메뉴 하단에 보여줄 페이지 번호 최대 개수 5로 설정
        return "item/itemMng";

    }
}
