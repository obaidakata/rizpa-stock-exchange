package rizpa;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.descriptor.StockExchangeDescriptor;
import rizpa.generated.RizpaStockExchangeDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RizpaXmlParser {

    private final String JAXB_XML_PACKAGE_NAME = "rizpa.generated";
    private final String FILE_NOT_FOUND_MESSAGE = "File not found";
    private final String FILE_ISNT_XML_MESSAGE = "File format is not xml";
    private final String FILE_EXTENSTION = ".xml";
    private Gson gson = new Gson();

    public RizpaStockExchangeDescriptor parseOldData(String filePath) throws Exception {
        if (filePath == null) {
            throw new Exception(FILE_NOT_FOUND_MESSAGE);
        } else if (!filePath.endsWith(FILE_EXTENSTION)) {
            throw new Exception(FILE_ISNT_XML_MESSAGE);
        } else if (!(new File(filePath).exists())) {
            throw new Exception(FILE_NOT_FOUND_MESSAGE);
        }


        RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor;
        try (InputStream inputStream = new FileInputStream(filePath)) {
            rizpaStockExchangeDescriptor = deserializeFrom(inputStream);
        }

        return rizpaStockExchangeDescriptor;
    }

    private RizpaStockExchangeDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }

    public String saveAllData(String fileName, StockExchangeDescriptor descriptor) throws Exception {
        if (!Files.isDirectory(Paths.get(fileName))) {
            throw new Exception("Directory not found");
        }


        String xmlFile = fileName + File.separator + "savedData.json";
        try (Writer writer = new FileWriter(xmlFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(descriptor, writer);
        }

        return xmlFile;
    }

    public StockExchangeDescriptor loadPreviousData(String fileName) throws Exception {
        File filePath = new File(fileName);
        if (!filePath.exists()) {
            throw new Exception(FILE_NOT_FOUND_MESSAGE);
        } else if (filePath.isDirectory()) {
            throw new Exception("Must be file not directory");
        }

        return gson.fromJson(new FileReader(fileName), StockExchangeDescriptor.class);
    }
}
