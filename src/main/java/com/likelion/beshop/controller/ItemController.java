package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.service.ItemImgService;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemImgService itemImgService;

    @GetMapping(value="/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }
    @PostMapping(value="/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            //ItemService에 만들었던 saveItem 호출해서 itemId 반환
            Long itemId = itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            //에러 발생 시 에러메세지 반환 후 폼으로 리다이렉트"
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value="/item/{itemId}")
    public String itemDtl(Model model, @PathVariable Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl.html";
    }

    //같은 함수를 오버로딩하는 것이므로 매개변수의 위치가 달라야 함
    @GetMapping(value="/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try{
            ItemFormDto itemFormDto=itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e){
            ItemFormDto newItemFormDto = new ItemFormDto();
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", newItemFormDto);
        }
        return "item/itemForm.html";
    }

    @PostMapping(value="/admin/item/{itemId}")
    public String updateItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) throws Exception {
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors().toString());
            return "item/itemForm.html";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다");
            return "item/itemForm.html";
        }
        try{
//            for (int i=0; i<itemImgFileList.size(); i++){
//                itemImgService.updateItemImg(itemFormDto.getItemImageIds().get(i), itemImgFileList.get(i));
//            }
            itemService.updateItem(itemFormDto, itemImgFileList);
        }
        catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm.html";
        }
        return "redirect:/";
    }
}
