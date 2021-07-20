var refreshRate = 1000; //milli seconds

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
            $("#hiLabel").text("Hi " + user.name)
            if(user.userRole === "Trader") {
                fileChooser.show()
                setInterval(getUserBalance, refreshRate);
            }
        }
    })
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
                    console.error("Failed to submit");
                    $("#result").text("Failed to get result from server " + e);
                },
                success: function(r) {
                    $("#result").text(r);
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
var selectedIndex = 0;
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
        const rowStart = (index === selectedIndex) ? '<tr class="selected">' : '<tr>'
        const row = $(rowStart +
            '<td>' + stock.companyName + '</td>' +
            '<td>' + stock.symbol + '</td>' +
            '<td>' + stock.price + '</td>' +
            '<td>' + stock.sumOfAllTransactions + '</td>' +
            '</tr>');
        row.click(function(){
            selectedIndex = index;
            console.log("clicked " + selectedIndex);
            // $(this).addClass('selected').siblings().removeClass('selected');
        });
        row.appendTo(stocksTable);
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


function onPageLoaded() {
    getUser();
    uploadFile();
    setInterval(ajaxUsersList, refreshRate);
    setInterval(getSystemHoldings, refreshRate);

    // $('.ok').on('click', function(e){
    //     console.log("OK Click");
    //     alert($("#table tr.selected td:first").html());
    // });
}


$(onPageLoaded);
