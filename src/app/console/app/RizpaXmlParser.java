package app.console.app;

import app.console.app.generated.RizpaStockExchangeDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class RizpaXmlParser implements Parser{

    private final String JAXB_XML_PACKAGE_NAME = "app.console.app.generated";
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

        return rizpaStockExchangeDescriptor;
    }

    private RizpaStockExchangeDescriptor deserializeFrom(InputStream in) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }



}
