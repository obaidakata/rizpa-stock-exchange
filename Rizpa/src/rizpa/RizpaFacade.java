package rizpa;

import app.RizpaConsoleApplication;
import engine.*;
import engine.command.Command;
import engine.command.CommandDirection;
import engine.command.CommandTressSet;
import engine.command.CommandType;
import engine.descriptor.Commands;
import engine.descriptor.Stock;
import engine.descriptor.StockExchangeDescriptor;
import rizpa.generated.RizpaStockExchangeDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RizpaFacade {
    private final RizpaDataConverter converter;
    private final RizpaXmlParser parser;

    private final RizpaEngine rizpaEngine;

    public RizpaFacade() {
        this.converter = new RizpaDataConverter();
        this.rizpaEngine = new RizpaEngine();
        this.parser =new RizpaXmlParser();
    }

    public Collection<DealData> getTransactions(String symbol) {
        return rizpaEngine.getStockTransactions(symbol)
                .stream()
                .map(Transaction::getDealData)
                .collect(Collectors.toList());
    }

    public List<Stock> getAllStocks() {
        return rizpaEngine.getAllStocks();
    }

    public void loadNewData(String filePath) throws Exception {
        RizpaStockExchangeDescriptor newRizpaStockExchangeDescriptor = parser.parseOldData(filePath);
        StockExchangeDescriptor descriptor = converter.convert(newRizpaStockExchangeDescriptor);
        checkData(descriptor);
        rizpaEngine.loadNewData(descriptor);
    }

    private void checkData(StockExchangeDescriptor descriptor) throws Exception {
        if(!rizpaEngine.isAllStocksSymbolUnique(descriptor)){
            String SYMBOLS_ARE_NOT_UNIQUE = "Symbols are not unique";
            throw new Exception(SYMBOLS_ARE_NOT_UNIQUE);
        }
        else if(!rizpaEngine.isAllCompaniesNamesUnique(descriptor)) {
            String COMPANIES_NAMES_ARE_NOT_UNIQUE = "Companies names are not unique";
            throw new Exception(COMPANIES_NAMES_ARE_NOT_UNIQUE);
        }
    }

    public void doLimitCommand(String direction, String symbol, int amount, int limit) {
        CommandDirection commandDirection = direction.equalsIgnoreCase("Buy") ? CommandDirection.Buy : CommandDirection.Sell;
        rizpaEngine.doLimitCommand(commandDirection, symbol, amount, limit);
    }

    public Collection<String> getAllSymbols(){
        return rizpaEngine.getAllSymbols();

    }

    public List<String> getDirections() {
        return Stream.of(CommandDirection.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<DealData> getSellOffers(String stockSymbol) {
        return rizpaEngine.getSellOffers(stockSymbol);
    }

    public Collection<DealData> getBuyOffers(String stockSymbol) {
        return rizpaEngine.getBuyOffers(stockSymbol);
    }

    public int getTransactionsCount(String symbol) {
        Collection<Transaction> transactions = rizpaEngine.getStockTransactions(symbol);
        return transactions != null? transactions.size() : 0;
    }

    public Stock getStockBySymbol(String symbol) {
        return rizpaEngine.getStockBySymbol(symbol);
    }

    public int getSumSellOffers(String stockSymbol) {
        return rizpaEngine.getSumSellOffers(stockSymbol);
    }

    public int getSumBuyOffers(String stockSymbol) {
        return rizpaEngine.getSumBuyOffers(stockSymbol);
    }

    public int getSumTransactions(String stockSymbol) {
        return rizpaEngine.getSumTransactions(stockSymbol);
    }


    public String saveAllData(String fileName) throws Exception {
        return parser.saveAllData(fileName, rizpaEngine.getDescriptor());
    }

    public void loadPreviousData(String fileName) throws Exception {
        rizpaEngine.setDescriptor(parser.loadPreviousData(fileName));
    }
}
