<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>  
    <title>Volleyball Registration</title>
    <link href="<c:url value="/resources/css/security.css" />" rel="stylesheet">
</head>
<body onload='document.getElementById("username").focus();'>
 
<div id='dialog-login'>
    <form:form cssClass='dialog-content' action="registration"
        modelAttribute="userDTO" method="POST">  
        <header>
          <h1 class="dialog-title">Volleyball Registration</h1>
          <p class="dialog-description">Please enter registration details.</p>
        </header>
        <section>
            <table>
                <tr>
                    <td>UserName:</td>
                    <td><form:input path="username" id="username"
                        size='40' maxlength='35'/></td>
                    <td><form:errors path="username" cssClass="error"/></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><form:input path="password"
                        size='40' maxlength='35'/></td>
                    <td><form:errors path="password" cssClass="error"/></td>
                </tr>
            </table>
        </section>
        <footer>
            <button type="submit" name='submit' class="submit-button">Register</button>
            <button type='button' onclick='document.location="login"'>Login</button>
            <c:if test="${not empty errorMessage}">
                <div class="errorLine">${errorMessage}</div>
            </c:if>
        </footer>
    </form:form>
</div>
</body>
</html>
