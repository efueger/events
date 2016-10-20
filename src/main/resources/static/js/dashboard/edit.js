$(document).ready(function () {
    $("#q").autocomplete({
        serviceUrl: '/api/v1/products/unused/search',
        lookupLimit: 3,
        minChars: 3,
        onSelect: function (suggestion) {
            $("#addProductKey").val(suggestion.data);
            $("#addProduct").submit();
        }
    });

    $(".remove-product").click(function () {
        $("#deleteProductKey").val($(this).data('id'));
        $("#deleteProduct").submit();
    });

    var timer;
    $("#editEvent .form-control").not(".no-trigger").keyup(function () {
        startTimer($("#editEvent"));
    }).keydown(function () {
        clearTimeout(timer);
    });


    $("#editEventOptions .form-control").change(function () {
        startTimer($("#editEventOptions"));
    });

    function startTimer(form) {
        clearTimeout(timer);
        timer = setTimeout(function () {
            if (form[0].checkValidity()) {
                form.submit();
            }
        }, 1000);
    }
});
