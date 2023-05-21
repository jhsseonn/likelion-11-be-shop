package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto_.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    //itemRepositoryCustomImpl 생성자 함수 만들기(파라미터로 엔티티 매니저 받기)

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }
    //searchSellStatus가 null일 경우 null 반환하고 null이 아닐 경우 판매중/품절 상태에 해당하는 상품만 조회

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); //현재 시간 받아서 dateTime에 저장

        //searchDateType에 따라 반환 값이 달라짐
        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null; //all이거나 null일 경우 -> null 반환
        } else if (StringUtils.equals("id", searchDateType)) {
            dateTime = dateTime.minusDays(1); //1d : dateTime에서 하루 뺸 값 반환
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime); //최종적으로 세팅이 끝난 시간 리턴
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

       //searchBy 값에 따라 조건값 반환
        //itemName : 상품명    createdBy : 상품 생성자 아이디
        if(StringUtils.equals("itemName",searchBy)){
            return QItem.item.itemName.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        List<Item> content = queryFactory //쿼리 생성
                .selectFrom(QItem.item) //상품 데이터를 조회하기 위해 Qitem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), //BooleanExpression 반환하는 조건문들
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc()) //아이템 id 내림차순으로 나열
                .offset(pageable.getOffset()) //데이터를 가지고 올 시작 인덱스 지정
                .limit(pageable.getPageSize())//한번에 가지고 올 최대 개수 지정
                .fetch(); //조회대상 리스트로 반환

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne(); //쿼리팩토리에서 where 절로 해당하는 데이터를 불러와 fetchOne으로 조회대상이 한건이면 해당 타입반환, 아니면 에러 발생

        return new PageImpl<>(content,pageable,total);


//        long total = queryFactory.select(Wildcard.count).from(QItem.item)
//                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
//                    total = results.getTotal()
//                .fetchOne();
//        return new PageImpl<>(content, pageable, total);


//        List<Item> content = results.getResults();
//
//        long total = results.getTotal();
//
//        return  new PageImpl<>(content,pageable,total);
    }
}
