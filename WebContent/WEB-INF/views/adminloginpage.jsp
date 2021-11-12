<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head><title>Admin Login Page</title></head>
<body>
<h1>Admin Login Page</h1>
<h2>Users with ROLE_ADMIN enter system on this page</h2>
<button type='button'
    onclick='document.location="leaguemaintenance"'>League Maintenance</button>
<a href="<c:url value='/logout' />">Click here to logout</a>
</body>
</html>
