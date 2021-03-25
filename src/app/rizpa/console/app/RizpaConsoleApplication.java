package app.rizpa.console.app;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;

public class RizpaConsoleApplication {
    private final Parser parser;

    public RizpaConsoleApplication() {
        parser = new RizpaXmlParser();
    }

    public void run() {
        RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor  =  parser.parse("src/resources/ex1-small.xml");
        if(rizpaStockExchangeDescriptor != null){

        }
    }
}
