package app.rizpa.console.app;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RizpaXmlParser implements Parser{

    private final String  JAXB_XML_PACKAGE_NAME = "app.rizpa.console.app.generated";

    @Override
    public RizpaStockExchangeDescriptor parse(String filePath) {
        RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor = null;
        try {
            InputStream inputStream = new FileInputStream(filePath);
            rizpaStockExchangeDescriptor = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return rizpaStockExchangeDescriptor;
    }

    private RizpaStockExchangeDescriptor deserializeFrom(InputStream in) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }
}
