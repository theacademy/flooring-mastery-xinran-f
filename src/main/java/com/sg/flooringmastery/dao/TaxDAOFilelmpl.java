package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.util.List;

public class TaxDAOFilelmpl implements TaxDAO {
    @Override
    public Tax addTax(String StateAbbreviation, Tax tax) {
        throw new UnsupportedOperationException("Not supported yet.");
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
