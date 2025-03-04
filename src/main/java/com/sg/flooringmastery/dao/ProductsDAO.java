package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Products;

import java.util.List;

public interface ProductsDAO {
    Products addProduct(String ProductType, Products product);

    List<Products> getAllProducts();

    Products getProduct(String ProductType);

    Products removeProduct(String ProductType);
}
