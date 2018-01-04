<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
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
    </style>
    <title>Login</title>
</head>
    <body>
    <div class="parent">
        <div class="block">
            <form action="login" method="post">
                <table>
                    <tr>
                        <td>Email</td>
                        <td><input type="text" name="email" value="test@test.com"/></td>
                    </tr>
                    <tr>
                        <td>Пароль</td>
                        <td><input type="password" name="password" value="7561"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="Signin"/></td>
                    </tr>
                </table>
            </form>
            <br>
            <a href="registration">Зарегистрироваться</a>
        </div>
    </div>
    </body>
</html>
