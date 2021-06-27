$(function () {
    $("#loginForm").submit(function (e) {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            method: 'get',
            error: function(errorObject) {
                console.log("error");
                console.log(errorObject.responseText);
            },
            success: function (nextPageUrl) {
                console.log("success");
                console.log(nextPageUrl);
                window.location.replace(nextPageUrl);
            }
        })
        return false;
    })
})