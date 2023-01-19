<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Volleyball System Admin</title>
  <link href='<c:url value="/resources/css/common.css" />' rel='stylesheet'>
  <link href='<c:url value="/resources/css/adminLoginPage.css" />' rel='stylesheet'>
</head>

<body onload="document.getElementById('leaguemaintenance').focus();">

<div id="dialog-system-admin" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1">
  <div class="dialog-content">
    <header>
      <h1 class="dialog-title">Volleyball System Admin</h1>
      <p class="dialog-description">Users with ROLE_ADMIN enter system on this page.</p>
    </header>
    <section>
      <table class="background-table">
        <tr>
          <td class='background-table-cell'>
            <button type='button' id='leaguemaintenance'
                onclick='document.location="leaguemaintenance"'>
                    League Maintenance</button>
          </td>
          <td class='background-table-cell'>
            <button type='button' id='clubmaintenance'
                onclick='document.location="clubmaintenance"'>Club Maintenance</button>
          </td>
        </tr>
      </table>
    </section>
    <footer>
      <button type='button' id='logout'
          onclick='document.location="logout"'>Logout</button>
    </footer>
  </div>
</div>

</body>
</html>
