package app.rizpa.console.app;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.console.app.generated.RseStock;

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

        //Move to the engine

        return rizpaStockExchangeDescriptor;
    }

    private RizpaStockExchangeDescriptor deserializeFrom(InputStream in) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }



}
