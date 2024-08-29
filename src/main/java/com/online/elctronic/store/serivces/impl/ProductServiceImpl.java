package com.online.elctronic.store.serivces.impl;

import com.online.elctronic.store.dtos.PageableResponse;
import com.online.elctronic.store.dtos.ProductDto;
import com.online.elctronic.store.entities.Category;
import com.online.elctronic.store.entities.Product;
import com.online.elctronic.store.exceptions.ResourceNotFoundException;
import com.online.elctronic.store.helper.Helper;
import com.online.elctronic.store.repositories.CategoryRepository;
import com.online.elctronic.store.repositories.ProductRepository;
import com.online.elctronic.store.serivces.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);

        String id = UUID.randomUUID().toString();
        product.setId(id);

        product.setAddedDate(new Date());


        Product savedProduct =  productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Not found this id"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Not found this id"));
        productRepository.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize ,sort);
        Page<Product> page = productRepository.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto get(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Not found this id"));
        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize ,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize ,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        //fetch the category
        Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
        Product product = modelMapper.map(productDto, Product.class);

        //fetch the product id
        String productId = UUID.randomUUID().toString();
        product.setId(productId);

        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }


    public ProductDto updateCategory(String productId, String categoryId){

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product id not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category id is not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct,ProductDto.class);
    }

    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir){
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category id is not found"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
