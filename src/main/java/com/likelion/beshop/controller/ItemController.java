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
    @GetMapping(value="/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }


    @PostMapping(value="/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam(name="itemImgFile") List<MultipartFile> itemImgFileList) {
        // 검증 오류 발생 시
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        // 첫번째 이미지가 비어있거나 상품 아이디가 없는 경우
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        // 상품 등록
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; // 정상 등록 시, 메인 페이지로 이동
    }

    // 상품 조회
    @GetMapping(value="/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("itemFormDto", itemFormDto);
        return "item/itemDtl";
    }

    // 상품 상세 정보 조회 -> 수정 시 관리자 모드로 접근
    @GetMapping(value="/admin/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 아이템 정보를 가져와 Dto에 넣음
            model.addAttribute("itemFormDto", itemFormDto); // Dto를 모델에 담아 뷰로 전달

        } catch (EntityNotFoundException e) {
            ItemFormDto itemFormDto = new ItemFormDto();
            model.addAttribute("itemFormDto", itemFormDto);
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    // 상품 수정 후 수정 버튼 누르는 경우 POST 요청 경로 매핑
    @PostMapping(value="/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam(name="itemImgFile") List<MultipartFile> itemImgFileList, Model model) throws Exception {
        // 검증 오류 발생시
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        // 첫번째 이미지가 비어있거나 상품 아이디가 없는 경우
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        // 상품 정보 수정 itemService의 updateItem 호출
        try {
            // 호출 순서: itemService.updateItem() > itemImgService.updateItemImg() > itemImg.updateItemImg()
            itemService.updateItem(itemFormDto, itemImgFileList);

        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; // 정상 등록 시, 메인 페이지로 이동
    }

    // 상품 목록 조회 (상품 관리 화면)
    @GetMapping(value={"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }
}

