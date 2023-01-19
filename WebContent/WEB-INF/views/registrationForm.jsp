<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>  
    <title>Volleyball Registration</title>

    <!--
      Use min version of jquery
      slim version does not contain AJAX function
    -->
    <script
        src="http://code.jquery.com/jquery-3.4.1.min.js"
        integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
        crossorigin="anonymous">
    </script>

    <link href='<c:url value="/resources/css/common.css" />' rel='stylesheet'>
    <link href="<c:url value="/resources/css/security.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/js/registration.js" />"></script>

    <script>
        $(document).ready(setUpRegistrationDocument);
    </script>

</head>
<body>
 
<div id="dialog-registration" class="a" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1">
    <form:form cssClass='dialog-content' action="registration"
        modelAttribute="userDTO" method="POST">  
        <header>
          <h1 class="dialog-title">Volleyball Registration</h1>
          <p class="dialog-description">
            <span>Please enter registration details.</span>
            <span> All fields are mandatory.</span>
          </p>
        </header>
        <section>
            <table>
                <tr>
                  <td><label for="first-name">First Name:</label></td>
                  <td><form:input path="firstName" id="first-name"
                    size='40' maxlength='35'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td><form:errors path="firstName" cssClass="error"
                    element='div'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td id='first-name-error' class='error'>
                      <span class="error-message-1"></span>
                      <span class="error-message-2"></span>
                  </td>
                </tr>
                <tr>
                  <td><label for="last-name">Last Name:</label></td>
                  <td><form:input path="lastName" id="last-name"
                    size='40' maxlength='35'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td><form:errors path="lastName" cssClass="error"
                    element='div'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td id='last-name-error' class='error'>
                      <span class="error-message-1"></span>
                      <span class="error-message-2"></span>
                  </td>
                </tr>
                <tr>
                  <td><label for="email">Email:</label></td>
                  <td><form:input path="email" id="email"
                    size='40' maxlength='35'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td><form:errors path="email" cssClass="error"
                    element='div'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td id='email-error' class='error'>
                      <span class="error-message-1"></span>
                      <span class="error-message-2"></span>
                  </td>
                </tr>
                <tr>
                  <td><label for="password">Password:</label></td>
                  <td><form:input path="password" id="password"
                    size='40' maxlength='35'/></td>
                  <td>8 to 25 characters.</td>
                </tr>
                <tr>
                  <td></td>
                  <td><form:errors path="password" cssClass="error"
                    element='div'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td id='password-error' class='error'>
                      <span class="error-message-1"></span>
                      <span class="error-message-2"></span>
                  </td>
                </tr>
                <tr>
                  <td><label for="matching-password">Confirm:</label></td>
                  <td><form:input path="matchingPassword" id="matching-password"
                    size='40' maxlength='35'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td><form:errors path="matchingPassword" cssClass="error"
                    element='div'/></td>
                </tr>
                <tr>
                  <td></td>
                  <td id='matching-password-error' class='error'>
                      <span class="error-message-1"></span>
                      <span class="error-message-2"></span>
                  </td>
                </tr>
                <tr>
                  <td></td>
                  <td><form:errors cssClass="error" element='div'/></td>
                </tr>
                <c:if test="${not empty errorMessage}">
                    <tr>
                      <td></td>
                        <td><div class="error">${errorMessage}</div></td>
                    </tr>
                </c:if>
            </table>
        </section>
        <footer>
            <button type="submit" name='submit' class="submit-button">Register</button>
            <button type='button' onclick='document.location="login"'>Login</button>
            <p id='main-error-message' class='error'></p>
        </footer>
    </form:form>
</div>
</body>
</html>
