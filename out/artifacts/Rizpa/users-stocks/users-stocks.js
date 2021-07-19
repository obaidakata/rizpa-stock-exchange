var refreshRate = 2000; //milli seconds

function getUser() {
    var fileChooser = $("#fileChooser")
    fileChooser.hide()
    console.log("checkIfUserIsTrader");
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
    const usersList = $("#usersList");
    usersList.empty();
    $('<tr>' +
        '<th> User Role </th>' +
        '<th> Username </th>' +
        '</tr>')
        .appendTo(usersList);

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        console.log("Adding user #" + index + ": " + user);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<tr>' +
            '<th>' + user.userRole + '</th>' +
            '<th>' + user.name + '</th>' +
        '</tr>')
            .appendTo(usersList);
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


function onPageLoaded() {
    getUser();
    uploadFile();
    setInterval(ajaxUsersList, refreshRate);
}



$(onPageLoaded)