const refreshRate = 1000; //milli seconds
let username = '';
let stockSymbol = '';

function getStockDetails() {
    $.ajax({
        url: "http://localhost:8080/rizpa/stockDetails",
        error: function (errorObject) {
            logMessage("Failed to get Stock Details");
        },
        success: function(stock) {
            updateStockDetails(stock);
        }
    });
}

function updateStockDetails(stock) {
    $("#stockLabel").text(stock.symbol + " stock");
    stockSymbol = stock.symbol;

    const stockDetailsTable = $("#stockDetailsTable");
    stockDetailsTable.empty();
    $('<tr>' +
        '<td> Symbol </td>' +
        '<td>' + stock.symbol +'</td>' +
        '</tr')
        .appendTo(stockDetailsTable);

    $('<tr>' +
        '<td> Companmy Name  </td>' +
        '<td>' + stock.companyName +'</td>' +
        '</tr')
        .appendTo(stockDetailsTable);

    $('<tr>' +
        '<td> Current Price </td>' +
        '<td>' + stock.price +'</td>' +
        '</tr')
        .appendTo(stockDetailsTable);

    $('<tr>' +
        '<td> MAHAZOR </td>' +
        '<td>' + stock.sumOfAllTransactions +'</td>' +
        '</tr')
        .appendTo(stockDetailsTable);
}

function getStockTransactions() {
    $.ajax({
        url: "http://localhost:8080/rizpa/stockTransactions",
        error: function (errorObject) {
            logMessage("Failed to get Stock Transactions");
        },
        success: function(transactins) {
            updateStockTransactions(transactins);
        }
    });
}

function updateStockTransactions(transactins) {
    //clear all current users
    const stockTransactionsTable = $("#stockTransactionsTable");
    stockTransactionsTable.empty();
    $('<tr>' +
        '<th> Time Stamp</th>' +
        '<th> Amount </th>' +
        '<th> Price </th>' +
        '</tr>')
        .appendTo(stockTransactionsTable);

    $.each(transactins || [], function(index, dealData) {
        $('<tr>' +
            '<th>' + dealData.timeStamp + '</th>' +
            '<th>' + dealData.amount + '</th>' +
            '<th>' + dealData.price + '</th>' +
            '</tr>')
            .appendTo(stockTransactionsTable);
    });
}

function onPageLoaded() {
    setInterval(getStockDetails, refreshRate);
    setInterval(getUser, refreshRate);
    setInterval(getStockTransactions, refreshRate);
}

let stockAmount = 0;
function getUserStockHolding() {
    $.ajax({
        url: "http://localhost:8080/rizpa/userStockHolding",
        method: 'get',
        error: function(errorObject) {
            console.log(errorObject.responseText);
        },
        success: function (userStockAmount) {
            stockAmount = userStockAmount;
            $('#userHoldingAmount').text("You have " + userStockAmount + " " +  stockSymbol + " stocks in you account");
        }
    })
}

function getUser() {
    $.ajax({
        url: "http://localhost:8080/rizpa/user",
        method: 'get',
        error: function(errorObject) {
            console.log(errorObject.responseText);
        },
        success: function (user) {
            if(user.userRole === "Trader") {
                $('#commandBox').show();
                $('#commands').hide();
                setInterval(getUserStockHolding, refreshRate);
                username = user.name;
            }
            else {
                getAdminData();
            }
        }
    })
}

function getAdminData() {
    $('#commandBox').hide();
    $('#messageList').hide();
    $('#systemMessagesLabel').hide();
    setInterval(getSellCommands, refreshRate);
    setInterval(getBuyCommands, refreshRate);
}

function getSellCommands() {
    $.ajax({
        url: "http://localhost:8080/rizpa/sellCommands",
        error: function(errorObject) {
            logMessage(errorObject.responseText);
        },
        success: function (sellCommands) {
            showCommands(sellCommands, "#sellCommandsTable");
        }
    })
}

function getBuyCommands() {
    $.ajax({
        url: "http://localhost:8080/rizpa/buyCommands",
        error: function(errorObject) {
            logMessage(errorObject.responseText);
        },
        success: function (buyCommands) {
            showCommands(buyCommands, "#buyCommandsTable");
        }
    })
}

function showCommands(commands, tableId) {
//clear all current users
    const table = $(tableId);
    table.empty();
    $('<tr>' +
        '<th> Time Stamp</th>' +
        '<th> Type </th>' +
        '<th> Amount </th>' +
        '<th> Price </th>' +
        '<th> Username </th>' +
        '</tr>')
        .appendTo(table);

    $.each(commands || [], function(index, command) {
        const dealData = command.dealData;

        $('<tr>' +
            '<th>' + dealData.timeStamp + '</th>' +
            '<th>' + command.type + '</th>' +
            '<th>' + dealData.amount + '</th>' +
            '<th>' + dealData.price + '</th>' +
            '<th>' + command.username + '</th>' +
            '</tr>')
            .appendTo(table);
    });
}



$(onPageLoaded);

function goBack() {
    window.location.replace("http://localhost:8080/rizpa/users-stocks/users-stocks.html");
}

function logMessage(message) {
    $("#messageList").append('<li>' + message + '</li>');
}

$(function () {
    $("#commandForm").submit(function (e) {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            method: 'post',
            error: function(errorObject) {
                logMessage(errorObject.responseText);
            },
            success: function (transactions) {
                showUserResults(transactions);
            }
        })
        return false;
    })
})

function showUserResults(transactions){
    transactions = transactions || [];

    if(transactions.length === 0) {
        logMessage("Command was successfully buy no transactions have been made");
    }
    else {
        $.each(transactions, (index, transaction) => {
            logMessage("New transaction : " + getTransactionDetails(transaction));
        })
    }
}

function getTransactionDetails(transaction) {
    const dealData = transaction.dealData;
    return  "timeStamp = " + dealData.timeStamp +
            ", type = " + transaction.type +
            ", amount = " + dealData.amount +
            ", price = " + dealData.price +
            ", Buyer Name = " + transaction.buyerName +
            ", Seller Name = " + transaction.sellerName

}

function commandTypeChange(select) {
    if(select.value === "MKT") {
        $('#priceInput').hide();
    }
    else {
        $('#priceInput').show();
    }
}

let selectedType = "Buy";

function directionChange(select) {
    selectedType = select.value;
    if(select.value === "Sell") {
        logMessage("On sell amount limited to you holding ( " +  stockAmount + " )");
        $('#amountInput').attr({
            "max" : stockAmount,
            "min" : 0
        });
        checkAmountLimit();
    }
    else {
        $('#amountInput').attr({
            "min" : 0
        });
    }
}

function checkAmountLimit() {
    const amountInput = $("#amountInput");
    if(selectedType === "Sell" && amountInput.val() > stockAmount) {
        logMessage("limit is " + stockAmount + " value reset to " + stockAmount);
        amountInput.val(stockAmount);
    }
    else if(amountInput.val() < 0){
        logMessage("Minimum amount value is 0, reset to 0");
        amountInput.val("0");
    }
}

function checkPriceLimit() {
    const priceInput = $("#priceInput");
    if(Number(priceInput.val()) < 0) {
        logMessage("Minimum price value is 0, reset to 0");
        priceInput.val("0");
    }
}