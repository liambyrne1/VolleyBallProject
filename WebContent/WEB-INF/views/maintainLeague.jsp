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

    <link href="<c:url value="/resources/css/maintainLeague.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/js/league.js" />"></script>
    <script src="<c:url value="/resources/js/validator.js" />"></script>
    <script src="<c:url value="/resources/js/common.js" />"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/ally.js/1.4.1/ally.min.js"></script>

<script>
$(document).ready(setUpMaintainLeagueDocument);
</script>

</head>
<body>

<div id="dialog-league-maintenance">
  <h1>League Maintenance</h1> 
  <table class="scroll">
    <thead>
      <tr><th>League Name</th></tr>
    </thead>
    <tbody id="league-list">
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
      <h1 class="dialog-title">Create League</h1>
      <p class="dialog-description">Please enter the new league name.</p>
    </header>
    <section>
      <label for="dialog-input">New League Name: </label>
      <input class="dialog-input" name="new-league-name" size="40" maxlength="35">
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
      <h1 class="dialog-title">Update League</h1>
      <p class="dialog-description">Please enter the new league name.</p>
    </header>
    <section>
      <p>Original League Name: <span class="dialog-old-name"/></p>
      <input class="dialog-object-id" name="league-id" hidden>
      <label for="dialog-input">New League Name: </label>
      <input class="dialog-input" name="new-league-name" size="40" maxlength="35">
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

<div id="dialog-league-team-maintenance" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <h1>League Team Maintenance</h1> 
    <table class="scroll">
      <thead>
        <tr><th id="dialog-team-header"></th></tr>
      </thead>
      <tbody id="league-team-list" class='team-list-class'>
      </tbody>
    </table>
    <button class="add-teams-to-league-button">Add Teams</button>
    <button class="dialog-cancel-button">Close</button>
    <div class="maintain-message"></div>
  </div>
</div>

<div id="dialog-remove-league-team" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <header>
      <h1 class="dialog-title">Remove Team</h1>
      <p class="dialog-description">Please confirm team removal.</p>
    </header>
    <section>
      <p>Are you sure you want to remove: <span class="dialog-object-name"/></p>
    </section>
    <footer>
      <button class="dialog-delete-button">Remove</button>
      <button class="dialog-cancel-button">Cancel</button>
      <div class="dialog-message-1"></div>
      <div class="dialog-message-2"></div>
      <div class="dialog-message-3"></div>
    </footer>
  </div>
</div>

<div id="dialog-add-league-teams" role="dialog" aria-labelledby="dialog-title"
  aria-describedby="dialog-description" tabindex="-1" hidden>
  <div class="dialog-content">
    <h1 class='selection-header'>Add Teams To <span class="dialog-object-name"/></h1> 
    <table class="background-table">
      <tr>
        <td class='background-table-cell'>
          <table class="selection">
            <thead>
              <tr><th id="dialog-team-header">Leagues
                <span class='league-prompt'>Select League</span></th></tr>
            </thead>
            <tbody id="league-selection-list" class='team-list-class'>
            </tbody>
          </table>
        </td>
        <td class='background-table-cell'>
          <table class="selection">
            <thead>
              <tr><th id="dialog-team-header">Teams
                <span class='team-prompt'>Select Team(s)</span></th></tr>
            </thead>
            <tbody id="team-selection-list" class='team-list-class'>
            </tbody>
          </table>
        </td>
      </tr>
    </table>
    <div>
      <div class='buttons'>
          <button class='add-teams-to-league-button'>Add Teams</button>
      </div>
      <div class='button-text'>
          <p class='add-button-text'>
            All selected teams, whether shown or not, are added to league.</p>
      </div>
    </div>
    <div>
      <div class='buttons'>
          <button class='dialog-cancel-button'>Cancel</button>
      </div>
      <div class='button-text'>
          <p class='paragraph-text'>All selections will be lost.</p>
      </div>
    </div>
    <div class="maintain-message"></div>
  </div>
</div>

</body>
</html>
