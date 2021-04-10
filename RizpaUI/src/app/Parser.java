package app;

import app.generated.RizpaStockExchangeDescriptor;

public interface Parser {
    RizpaStockExchangeDescriptor parse(String filePath) throws Exception;
}
