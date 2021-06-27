<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>za maintain league</title>

<!--
  Use min version of jquery
  slim version does not contain AJAX function
-->
<script
    src="http://code.jquery.com/jquery-3.4.1.min.js"
    integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
    crossorigin="anonymous">
</script>

    <link href="<c:url value="/resources/maintainLeague.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/js/league.js" />"></script>
    <script src="<c:url value="/resources/js/validator.js" />"></script>

<!--
<script src="js/league.js" type="text/javascript"></script>
<script src="js/validator.js"></script>
-->

<script src="https://cdnjs.cloudflare.com/ajax/libs/ally.js/1.4.1/ally.min.js"></script>

<script>
$(document).ready(setUpMaintainLeagueDocument);
</script>

<!--
  <link type="text/css" rel="stylesheet" href="maintainLeague.css" >
-->

</head>
<body>

    <h1>League Maintenance</h1> 
    <table class="scroll">
      <thead>
        <tr><th>League Name</th></tr>
      </thead>
      <tbody id="league-list">
      </tbody>
    </table>
  <button id="create-button">Create</button>
<a href="<c:url value='/logout' />">Click here to logout</a>
  <div id="maintain-message"></div>

<div id="dialog-create-league" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <form class="dialog-form dialog-content">
    <header>
      <h1 class="dialog-title">Create League</h1>
      <p class="dialog-description">Please enter the new league name.</p>
    </header>
    <section>
      <label for="dialog-input">New League Name: </label>
      <input class="dialog-input" name="new-league-name" size="40" maxlength="35">
    </section>
    <footer>
      <button type="submit" class="submit-button">Create</button>
      <button type="button" class="dialog-cancel-button">Cancel</button>
      <div class="dialog-message-1"></div>
      <div class="dialog-message-2"></div>
      <div class="dialog-message-3"></div>
    </footer>
  </form>
</div>

<div id="dialog-update-league" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <form class="dialog-form dialog-content">
    <header>
      <h1 class="dialog-title">Update League</h1>
      <p class="dialog-description">Please enter the new league name.</p>
    </header>
    <section>
      <p>Original League Name: <span class="dialog-old-name"/></p>
      <input class="dialog-object-id" name="league-id" hidden>
      <label for="dialog-input">New League Name: </label>
      <input class="dialog-input" name="new-league-name" size="40" maxlength="35">
    </section>
    <footer>
      <button type="submit" class="submit-button">Update</button>
      <button type="button" class="dialog-cancel-button">Cancel</button>
      <div class="dialog-message-1"></div>
      <div class="dialog-message-2"></div>
      <div class="dialog-message-3"></div>
    </footer>
  </form>
</div>

<div id="dialog-delete-league" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <header>
      <h1 class="dialog-title">Delete League</h1>
      <p class="dialog-description">Please confirm deletion.</p>
    </header>
    <section>
      <p>Are you sure you want to delete: <span class="dialog-object-name"/></p>
    </section>
    <footer>
      <button class="dialog-delete-button">Delete</button>
      <button class="dialog-cancel-button">Cancel</button>
      <div class="dialog-message-1"></div>
      <div class="dialog-message-2"></div>
      <div class="dialog-message-3"></div>
    </footer>
  </div>
</div>
</body>
</html>
