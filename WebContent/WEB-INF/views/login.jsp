<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Volleyball Login</title>
    <link href='<c:url value="/resources/css/common.css" />' rel='stylesheet'>
    <link href="<c:url value="/resources/css/security.css" />" rel="stylesheet">
</head>
<body onload='document.login.username.focus();'>
 
<div id="dialog-login" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1">
    <form class='dialog-content' name='login' action='login' method='POST'>
        <header>
          <h1 class="dialog-title">Volleyball Login</h1>
          <p class="dialog-description">Please enter login details.</p>
        </header>
        <section>
            <table>
                <tr>
                    <td><label for='dialog-input'>Email: </label></td>
                    <td><input type='text' class='dialog-input' name='username' value=''
                        size='40' maxlength='35'></td>
                </tr>
                <tr>
                    <td><label for='dialog-input'>Password: </label></td>
                    <td><input type='password' class='dialog-input' name='password'
                        size='40' maxlength='35'></td>
                </tr>
            </table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </section>
        <footer>
            <button type="submit" name='submit' class="submit-button">Login</button>
            <button type='button'
                onclick='document.location="registration"'>Register</button>
            <c:if test="${not empty errorMessage}">
                <div class="errorLine">${errorMessage}</div>
            </c:if>
        </footer>
    </form>
</div>
</body>
</html>
