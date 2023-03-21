package com.electonic.store.ElectonicStore.helper;

import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.UserDto;
import com.electonic.store.ElectonicStore.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {

        List<U> entity = page.getContent();

        List<V> dtos = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());


        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtos);
        response.setPageNumber(page.getNumber()+1);
        response.setPageSize(page.getSize());
        response.setTotalElement(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }

}
