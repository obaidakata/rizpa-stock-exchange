package app.rizpa.console.app;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.console.app.generated.RseStock;
import app.rizpa.console.app.generated.RseStocks;
import app.rizpa.data.Stock;
import javafx.util.Pair;
import sun.jvm.hotspot.debugger.SymbolLookup;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RizpaXmlParser implements Parser{

    private final String JAXB_XML_PACKAGE_NAME = "app.rizpa.console.app.generated";
    private final String FILE_NOT_FOUND_MESSAGE = "File not found";
    private final String FILE_ISNT_XML_MESSAGE = "File format is not xml";
    private final String SYMBOLS_ARE_NOT_UNIQUE = "Symbols are not unique";
    private final String COMPANIES_NAMES_ARE_NOT_UNIQUE = "Companies names are not unique";
    private final String FILE_EXTENSTION = ".xml";


    @Override
    public RizpaStockExchangeDescriptor parse(String filePath) throws Exception {
        if(filePath == null) {
            throw new Exception(FILE_NOT_FOUND_MESSAGE);
        }
        else if(!filePath.endsWith(FILE_EXTENSTION)) {
            throw new Exception(FILE_ISNT_XML_MESSAGE);
        }
        else if(!(new File(filePath).exists())) {
            throw new Exception(FILE_NOT_FOUND_MESSAGE);
        }


        RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor;
        try(InputStream inputStream = new FileInputStream(filePath)) {
            rizpaStockExchangeDescriptor = deserializeFrom(inputStream);
        }

        if(!isAllStocksSymbolUnique(rizpaStockExchangeDescriptor)){
            throw new Exception(SYMBOLS_ARE_NOT_UNIQUE);
        }
        else if(!isAllCompaniesNamesUnique(rizpaStockExchangeDescriptor)) {
            throw new Exception(COMPANIES_NAMES_ARE_NOT_UNIQUE);
        }

        return rizpaStockExchangeDescriptor;
    }

    private RizpaStockExchangeDescriptor deserializeFrom(InputStream in) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }

    private boolean isAllStocksSymbolUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isAllUnique = true;
        try {
            List<String> symbols = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseSymbol)
                    .collect(Collectors.toList());

            if(symbols.stream().distinct().count() < symbols.size()) {
                isAllUnique = false;
            }
        } catch (NullPointerException ignored) { }

        return isAllUnique;
    }

    private boolean isAllCompaniesNamesUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isAllUnique = true;
        try {
            List<String> companiesNames = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseCompanyName)
                    .collect(Collectors.toList());

            if(companiesNames.stream().distinct().count() < companiesNames.size()) {
                isAllUnique = false;
            }
        } catch (NullPointerException ignored) { }

        return isAllUnique;
    }

}
