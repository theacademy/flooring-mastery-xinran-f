package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.util.List;

public interface TaxDAO {
    Tax addTax(String StateAbbreviation, Tax tax);

    List<Tax> getTaxes();

    Tax getTax(String StateAbbreviation);

    Tax removeTax(String StateAbbreviation);
}
