package app.rizpa.console.app;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;

public interface Parser {
    RizpaStockExchangeDescriptor parse(String filePath) throws Exception;
}
