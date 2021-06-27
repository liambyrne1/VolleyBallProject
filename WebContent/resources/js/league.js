"use strict";

/**
 * Common message displayed when server is unable to respond.
 */
var serverUnavailabeMessage = 'Volleyball System is unavailable';

/**
 * variables used in ally.js code.
 */
var disabledHandle;
var tabHandle;
var keyHandle;
var hiddenHandle;

/**
 * Holds the dialog element currently being used.
 */
var dialogElement;

/**
 * Correction for scroll position in the table.
 * The JQuery .position() method returns the position of a element relative to
 * the offset parent, in this case the tbody element relative to the table element.
 * Therefore the scroll adjustment =
 * height of the thead element + padding (30px) + height of the tbody top border (2px).
 */
const SCROLL_ADJUSTMENT = 32;

/**
 * Called when Maintain League page is loaded.
 *
 * Loads the leagues from the server into the table.
 *
 * Assigns the openCreateLeagueDialog function to the create button.
 *
 * Uses the class selector to assign the validateLeagueInput function to the
 * input field on each dialog.
 *
 * Sets up the events on the three dialogs create, update and delete.
 *
 * Before assigning the command buttons on a dialog, must assign the global variable
 * dialogElement to the specific dialog.
 */
function setUpMaintainLeagueDocument() {
    getLeaguesFromServer();

    $('#create-button').click(openCreateLeagueDialog);

    $('.dialog-input').on('input', function() {
        validateLeagueInput($(this).val());
    });

    dialogElement = document.getElementById('dialog-create-league');
    setUpDialogButtons(createLeagueOnServer);

    dialogElement = document.getElementById('dialog-update-league');
    setUpDialogButtons(updateLeagueOnServer);

    setUpDeleteDialogButtons();
}

/**
 * Sets up the dialog buttons on the create/update dialogs. Both dialogs
 * have embedded forms, and are activated by the form submit button,
 *
 * Pass the form element to the submit function as an event data parameter.
 *
 * Assign a function to the submit button, the parameter pass into this
 * function.
 *
 * Assign the 'cancelDialog' function to the cancel button.
 */
function setUpDialogButtons(serverFunction) {
    var dialogFormElement =
        dialogElement.getElementsByClassName('dialog-form')[0];
    var submitparams = {'form' : dialogFormElement};
    $(dialogFormElement).submit(submitparams, serverFunction);

    var dialogCancelButtonElement =
        dialogElement.getElementsByClassName('dialog-cancel-button')[0];
    $(dialogCancelButtonElement).click(cancelDialog);
}

/**
 * Assign functions to the Delete and Cancel buttons on the Delete dialog.
 */
function setUpDeleteDialogButtons() {
    dialogElement = document.getElementById('dialog-delete-league');
    
    var dialogDeleteButtonElement =
        dialogElement.getElementsByClassName('dialog-delete-button')[0];
    $(dialogDeleteButtonElement).click(deleteLeagueOnServer);

    var dialogCancelButtonElement =
        dialogElement.getElementsByClassName('dialog-cancel-button')[0];
    $(dialogCancelButtonElement).click(cancelDialog);
}

/* Read Code Section. */

function getLeaguesFromServer(newScrollPosition) {
    $.ajax({
        cache: false,
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    displayLeaguesInTable(result, status, xhr, newScrollPosition);
                },
        type: "GET",
        url: 'rest/LeagueService/leagues'
    });
}

/**
 * Invoked after a successful read request.
 */
function displayLeaguesInTable(leagueArr, textStatus, jQxhr, newScrollPosition) {
    var errorMessage = 'No leagues exist.';

    if (leagueArr.length == 0) {
        displayMaintainMessage(errorMessage);
    } else {
        displayLeagues(leagueArr, newScrollPosition);
    }
}

/**
 * Display the leagues in the table. Position the scrollbar,
 * so that the league name in 'newScrollPosition', is displayed
 * at the top of the table.
 * 'newScrollPosition' is undefined when leagues are loaded for the first
 * time, screen initialisation. Use the first league returned from server.
 */
function displayLeagues(leagueArr, newScrollPosition) {
    var tbodyElement = document.getElementById("league-list");
    var index;
    var rowElement;
    var newScrollElementPosition;

    if (newScrollPosition == undefined) {
        newScrollPosition = leagueArr[0].name;
    }

    for (index = 0; index < leagueArr.length; index++) {
        rowElement = createRowElement(leagueArr[index]);
        if (newScrollPosition.localeCompare(leagueArr[index].name) == 0) {
            newScrollElementPosition = rowElement;
        }
        tbodyElement.appendChild(rowElement);
    }

    var scrollPosition = $(newScrollElementPosition).position();
    $( "tbody" ).scrollTop(scrollPosition.top - SCROLL_ADJUSTMENT);
}

/**
 * store the league id in the 'data-league-id' attribute of the row.
 * create data cells for the league name, update button and delete button.
 */
function createRowElement(league) {
    var rowElement = document.createElement("tr");
    rowElement.setAttribute("data-league-id", league.id.toString());

    var cellElement = createDescriptionCellElement(league.name);
    rowElement.appendChild(cellElement);
    cellElement = createUpdateButtonCellElement();
    rowElement.appendChild(cellElement);
    cellElement = createDeleteButtonCellElement();
    rowElement.appendChild(cellElement);

    return rowElement;
}

/**
 * create a data cell for the league name.
 */
function createDescriptionCellElement(leagueName) {
    var cellElement = document.createElement("td");
    var textNode = document.createTextNode(leagueName);
    cellElement.appendChild(textNode);
    return cellElement;
}

/**
 * create a data cell for the update button.
 */
function createUpdateButtonCellElement() {
    var cellElement = document.createElement("td");

    var buttonElement = document.createElement("button");
    var textNode = document.createTextNode("Update");
    buttonElement.appendChild(textNode);
    buttonElement.setAttribute("onclick", "updateLeague(this)");

    cellElement.appendChild(buttonElement);
    return cellElement;
}

/**
 * create a data cell for the delete button.
 */
function createDeleteButtonCellElement() {
    var cellElement = document.createElement("td");

    var buttonElement = document.createElement("button");
    var textNode = document.createTextNode("Delete");
    buttonElement.appendChild(textNode);
    buttonElement.setAttribute("onclick", "deleteLeague(this)");

    cellElement.appendChild(buttonElement);
    return cellElement;
}

/* Create Code Section. */

/**
 * Invoked by the create button on the main screen.
 * Open and initialise the dialog to create a league.
 */
function openCreateLeagueDialog() {
    clearMaintainMessage();
    dialogElement = document.getElementById('dialog-create-league');
    // access openDialog via leagueExports to allow 'mocha' to stub it out.
    leagueExports.openDialog(dialogElement);
    dialogElement.getElementsByClassName('dialog-input')[0].value = '';
}

/**
 * Invoked by the create button on the create league dialog.
 * read input from dialog. Display error if input is empty.
 * if input is valid, submit to server.
 */
function createLeagueOnServer(event) {
    event.preventDefault();

    console.log('Runing createLeagueOnServer');
    let inputField = dialogElement.getElementsByClassName('dialog-input')[0];
    let inputName = inputField.value;
    console.log('validInput = ' + validInput);
    if (validInput) {
        console.log('About to call submitToServer');
        let url = 'rest/LeagueService/leagues';
        let type = 'POST';
        let form = event.data.form;
        let successFunction = serverSuccessCreateUpdate;
        submitToServer(url, type, form, successFunction, inputName, 'created');
    } else {
        isEmptyText(inputName);
        $(inputField).focus();
    }
}

/**
 * Success method used after a create request or an update request.
 * Reload the leagues from the server, close the dialog
 * and display a message on the main screen.
 * newScrollPosition is the new league name of the created or updated league.
 * Use this to position the new league name as the top row of the table.
 */
function serverSuccessCreateUpdate(response, status, xhr, newScrollPosition, text) {
    let message = 'League has been ' + text + '.';

    if (response.status) {
        reloadLeaguesFromServer(newScrollPosition);
        cancelDialog();
        displayMaintainMessage(message);
    } else {
        displayErrorTop(response.message);
        let inputField = dialogElement.getElementsByClassName('dialog-input')[0];
        $(inputField).focus();
    }
}

/* Update Code Section. */

/**
 * invoked by update button in table.
 * update button is in second data cell of row.
 * retrieve the league id and name from the table
 * and open the update dialog.
 */
function updateLeague(buttonElement) {
    clearMaintainMessage();
    var rowElement = buttonElement.parentNode.parentNode;
    var leagueId = rowElement.getAttribute("data-league-id");
    // get name from first data cell in row.
    var updatedLeagueName = rowElement.firstChild.firstChild.nodeValue;
    openUpdateLeagueDialog(leagueId, updatedLeagueName);
}

/**
 * Open a dialog to update a league name.
 * Display the old league name and store its database id number.
 */
function openUpdateLeagueDialog(leagueId, oldLeagueName) {
    dialogElement = document.getElementById('dialog-update-league');
    // access openDialog via leagueExports to allow 'mocha' to stub it out.
    leagueExports.openDialog(dialogElement);

    dialogElement.getElementsByClassName('dialog-input')[0].value = '';
    dialogElement.getElementsByClassName('dialog-old-name')[0].innerHTML = oldLeagueName;
    dialogElement.getElementsByClassName('dialog-object-id')[0].value = leagueId;

}

/**
 * Invoked by the update button on the update dialog.
 * Check if new name is different to old name.
 * Also display error if input is empty.
 */
function updateLeagueOnServer(event) {
    event.preventDefault();

    let inputField = dialogElement.getElementsByClassName('dialog-input')[0];
    let inputName = inputField.value;
    if (validInput) {
        if (validateDifferentName(inputName)) {
            let url = 'rest/LeagueService/leagues';
            let type = 'PUT';
            let form = event.data.form;
            let successFunction = serverSuccessCreateUpdate;
            submitToServer(url, type, form, successFunction, inputName, 'updated');
        }
    } else {
        isEmptyText(inputName);
        $(inputField).focus();
    }
}

/**
 * If name is empty, displays error message and returns false
 */
function isEmptyText(name) {
    let emptyText = false;
    let errorMessage = "Enter League Name.";

    if (name == "") {
        displayErrorTop(errorMessage);
        emptyText = true;
    }
    return emptyText;
}

/**
 * Allow user to change the case of the name, therefore use a
 * case-sensitive string comparison.
 */
function validateDifferentName(newName) {
    let validReturn = true;
    let errorMessage = 'New name is the same as old name.';
    let oldNameElement = dialogElement.getElementsByClassName('dialog-old-name')[0];
    let oldName = $(oldNameElement).text();

    if (newName.localeCompare(oldName) == 0) {
        displayErrorTop(errorMessage);
        let inputField = dialogElement.getElementsByClassName('dialog-input')[0];
        $(inputField).focus();
        validReturn = false;
    }
    return validReturn;
}

/* Delete Code Section. */

/**
 * invoked by delete button in table.
 * delete button is in third data cell of row.
 * retrieve the league id and name from the table
 * and open the delete dialog.
 */
function deleteLeague(buttonElement) {
    clearMaintainMessage();
    var rowElement = buttonElement.parentNode.parentNode;
    var leagueId = rowElement.getAttribute("data-league-id");
    // get name from first data cell in row.
    var deletedLeagueName = rowElement.firstChild.firstChild.nodeValue;
    var newScrollPosition = getDeleteScrollPosition(rowElement);
    openDeleteLeagueDialog(leagueId, deletedLeagueName, newScrollPosition);
}

/**
 * Determind the position of the scrollbar in the table after a deletion.
 * Positioned the scrollbar at the league name before the deleted league.
 * If not possible, i.e. the deleted league was displayed first in the table,
 * use the league name after the deleted league. Note this might not be present,
 * as the deleted league could be the last league on the database.
 * returns the name of the league, that will be displayed at the top of the table.
 * will return 'undefined' if not set.
 */
function getDeleteScrollPosition(deleteElementRow) {
    var newScrollPosition;
    var previousRowElement = deleteElementRow.previousElementSibling;
    var nextRowElement;

    if (previousRowElement != null) {
        newScrollPosition = previousRowElement.firstChild.firstChild.nodeValue;
    } else {
        nextRowElement = deleteElementRow.nextElementSibling;
        if (nextRowElement != null) {
            newScrollPosition = nextRowElement.firstChild.firstChild.nodeValue;
        }
    }
    return newScrollPosition;
}
/**
 * Open a dialog to delete a league name.
 * Display a confirmation message with the league name
 * Store its database id number and the new scroll position in a data attribute.
 */
function openDeleteLeagueDialog(leagueId, deleteLeagueName, newScrollPosition) {
    dialogElement = document.getElementById('dialog-delete-league');
    // access openDialog via leagueExports to allow 'mocha' to stub it out.
    leagueExports.openDialog(dialogElement);

    var spanElement = dialogElement.getElementsByClassName('dialog-object-name')[0];
    spanElement.setAttribute('data-league-id', leagueId);
    spanElement.setAttribute('data-scroll-position', newScrollPosition);
    spanElement.innerHTML = deleteLeagueName;

}

/**
 * Invoked by the delete button on the delete dialog.
 * Get the league id and the new scroll position from the span element
 * and send a delete request to the server.
 */
function deleteLeagueOnServer() {
    var spanElement = dialogElement.getElementsByClassName('dialog-object-name')[0];
    var deleteLeagueName = spanElement.innerHTML;
    var leagueId = spanElement.getAttribute('data-league-id');
    var newScrollPosition = spanElement.getAttribute('data-scroll-position');

    $.ajax({
        async: false,
        cache: false,
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    serverSuccessDelete(result, status, xhr,
                        deleteLeagueName, newScrollPosition);
                },
        type: "DELETE",
        url: 'rest/LeagueService/leagues/' + leagueId
    });
}

/**
 * Reload the leagues from the server, close the dialog
 * and display a message on the main screen.
 */
function serverSuccessDelete(result, status, xhr, deleteLeagueName, newScrollPosition) {
    var message = deleteLeagueName + ' has been deleted.'

    reloadLeaguesFromServer(newScrollPosition);
    cancelDialog();
    displayMaintainMessage(message);
}

/* Error Display Code Section. */

/**
 * Initialises boolean fields and error messages
 */
function initialiseDialog() {
    validInput = false;
    dialogElement.getElementsByClassName('dialog-message-1')[0].innerHTML = '';
    dialogElement.getElementsByClassName('dialog-message-2')[0].innerHTML = '';
    dialogElement.getElementsByClassName('dialog-message-3')[0].innerHTML = '';
}

/**
 * displays an error message in the next available error position on the dialog.
 */
function displayError(errorMessage) {
    var dialogMessageElement
        = dialogElement.getElementsByClassName('dialog-message-1')[0];
    if (dialogMessageElement.innerHTML == "") {
        dialogMessageElement.innerHTML = errorMessage;
        dialogMessageElement.style.color = "red";
    } else {
        dialogMessageElement
            = dialogElement.getElementsByClassName('dialog-message-2')[0];
        if (dialogMessageElement.innerHTML == "") {
            dialogMessageElement.innerHTML = errorMessage;
            dialogMessageElement.style.color = "red";
        } else {
            dialogMessageElement
                = dialogElement.getElementsByClassName('dialog-message-3')[0];
            dialogMessageElement.innerHTML = errorMessage;
            dialogMessageElement.style.color = "red";
        }
    }
}

/**
 * displays an error message in the top error position on the dialog.
 * this prevents error messages being repeated.
 */
function displayErrorTop(errorMessage) {
    let dialogMessageElement
        = dialogElement.getElementsByClassName('dialog-message-1')[0];
    dialogMessageElement.innerHTML = errorMessage;
    dialogMessageElement.style.color = "red";
}

/**
 * Clears the message on the maintainance screen.
 */
function clearMaintainMessage(message) {
    $("#maintain-message").text('');
}


/**
 * Displays a message on the maintainance screen.
 */
function displayMaintainMessage(message) {
    $("#maintain-message").text(message).css("color", "green").show();
}

/* Common Code Section. */

function reloadLeaguesFromServer(newScrollPosition) {
    $('#league-list').empty();
    getLeaguesFromServer(newScrollPosition);
}

function openDialog(dialog) {
    // Make sure that no element outside of the dialog
    // can be interacted with while the dialog is visible.
    // This means we don't have to handle Tab and Shift+Tab,
    // but can defer that to the browser's internal handling.
    disabledHandle = ally.maintain.disabled({
        filter: dialog
    });

    // Make sure that Tab key controlled focus is trapped within
    // the tabsequence of the dialog and does not reach the
    // browser's UI, e.g. the location bar.
    tabHandle = ally.maintain.tabFocus({
        context: dialog,
    });

    // React to enter and escape keys as mandated by ARIA Practices
    keyHandle = ally.when.key({
        escape: cancelDialog,
    });

    // create or show the dialog
    dialog.hidden = false;
    initialiseDialog();

    // the dialog is visible on screen, so find the first
    // keyboard focusable element (giving any element with
    // autofocus attribute precendence). If the dialog does
    // not contain any keyboard focusabe elements, focus will
    // be given to the dialog itself.
    var element = ally.query.firstTabbable({
        context: dialog,
        defaultToContext: true,
    });
    element.focus();

    // Make sure that no element outside of the dialog
    // is exposed via the Accessibility Tree, to prevent
    // screen readers from navigating to content it shouldn't
    // be seeing while the dialog is open. See example:
    // https://marcysutton.com/slides/mobile-a11y-seattlejs/#/36
    hiddenHandle = ally.maintain.hidden({
        filter: dialog,
    });

}

function cancelDialog() {
    // undo disabling elements outside of the dialog
    disabledHandle.disengage();

    // undo trapping Tab key focus
    tabHandle.disengage();

    // undo hiding elements outside of the dialog
    hiddenHandle.disengage();

    dialogElement.hidden = true;
}

/**
 * Submits the form to the server, for a create or update request,
 * for a given url, HTTP method type and a form id.
 */
function submitToServer(url, type, form, successFunction, newScrollPosition, text) {

    $.ajax({
        cache: false,
        contentType: 'application/x-www-form-urlencoded',
        data: $(form).serialize(),
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    successFunction(result, status, xhr, newScrollPosition, text);
                },
        type: type,
        url: url
    });
}

/**
 * Bound to a server error response
 * Display the error message on console.
 */
function serverFailure(jqXhr, textStatus, errorThrown){
    console.log( 'textStatus = ' + textStatus );
    console.log( 'jqXhr = ' + jqXhr );
    console.log( 'jqXhr.status = ' + jqXhr.status );
    console.log( 'jqXhr.statusText = ' + jqXhr.statusText );
    console.log('jqXhr.responseText = ' + jqXhr.responseText );
    }

/**
 * Object to expose variables/functions to test service.
 */
var leagueExports = {};

leagueExports.setUpDialogButtons = setUpDialogButtons;
leagueExports.setUpDeleteDialogButtons = setUpDeleteDialogButtons;
leagueExports.displayError = displayError;
leagueExports.openCreateLeagueDialog = openCreateLeagueDialog;
leagueExports.createLeagueOnServer = createLeagueOnServer;
leagueExports.openDialog = openDialog;
leagueExports.initialiseDialog = initialiseDialog;
leagueExports.getLeaguesFromServer = getLeaguesFromServer;
leagueExports.displayLeaguesInTable = displayLeaguesInTable;
leagueExports.displayLeagues = displayLeagues;
leagueExports.updateLeague = updateLeague;
leagueExports.updateLeagueOnServer = updateLeagueOnServer;
leagueExports.deleteLeague = deleteLeague;
module.exports = leagueExports;
