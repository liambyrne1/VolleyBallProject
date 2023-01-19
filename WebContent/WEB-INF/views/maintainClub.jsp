<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>za maintain club</title>

<!--
  Use min version of jquery
  slim version does not contain AJAX function
-->
<script
    src="http://code.jquery.com/jquery-3.4.1.min.js"
    integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
    crossorigin="anonymous">
</script>

    <link href="<c:url value="/resources/css/maintainClub.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/js/club.js" />"></script>
    <script src="<c:url value="/resources/js/validator.js" />"></script>
    <script src="<c:url value="/resources/js/common.js" />"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/ally.js/1.4.1/ally.min.js"></script>

<script>
$(document).ready(setUpMaintainClubDocument);
</script>

</head>
<body>

<div id="dialog-club-maintenance">
  <h1>Club Maintenance</h1> 
  <table class="scroll">
    <thead>
      <tr><th>Club Name</th></tr>
    </thead>
    <tbody id="club-list">
    </tbody>
  </table>
  <button id="create-button">Create</button>
  <button id='close-button'>Close</button>
  <div class="maintain-message"></div>
</div>

<div id="dialog-create-item" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <form class="dialog-form dialog-content">
    <header>
      <h1 class="dialog-title">Create Club</h1>
      <p class="dialog-description">Please enter the new club name.</p>
    </header>
    <section>
      <label for="dialog-input">New Club Name: </label>
      <input class="dialog-input" name="new-club-name" size="40" maxlength="35">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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

<div id="dialog-update-item-name" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <form class="dialog-form dialog-content">
    <header>
      <h1 class="dialog-title">Update Club</h1>
      <p class="dialog-description">Please enter the new club name.</p>
    </header>
    <section>
      <p>Original Club Name: <span class="dialog-old-name"/></p>
      <input class="dialog-object-id" name="club-id" hidden>
      <label for="dialog-input">New Club Name: </label>
      <input class="dialog-input" name="new-club-name" size="40" maxlength="35">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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

<div id="dialog-delete-item" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <header>
      <h1 class="dialog-title">Delete Club</h1>
      <p class="dialog-description">Please confirm deletion.</p>
    </header>
    <section>
      <p>Are you sure you want to delete: <span class="dialog-object-name"/></p>
      <p>Warning! This will remove all associated teams.</p>
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

<div id="dialog-club-team-maintenance" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <h1>Club Team Maintenance</h1> 
    <table class="scroll">
      <thead>
        <tr><th id="dialog-team-header"></th></tr>
      </thead>
      <tbody id="team-list" class='team-list-class'>
      </tbody>
    </table>
    <button id="create-team-button">Create</button>
    <button class="dialog-cancel-button">Close</button>
    <div class="maintain-message"></div>
  </div>
</div>

<div id="dialog-create-club-team" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <form class="dialog-form dialog-content">
    <header>
      <h1 class="dialog-title">Create Team</h1>
      <p class="dialog-description">Please enter the new team name.</p>
    </header>
    <section>
      <label for="dialog-input">New Team Name: </label>
      <input class="dialog-input" name="new-team-name" size="40" maxlength="35">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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

<div id="dialog-update-club-team-name" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <form class="dialog-form dialog-content">
    <header>
      <h1 class="dialog-title">Update Team</h1>
      <p class="dialog-description">Please enter the new team name.</p>
    </header>
    <section>
      <p>Original Team Name: <span class="dialog-old-name"/></p>
      <input class="dialog-object-id" name="team-id" hidden>
      <label for="dialog-input">New Team Name: </label>
      <input class="dialog-input" name="new-team-name" size="40" maxlength="35">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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

<div id="dialog-delete-club-team" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <header>
      <h1 class="dialog-title">Delete Team</h1>
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
