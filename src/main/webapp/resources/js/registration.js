$(window).on('load', function(){
    $("#validemail").hide();
    $("#validfirstname").hide();
    $("#validlastname").hide();
    $("#validpassword").hide();
    $("#buttonsubmitform").prop("disabled", true);
});

function changeRegistrationData() {
    values = new Object();
    values = {
        email : {value: $("input[name='email']").val(), validFunction: correctEmail },
        firstname : {value: $("input[name='firstname']").val(), validFunction: correctFirstName},
        lastname : {value: $("input[name='lastname']").val(), validFunction: correctLastName},
    };
    enableButton = true;
    for(var key in values) {
        var correctVslue = (values[key]['value'] != "") && (values[key]['validFunction'](values[key]['value']));
        enableButton = switchValidInvalid( correctVslue, key) && enableButton;
    }
    password = $("input[name='password']").val();
    dublicatepassword = $("input[name='dublicatepassword']").val();
    enableButton = switchValidInvalid( (password != "" &&
                                        dublicatepassword !="" &&
                                        password == dublicatepassword),
                                       "password"
                                     ) && enableButton;

    $("#buttonsubmitform").prop("disabled", !enableButton);
}

function switchValidInvalid(valid, key) {
    if(valid) {
        $("#invalid" + key).hide();
        $("#valid" + key).show();

    } else {
        $("#invalid" + key).show();
        $("#valid" + key).hide();
    }
    return valid;
}

var correctEmail = function(value) {
    var absentEmail = true;
    $.ajax({
        url:'absetEmail',
        data:{email : value},
        method:'GET',
        dataType:'json',
        async:false
    }).done(function (answer) {
        absentEmail = answer;
    });
    if (absentEmail && value.match(/^[0-9a-z-\.]+\@[0-9a-z-]{2,}\.[a-z]{2,}$/i)) {
        return true;
    } else {
        return false;
    }
}
var correctFirstName = function(value) {
    //TODO функции проверки для разных полей сейчас могут совпадать, но потом в разные добавятся разные реацкии на неправильное поле
    if ((/^[^0-9%\\\/,@#$^&()+=*\-<>]{2,}$/).test(value)) {
        return true;
    } else {
       return false;
    }
}
var correctLastName = function(value) {
    if (value.match(/^[^0-9%\\\/,@#$^&()+=*\-<>]{2,}$/)) {
        return true;
    } else {
        return false;
    }
}