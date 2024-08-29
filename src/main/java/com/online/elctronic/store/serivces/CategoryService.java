package com.online.elctronic.store.serivces;

import com.online.elctronic.store.dtos.CategoryDto;
import com.online.elctronic.store.dtos.PageableResponse;

public interface CategoryService {

    //Create
    CategoryDto create(CategoryDto categoryDto);

    //Update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //Delete
    void delete(String categoryId);

    //GetAll
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get Single
    CategoryDto get(String categoryId);


}
