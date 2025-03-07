package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class TaxDAOFilelmpl implements TaxDAO {
    private Map<String, Tax> taxes = new HashMap<>();
    public static final String DELIMETER = ",";
    public static final String TAXES_FILE = "./src/main/java/com/sg/flooringmastery/SampleFileData/Data/Taxes.txt";

    @Override
    public Tax addTax(String StateAbbreviation, Tax tax) {
        Tax prevTax = taxes.put(StateAbbreviation, tax);

        return prevTax;
    }

    @Override
    public List<Tax> getAllTaxes() {
        loadTaxesFile();

        return new ArrayList(taxes.values());
    }

    @Override
    public Tax getTax(String state) {
        loadTaxesFile();

        return taxes.get(state);
    }

    @Override
    public Tax removeTax(String stateAbbreviation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void loadTaxesFile() {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrdersPersistenceException(
                    "No taxes file found.", e);
        }

        String currentLine;
        Tax currentTax;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();

            if (!currentLine.startsWith("State")) {
                currentTax = unmarshallTax(currentLine);
                taxes.put(currentTax.getStateName(), currentTax);
            }
        }

        scanner.close();
    }

    private Tax unmarshallTax(String taxAsText) {
        String[] taxTokens = taxAsText.split(DELIMETER);
        Tax taxFromFile = new Tax();

        // Index 0 - State
        taxFromFile.setStateAbbreviation(taxTokens[0]);

        // Index 1 - State Name
        taxFromFile.setStateName(taxTokens[1]);

        // Index 2 - Tax Rate
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));

        return taxFromFile;
    }

}
