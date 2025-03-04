package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsDAOFilelmpl implements ProductsDAO {
    private Map<String, Products> products = new HashMap<>();

    @Override
    public Products addProduct(String ProductType, Products product) {
        Products prevProduct = products.put(ProductType, product);

        return prevProduct;
    }

    @Override
    public List<Products> getAllProducts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Products getProduct(String ProductType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Products removeProduct(String ProductType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
