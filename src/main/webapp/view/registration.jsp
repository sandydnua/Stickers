<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="<c:url value="/resources/js/jquery.js" />"></script>
    <script src="<c:url value="/resources/js/registration.js" />"></script>
    <style> /* такойже стиль для parent и block и на странице логирования*/
        .parent {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            display: flex;
            align-items: center;
            align-content: center;
            justify-content: center;
            overflow: auto;
        }
        .block {
            background: #FFFFFF;
        }
        td {
            color: #979999;
            font-family: sans-serif;
        }
        img {
            height: 20px;
            width: 20px;
        }

    </style>
    <title>Registration</title>
</head>
<body>
<div class="parent">
    <div class="block">
        <form action="registration" method="post">
            <table>
                <tr>
                    <td>Email</td>
                    <td>
                        <input type="text" name="email" oninput="changeRegistrationData()"/>
                    </td>
                    <td>
                        <div id="validemail"><img src="<c:url value="/resources/img/ok.jpg" />" ></div>
                        <div id="invalidemail"><img src="<c:url value="/resources/img/no.jpg" />" ></div>
                    </td>
                </tr>
                <tr>
                    <td>Пароль</td>
                    <td>
                        <input type="password" name="password" oninput="changeRegistrationData()"/>
                    </td>
                    <td>
                        <div id="validpassword"><img src="<c:url value="/resources/img/ok.jpg" />" ></div>
                        <div id="invalidpassword"><img src="<c:url value="/resources/img/no.jpg" />" ></div>
                    </td>
                </tr>
                <tr>
                    <td>Повторить пароль</td>
                    <td>
                        <input type="password" name="dublicatepassword" oninput="changeRegistrationData()"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>
                        <input type="text" name="firstname" oninput="changeRegistrationData()"/>
                    </td>
                    <td>
                        <div id="validfirstname"><img src="<c:url value="/resources/img/ok.jpg" />" ></div>
                        <div id="invalidfirstname"><img src="<c:url value="/resources/img/no.jpg" />"  ></div>
                    </td>
                </tr>
                <tr>
                    <td>Фамилия</td>
                    <td>
                        <input type="text" name="lastname" oninput="changeRegistrationData()"/>
                    </td>
                    <td>
                        <div id="validlastname"><img src="<c:url value="/resources/img/ok.jpg" />" ></div>
                        <div id="invalidlastname"><img src="<c:url value="/resources/img/no.jpg" />" ></div>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" id="buttonsubmitform" value="Принять"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>

</form>
</body>
</html>
