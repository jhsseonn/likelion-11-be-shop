package com.likelion.beshop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.Wildcard;
import org.apache.commons.lang3.StringUtils;
import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    //생성자 DI를 통해서 EntityManager 주입
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("createdBy",searchBy)){
            return QItem.item.id.like("%"+searchQuery+"%");
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

//        List<Item> content = results.getResults();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

//        if (total != 1) {
//            throw new IllegalStateException("Expected a single result, but found " + total + " results");
//        }

        return new PageImpl<>(content, pageable, total);
    }
}
