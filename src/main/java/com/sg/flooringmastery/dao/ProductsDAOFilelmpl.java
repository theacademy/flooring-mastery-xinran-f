package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Products;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductsDAOFilelmpl implements ProductsDAO {
    private Map<String, Products> products = new HashMap<>();
    public static final String DELIMETER = ",";
    public static final String TAXES_FILE = "./src/main/java/com/sg/flooringmastery/SampleFileData/Data/Products.txt";

    @Override
    public Products addProduct(String ProductType, Products product) {
        Products prevProduct = products.put(ProductType, product);

        return prevProduct;
    }

    @Override
    public List<Products> getAllProducts() {
        loadProductsFile();

        return new ArrayList(products.values());
    }

    @Override
    public Products getProduct(String ProductType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Products removeProduct(String ProductType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void loadProductsFile() {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrdersPersistenceException(
                    "No products file found.", e);
        }

        String currentLine;
        Products currentProduct;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();

            if (!currentLine.startsWith("ProductType")) {
                currentProduct = unmarshallProduct(currentLine);
                products.put(currentProduct.getProductType(), currentProduct);
            }
        }

        scanner.close();
    }

    private Products unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMETER);
        Products productFromFile = new Products();

        // Index 0 - Product Type
        productFromFile.setProductType(productTokens[0]);

        // Index 1 - Cost Per Square Foot
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));

        // Index 2 - Labor Cost Per Square Foot
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

        return productFromFile;
    }
}
