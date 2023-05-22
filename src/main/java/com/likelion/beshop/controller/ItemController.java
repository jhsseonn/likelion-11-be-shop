package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.ItemSearchDto;
import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {//검증 오류 발생
            return "item/itemForm";
        }
        //상품 등록 시, 첫 번째 이미지가 없을 경우
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환
        }
        // 상품 저장 로직 호출 - 매개변수로 상품 정보,이미지 정보를 담고있는 itemFormDto, itemImgFileList 넘겨줌
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) { //상품 등록 중 에러 발생
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환
        }
        return "redirect:/"; //정상 등록 시 메인페이지로 이동(리다이렉트)
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);

        } catch (EntityNotFoundException e) {
            model.addAttribute("itemFormDto", new ItemFormDto());
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환

        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {//검증 오류 발생
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) { //상품 등록 중 에러 발생
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환
        }
        return "redirect:/";
    }
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }
}
