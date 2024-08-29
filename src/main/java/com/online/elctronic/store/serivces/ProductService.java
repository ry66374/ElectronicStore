package com.online.elctronic.store.serivces;

import com.online.elctronic.store.dtos.PageableResponse;
import com.online.elctronic.store.dtos.ProductDto;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface ProductService {

    //Create
    ProductDto create(ProductDto productDto);

    //Update
    ProductDto update(ProductDto productDto, String productId);

    //Delete
    void delete( String productId);

    //GetALl
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //getSingle
    ProductDto get(String productId);

    //Get All Live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    //Search
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    //Create product with category

    ProductDto createWithCategory(ProductDto productDto, String categoryId);
    ProductDto updateCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);



}
