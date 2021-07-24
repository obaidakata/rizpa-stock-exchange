const refreshRate = 1000; //milli seconds
let selectedIndex = 0;
let selectedStockSymbol = '';
let userBalance = 0;
let username = "";
function getUserBalance() {
    $.ajax({
        url: "http://localhost:8080/rizpa/userBalance",
        success: function(userBalance) {
            const userBalanceDev = $("#userBalance");
            userBalanceDev.empty();
            $('<h4> Your balance is: ' + userBalance + ' </h4>')
                .appendTo(userBalanceDev);
        }
    });
}


function getUser() {
    var fileChooser = $("#fileChooser")
    fileChooser.hide()
    $.ajax({
        url: "http://localhost:8080/rizpa/user",
        timeout: 2000,
        method: 'get',
        error: function(errorObject) {
            console.log(errorObject.responseText);
        },
        success: function (user) {
            let hiMessage = "Hi " + user.name + ", Role : " ;
            if(user.userRole === "Trader") {
                fileChooser.show()
                setInterval(getUserBalance, refreshRate);
                setInterval(getTransactionsRecord, refreshRate);
                username = user.name;
                hiMessage = hiMessage + "Trader";
            }
            else {
                hiMessage = hiMessage + "Admin";
            }

            $("#hiLabel").text(hiMessage)
        }
    })
}

function logMessage(message) {
    $("#messageList").append('<li>' + message + '</li>');
}

function uploadFile() {
    $("#uploadForm")
        .submit(function() {

            const file = this[0].files[0];

            const formData = new FormData();
            formData.append("fake-key-1", file);

            $.ajax({
                method:'POST',
                data: formData,
                url: this.action,
                processData: false, // Don't process the files
                contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                timeout: 4000,
                error: function(e) {
                    logMessage("Failed to upload file");
                    logMessage("Failed to get result from server " + e);
                },
                success: function(r) {
                    logMessage("File loaded successfully");
                }
            });

            // return value of the submit operation
            // by default - we'll always return false so it doesn't redirect the user.
            return false;
    })
}

function refreshUsersList(users) {
    //clear all current users
    const usersTable = $("#usersTable");
    usersTable.empty();
    $('<tr>' +
        '<th> User Role </th>' +
        '<th> Username </th>' +
        '</tr>')
        .appendTo(usersTable);

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        $('<tr>' +
            '<th>' + user.userRole + '</th>' +
            '<th>' + user.name + '</th>' +
        '</tr>')
            .appendTo(usersTable);
    });
}

function ajaxUsersList() {
    $.ajax({
        url: "http://localhost:8080/rizpa/users",
        success: function(users) {
            refreshUsersList(users);
        }
    });
}


function refreshHoldingsList(stocks) {
    const stocksTable = $("#stocksTable");
    stocksTable.empty();
    $('<tr>' +
        '<td> Company name </td>' +
        '<td> Symbol </td>' +
        '<td> Current price </td>' +
        '<td> Sum Price </td>' +
        '</tr>')
        .appendTo(stocksTable);


    $.each(stocks || [], function(index, stock) {
        let rowStart = (index === selectedIndex) ? '<tr class="selected">' : '<tr>';

        const rowString =  rowStart +
            '<td>' + stock.companyName + '</td>' +
            '<td>' + stock.symbol + '</td>' +
            '<td>' + stock.price + '</td>' +
            '<td>' + stock.sumOfAllTransactions + '</td>' +
            '</tr>';

        $(rowString).click(function () {
            $('.selected').removeClass('selected');
            $(this).addClass("selected");
            selectedIndex = index;
        }).appendTo(stocksTable);

        if(index === selectedIndex) {
            selectedStockSymbol = stock.symbol;
            $("#ok").text("Go to " + stock.companyName)
        }
    });
}



function getSystemHoldings() {
    $.ajax({
        url: "http://localhost:8080/rizpa/stocks",
        success: function(stocks) {
            refreshHoldingsList(stocks);
        }
    });
}

function refreshTransactionsRecordList(transactionsRecord) {
    const transactionsRecordTable = $("#transactionsRecordTable");
    transactionsRecordTable.empty();
    $('<tr>' +
        '<td> Number </td>' +
        '<td> Type </td>' +
        '<td> Symbol </td>' +
        '<td> Time stamp </td>' +
        '<td> Price </td>' +
        '<td> Balance Before </td>' +
        '<td> Balance After </td>' +
        '</tr>')
        .appendTo(transactionsRecordTable);

    $.each(transactionsRecord || [], function(index, transactionRecord) {
        index = index + 1;
        $('<tr>' +
            '<td>' + index  + '</td>' +
            '<td>' + transactionRecord.transactionType + '</td>' +
            '<td>' + transactionRecord.symbol + '</td>' +
            '<td>' + transactionRecord.timeStamp + '</td>' +
            '<td>' + transactionRecord.price + '</td>' +
            '<td>' + transactionRecord.balanceBefore + '</td>' +
            '<td>' + transactionRecord.balanceAfter  + '</td>' +
            '</tr>').appendTo(transactionsRecordTable);
    });
}

function getTransactionsRecord() {
    $.ajax({
        url: "http://localhost:8080/rizpa/user/transactionsRecord",
        success: function(transactionsRecord) {
            refreshTransactionsRecordList(transactionsRecord);
        }
    });
}

function onPageLoaded() {
    getUser();
    uploadFile();
    setInterval(ajaxUsersList, refreshRate);
    setInterval(getSystemHoldings, refreshRate);
    $("#ok").click(function () {

    });
}

$(onPageLoaded);

function chargeAccount() {
    const value = $("#chargeValue").val();
    if(isNaN(value)) {
        logMessage("Charging value should be a number");
    }
    else if(value <= 0) {
        logMessage("Charging value should be >= 0");
    }
    else {
        $.ajax({
            data: value,
            url: "http://localhost:8080/rizpa/user/chargeAccount?chargeValue=" + value,
            timeout: 2000,
            method: 'post',
            error: function (errorObject) {
                logMessage("Failed charging the account");
            },
            success: function (newBalance) {
                logMessage("Account charging was successful, new balance = " + newBalance);
            }
        });
    }
}

function goToStock() {
    logMessage("Going to " + selectedStockSymbol);
    $.ajax({
        url: "http://localhost:8080/rizpa/StockRedirect?stockSymbol=" + selectedStockSymbol,
        timeout: 2000,
        method: 'get',
        error: function (errorObject) {
            logMessage("Failed redirect to stock details (" + selectedStockSymbol + " )");
        },
        success: function (nextPageUrl) {
            logMessage(nextPageUrl);
            window.location.replace(nextPageUrl);
        }
    });
}
