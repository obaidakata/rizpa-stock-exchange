package app.console.app;

import app.console.app.generated.RizpaStockExchangeDescriptor;

public interface Parser {
    RizpaStockExchangeDescriptor parse(String filePath) throws Exception;
}
