$(function () {
    $("#loginForm").submit(function (e) {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            method: 'get',
            error: function(errorObject) {
                logMessage(errorObject.responseText);
            },
            success: function (nextPageUrl) {
                window.location.replace(nextPageUrl);
            }
        })
        return false;
    })
})

function logMessage(message) {
    $("#messageList").append('<li>' + message + '</li>');
}