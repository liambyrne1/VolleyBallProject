<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head><title>Admin Login Page</title></head>
<body onload="document.getElementById('clubmaintenance').focus();">
<h1>Admin Login Page</h1>
<h2>Users with ROLE_ADMIN enter system on this page</h2>
<button type='button' id='leaguemaintenance'
    onclick='document.location="leaguemaintenance"'>League Maintenance</button>
<button type='button' id='clubmaintenance'
    onclick='document.location="clubmaintenance"'>Club Maintenance</button>
<a href="<c:url value='/logout' />">Click here to logout</a>
</body>
</html>
