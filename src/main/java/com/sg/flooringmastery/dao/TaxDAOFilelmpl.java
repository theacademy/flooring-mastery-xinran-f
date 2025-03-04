package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxDAOFilelmpl implements TaxDAO {
    private Map<String, Tax> taxes = new HashMap<>();

    @Override
    public Tax addTax(String StateAbbreviation, Tax tax) {
        Tax prevTax = taxes.put(StateAbbreviation, tax);

        return prevTax;
    }

    @Override
    public List<Tax> getTaxes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tax getTax(String StateAbbreviation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tax removeTax(String StateAbbreviation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
