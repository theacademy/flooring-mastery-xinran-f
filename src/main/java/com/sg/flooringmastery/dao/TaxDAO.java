package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.util.List;

public interface TaxDAO {
    Tax addTax(String stateAbbreviation, Tax tax);

    List<Tax> getAllTaxes();

    Tax getTax(String state);

    Tax removeTax(String state);
}
