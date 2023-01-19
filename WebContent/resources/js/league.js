"use strict";

/**
 * The name of the league maintenance dialog. The base dialog of the web page.
 */
 const DIALOG_LEAGUE_MAINTENANCE = 'dialog-league-maintenance';

/**
 * The id of the tbody element of the league table.
 */
 const LEAGUE_LIST = 'league-list';

/**
 * The name of the current item i.e. league/club/team.
 */
 const ITEM = 'League';

/**
 * The name of the create dialog.
 */
 const DIALOG_CREATE_ITEM = 'dialog-create-item';

/**
 * The name of the update dialog.
 */
 const DIALOG_UPDATE_ITEM_NAME = 'dialog-update-item-name';

/**
 * The name of the delete dialog.
 */
 const DIALOG_DELETE_ITEM = 'dialog-delete-item';

/**
 * The URL end part that relates to the current item i.e.  Leagues
 */
 const URL_END = 'leagues';

/**
 * The name of the league team maintenance dialog.
 */
 const DIALOG_LEAGUE_TEAM_MAINTENANCE = 'dialog-league-team-maintenance';

/**
 * The id of the tbody element of the team table.
 */
 const LEAGUE_TEAM_LIST = 'league-team-list';

/**
 * The name of the remove team dialog.
 */
 const DIALOG_REMOVE_LEAGUE_TEAM = 'dialog-remove-league-team';

/**
 * The URL start part that relates to the team processing.
 */
 const URL_LEAGUE_TEAM_START = 'league';

/**
 * The add teams to league button on the league team maintenance dialog and
 * on the add teams to league dialog.
 */
const ADD_TEAMS_TO_LEAGUE_BUTTON = 'add-teams-to-league-button';

/**
 * The add teams to league dialog.
 */
const DIALOG_ADD_LEAGUE_TEAMS = 'dialog-add-league-teams';

/**
 * Identifies the tbody element in the league selection table.
 */
const LEAGUE_SELECTION_LIST = 'league-selection-list';

/**
 * Identifies the tbody element in the team selection table.
 */
const TEAM_SELECTION_LIST = 'team-selection-list';

/**
 * Function to react to a checkbox click on the league selection table.
 */
const LEAGUE_CHECKBOX_FUNCTION =
    'processLeagueSelection(this)';

/**
 * Row attribute to store the item id from the database.
 */
const DATA_ID = 'data-id';

/**
 * Row attribute to store the row count.
 */
const DATA_ROW_COUNT = 'data-row-count';

/**
 * Name of no league row in the league selection table.
 * Used to select teams that are not associated to a league.
 */
const NO_LEAGUE = 'No League';

/**
 * Function to react to a checkbox click on the team selection table.
 */
const TEAM_CHECKBOX_FUNCTION =
    'processTeamSelection(this)';

/**
 * Holds the Ids of the teams that have been selected in the team selection table.
 * The Ids are stored as strings.
 */
let checkedTeamIds = [];

/**
 * The class for the data cell in the table that holds the checkbox.
 */
const TD_CHECKBOX_CLASS = 'td-checkbox-class';

/**
 * Stores the count of the last row that was clicked in the league selection table.
 */
let lastLeagueRowClicked = undefined;

/**
 * Identifies the league prompt on the 'Add Teams To League' dialog.
 */
const LEAGUE_PROMPT = 'league-prompt';

/**
 * Identifies the team prompt on the 'Add Teams To League' dialog.
 */
const TEAM_PROMPT = 'team-prompt';

/**
 * The user will only be prompt once, to select a team
 * on the 'Add Teams To League' dialog.
 */
let teamPromptNotShown = true;

/**
 * Text to be displayed with the 'Add Teams' button
 * on the 'Add Teams To League' dialog.
 */
const ADD_TEAMS_BUTTON_TEXT =
    'All selected teams, whether shown or not, are added to league.';

/**
 * Text to be displayed as a warning with the 'Add Teams' button
 * on the 'Add Teams To League' dialog.
 */
const ADD_TEAMS_BUTTON_WARNING =
    'No teams selected.';

/**
 * Indicates whether the add teams button warning is shown.
 */
let addTeamsWarningShown = false;

/**
 * Identifies the text field associate with the 'Add Teams' button
 * on the 'Add Teams To League' dialog.
 */
const ADD_BUTTON_TEXT = 'add-button-text';

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
 * Assigns the openCreateItemDialog function to the create button.
 *
 * Assigns an action to the close button to redirect the user to the home page.
 *
 * Assigns the Escape key to the close button as a shortcut.
 *
 * Uses the class selector to assign the validateInput function to the
 * input field on each dialog.
 *
 * Sets up the events on the three dialogs create, update and delete.
 *
 * Sets up the cancel event on the league team dialog and
 * on the add team to league dialog.
 */
function setUpMaintainLeagueDocument() {
    getLeaguesForTable(undefined, URL_END, ITEM);

    let parameters = {'dialogName' : DIALOG_CREATE_ITEM,
                      'baseDialogName' : DIALOG_LEAGUE_MAINTENANCE};
    $('#create-button').click(parameters, openCreateItemDialog);

    $('#close-button').click(gotoAdminLoginPage);

    document.addEventListener('keydown', assignEscapeToCloseButton);

    setUpDialogButtons(DIALOG_CREATE_ITEM, DIALOG_LEAGUE_MAINTENANCE, createItemOnServer,
        URL_END, 'created', ITEM, reloadLeaguesFromServer);

    setUpDialogButtons(DIALOG_UPDATE_ITEM_NAME, DIALOG_LEAGUE_MAINTENANCE,
        updateItemOnServer, URL_END, 'updated', ITEM, reloadLeaguesFromServer);

    setUpDeleteDialogButtons(DIALOG_DELETE_ITEM, DIALOG_LEAGUE_MAINTENANCE,
        deleteItemOnServer, URL_END, 'deleted', ITEM, reloadLeaguesFromServer);

    setUpCancelEvent(DIALOG_LEAGUE_TEAM_MAINTENANCE);
    setUpCancelEvent(DIALOG_ADD_LEAGUE_TEAMS);

}

/* Read Code Section. */

/**
 * Gets the leagues from the server and displays them in a table.
 * Each row contains a league name, update button, delete button
 * and team button.
 */
function getLeaguesForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd3ButtonTable,
        urlEnd, item, DIALOG_LEAGUE_MAINTENANCE, LEAGUE_LIST,
        'Update', 'updateItemName(this, DIALOG_UPDATE_ITEM_NAME, ' +
            'DIALOG_LEAGUE_MAINTENANCE)',
        'Delete', 'deleteItem(this, DIALOG_DELETE_ITEM, DIALOG_LEAGUE_MAINTENANCE)',
        'Team', 'maintainTeams(this, URL_LEAGUE_TEAM_START, ' +
            'DIALOG_LEAGUE_MAINTENANCE, ' +
            'DIALOG_LEAGUE_TEAM_MAINTENANCE, LEAGUE_TEAM_LIST, ' +
            'setUpMaintainLeagueTeamForm)');
}

/**
 * Gets the teams from the server and displays them in a table.
 * Each row contains a team name and a remove button.
 */
function getLeagueTeamsForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd1ButtonTable,
        urlEnd, item, DIALOG_LEAGUE_TEAM_MAINTENANCE, LEAGUE_TEAM_LIST,
        'Remove', 'deleteItem(this, ' +
            'DIALOG_REMOVE_LEAGUE_TEAM, DIALOG_LEAGUE_TEAM_MAINTENANCE)');
}

/**
 * Invoked after a league create/update/delete.
 * Clears the league table, and reloads it with the new data.
 */
function reloadLeaguesFromServer(newScrollPosition, url, item) {
    $('#' + LEAGUE_LIST).empty();
    getLeaguesForTable(newScrollPosition, url, item);
}

/**
 * Invoked after a team removal.
 * Clears the team table, and reloads it with the new data.
 */
function reloadLeagueTeamsFromServer(newScrollPosition, url, item) {
    $('#' + LEAGUE_TEAM_LIST).empty();
    getLeagueTeamsForTable(newScrollPosition, url, item);
}

/* Maintain Team Code Section. */

/**
 * Called when Maintain League Team dialog is opened.
 *
 * Loads the teams for a given league from the server into the table.
 *
 * Sets up the events on the remove team dialog.
 *
 * Sets up the event on the add teams button.
 *
 * Parameters
 *
 * leagueName - Name of league for which teams are being accessed,
 *            used in table header.
 * urlEnd - URL used to retrive/update teams from a league on the server.
 * item - item being accessed i.e. 'team'.
 * leagueId - id of league for which teams are being accessed.
 */
function setUpMaintainLeagueTeamForm(leagueName, urlEnd, item, leagueId) {
    document.getElementById(DIALOG_TEAM_HEADER).innerHTML = leagueName;
    getLeagueTeamsForTable(undefined, urlEnd, item);

    setUpDeleteDialogButtons(DIALOG_REMOVE_LEAGUE_TEAM, DIALOG_LEAGUE_TEAM_MAINTENANCE,
        deleteItemOnServer, urlEnd, 'removed', item, reloadLeagueTeamsFromServer);

    let dialog = document.getElementById(DIALOG_LEAGUE_TEAM_MAINTENANCE);
    let addTeamsToLeagueButtonElement =
        dialog.getElementsByClassName(ADD_TEAMS_TO_LEAGUE_BUTTON)[0];
    $(addTeamsToLeagueButtonElement).off('click');
    let parameters = {'dialogName' : DIALOG_ADD_LEAGUE_TEAMS,
                      'baseDialogName' : DIALOG_LEAGUE_TEAM_MAINTENANCE,
                      'leagueId' : leagueId,
                      'leagueName' : leagueName,
                      'url' : urlEnd,
                      'item' : item};
    $(addTeamsToLeagueButtonElement).click(parameters, addTeamsToLeague);

}

/**
 * Invoked by the 'Add Teams' button on the 'League Team Maintenance' dialog.
 *
 * Opens the 'Add Teams to League' dialog.
 *
 * Initialises the 'Add Teams to League' dialog. Initialises fields and clears the
 * lists. Also initialises the global variables.
 *
 * Adds an event to the 'Add Teams' button on the 'Add Teams to League' dialog.
 * This button adds all the selected teams to the league on the server.
 */
function addTeamsToLeague(event) {
    let dialogName = event.data.dialogName;
    let baseDialogName = event.data.baseDialogName;
    let leagueId = event.data.leagueId;
    let leagueName = event.data.leagueName;
    let url = event.data.url;
    let item = event.data.item;

    lastLeagueRowClicked = undefined;
    checkedTeamIds = [];
    showLeaguePrompt();
    removeTeamPrompt();
    teamPromptNotShown = true;
    addTeamsWarningShown = false;
    setAddTeamsButtonText();

    clearMaintainMessage(baseDialogName);
    $('#' + LEAGUE_SELECTION_LIST).empty();
    $('#' + TEAM_SELECTION_LIST).empty();
    let dialog = openDialog(dialogName);
    dialog.getElementsByClassName(DIALOG_OBJECT_NAME)[0].innerHTML = leagueName;

    let addTeamsToLeagueButtonElement =
        dialog.getElementsByClassName(ADD_TEAMS_TO_LEAGUE_BUTTON)[0];
    $(addTeamsToLeagueButtonElement).off('click');
    let parameters = {'dialog' : dialog,
                      'baseDialogName' : baseDialogName,
                      'url' : url,
                      'item' : item};
    $(addTeamsToLeagueButtonElement).click(parameters, addTeamsToLeagueOnServer);

    getLeaguesForSelection(dialogName, leagueId, LEAGUE_SELECTION_LIST);

}

/**
 * Sets up the league selection table.
 * Inserts a no league row at the beginning of the table.
 * Gets the leagues from the server.
 *
 * dialogName - Add teams to league dialog.
 * leagueId - id of league teams are being added too.
 * tbodyElementId - id of tbody element, position to display the table.
 */
function getLeaguesForSelection(dialogName, leagueId, tbodyElementId) {
    setUpNoLeagueRow(tbodyElementId, LEAGUE_CHECKBOX_FUNCTION);
    getItemsFromServer(undefined, displayLeaguesInTextAnd1CheckboxTable,
        URL_END, undefined, dialogName, tbodyElementId,
        LEAGUE_CHECKBOX_FUNCTION, leagueId);
}

/**
 * Sets up a no league row in the league selection table.
 * Used to select team that are not associated to a league.
 * 
 * tbodyElementId - id of tbody element, position to add no league row.
 * checkboxFunction - Function executed when checkbox is clicked.
 */
function setUpNoLeagueRow(tbodyElementId, checkboxFunction) {
    let tbodyElement = document.getElementById(tbodyElementId);
    let dataObject = { id : 0, name : NO_LEAGUE };
    let rowElement = createTextAnd1CheckboxRowElement(dataObject, checkboxFunction, 0);
    tbodyElement.appendChild(rowElement);
}

/**
 * Invoked after a successful read request.
 * Displays a text field and one checkbox field in each row of the table,
 * for a league selection.
 *
 * Parameters.
 *
 * dataArr - Data from server.
 * newScrollPosition - is undefined.
 * item - is undefined.
 * dialogName - is undefined.
 * tbodyElementId - id of tbody element, position to display the table.
 * checkboxFunction - Function executed when checkbox is clicked.
 * leagueId - id of league teams are being added too.
 *            This league must not be displayed in selection table.
 */
function displayLeaguesInTextAnd1CheckboxTable(dataArr, newScrollPosition, item,
    dialogName, tbodyElementId, checkboxFunction, leagueId) {
    let tbodyElement = document.getElementById(tbodyElementId);
    let index;
    let rowElement;
    let rowCount = 1; // No league row is row 0
    let dataObject;

    for (index = 0; index < dataArr.length; index++) {
        dataObject = dataArr[index];
        if (leagueId != dataObject.id) {
            rowElement = createTextAnd1CheckboxRowElement(dataObject,
                checkboxFunction, rowCount);
            tbodyElement.appendChild(rowElement);
            rowCount = rowCount + 1;
        }
    }

}

/**
 * Creates a row of a table with 2 data cells. The first cell holds text,
 * the second holds a  checkbox.
 * Stores the data id in the DATA_ID attribute of the row.
 * Stores the row count in the DATA_ROW_COUNT attribute of the row.
 * This is used to uncheck the previous checkbox in the table.
 *
 * Parameters.
 *
 * data - Data from server.
 * checkboxFunction - Function to response to checkbox click.
 * rowCount - Row count.
 */
function createTextAnd1CheckboxRowElement(data, checkboxFunction, rowCount) {
    let rowElement = document.createElement("tr");
    rowElement.setAttribute(DATA_ID, data.id.toString());
    rowElement.setAttribute(DATA_ROW_COUNT, rowCount);

    let cellElement = createDescriptionCellElement(data.name);
    rowElement.appendChild(cellElement);
    cellElement = createCheckboxCellElement(checkboxFunction);
    rowElement.appendChild(cellElement);

    return rowElement;
}

/**
 * Creates a data cell for a checkbox.
 */
function createCheckboxCellElement(checkboxFunction) {
    let cellElement = document.createElement('td');
    cellElement.setAttribute('class', TD_CHECKBOX_CLASS);

    let checkboxElement = document.createElement('input');
    checkboxElement.setAttribute('type', 'checkbox');
    checkboxElement.setAttribute('onclick', checkboxFunction);

    cellElement.appendChild(checkboxElement);
    return cellElement;
}

/**
 * Invoked when a checkbox is clicked in the league selection table.
 *
 * checkboxElement - Checkbox that was clicked. Indicates row.
 */
function processLeagueSelection(checkboxElement) {
    let rowElement = checkboxElement.parentNode.parentNode;
    let leagueId = rowElement.getAttribute(DATA_ID);
    let rowCount = rowElement.getAttribute(DATA_ROW_COUNT);

    resetAddTeamsButtonText();
    if (checkboxElement.checked) {
        processCheckedLeagueBox(leagueId, rowCount);
    } else {
        processUncheckedLeagueBox();
    }
}

/**
 * A league has just been selected.
 *
 * Prompts the user to select a team.
 *
 * Unchecks the previous league selection.
 *
 * Stores the row count in a global variable, so that the row
 * can be unchecked when the user clicks another row.
 *
 * Gets the teams for the team selection dialog.
 *
 * leagueId - League id to select teams for.
 * rowCount - Count of row selected in league selection.
 */
function processCheckedLeagueBox(leagueId, rowCount) {
    promptUserToSelectTeam();
    uncheckPreviousLeagueSelection();
    lastLeagueRowClicked = rowCount;
    $('#' + TEAM_SELECTION_LIST).empty();
    getTeamsForSelection(leagueId);
}

/**
 * Removes the league prompt.
 *
 * Prompts the user to select a team.
 * This prompt is only displayed once on a 'Add Teams To League' dialog.
 */
 function promptUserToSelectTeam() {
    removeLeaguePrompt();  

    if (teamPromptNotShown) {
        showTeamPrompt();
        teamPromptNotShown = false;
    }
 }

/**
 * Unchecks the previous league selection, if any,
 * by using the global variable.
 */
function uncheckPreviousLeagueSelection() {
    if (lastLeagueRowClicked != undefined) {
        let tbodyElement = document.getElementById(LEAGUE_SELECTION_LIST);
        let rowElement = tbodyElement.childNodes[lastLeagueRowClicked];
        let checkboxElement =
            rowElement.getElementsByClassName(TD_CHECKBOX_CLASS)[0].firstChild;
        checkboxElement.checked = false;
    }

}
/**
 * Gets the teams for the team selection dialog.
 *
 * leagueId - League id to select teams for.
 */
function getTeamsForSelection(leagueId) {
    let url = URL_LEAGUE_TEAM_START + '/' + leagueId + '/' + URL_TEAM_END;
    getItemsFromServer(undefined, displayTeamsInTextAnd1CheckboxTable,
        url, undefined, undefined, TEAM_SELECTION_LIST, TEAM_CHECKBOX_FUNCTION);
}

/**
 * Invoked after a successful read request.
 * Displays a text field and one checkbox field in each row of the table,
 * for a team selection.
 *
 * Parameters.
 *
 * dataArr - Data from server.
 * newScrollPosition - is undefined.
 * item - is undefined.
 * dialogName - is undefined.
 * tbodyElementId - id of tbody element, position to display the table.
 * checkboxFunction - Function executed when checkbox is clicked.
 */
function displayTeamsInTextAnd1CheckboxTable(dataArr, newScrollPosition, item,
    dialogName, tbodyElementId, checkboxFunction) {
    let tbodyElement = document.getElementById(tbodyElementId);
    let index;
    let rowElement;

    for (index = 0; index < dataArr.length; index++) {
        rowElement = createTeamTextAnd1CheckboxRowElement(dataArr[index],
            checkboxFunction);
        tbodyElement.appendChild(rowElement);
    }

}

/**
 * Creates a row of a table with 2 data cells. The first cell holds text,
 * the second holds a  checkbox. Different code is required for the team
 * selection dialog. The code checks whether the checkbox has been previously
 * checked.
 * Stores the data id in the DATA_ID attribute of the row.
 *
 * Parameters.
 *
 * data - Data from server.
 * checkboxFunction - Function to response to checkbox click.
 */
function createTeamTextAnd1CheckboxRowElement(data, checkboxFunction) {
    let rowElement = document.createElement("tr");
    rowElement.setAttribute(DATA_ID, data.id);

    let cellElement = createDescriptionCellElement(data.name);
    rowElement.appendChild(cellElement);
    cellElement = createTeamCheckboxCellElement(checkboxFunction, data.id);
    rowElement.appendChild(cellElement);

    return rowElement;
}

/**
 * Creates a data cell for a checkbox for the team selection dialog.
 *
 * Sets the box, if it has been previously checked by the user,
 * by checking the global variable.
 *
 * checkboxFunction - Function to respond to checkbox click.
 * teamId - team id.
 */
function createTeamCheckboxCellElement(checkboxFunction, teamId) {
    let cellElement = document.createElement('td');

    let checkboxElement = document.createElement('input');
    checkboxElement.setAttribute('type', 'checkbox');
    checkboxElement.setAttribute('onclick', checkboxFunction);

    if (checkedTeamIds.indexOf(teamId.toString()) != -1) {
        checkboxElement.checked = true;
    }

    cellElement.appendChild(checkboxElement);
    return cellElement;
}

/**
 * Only one league should be checked at any given time.
 * If the user unchecks the league, re-initialise the global variable
 * and clear the team selection dialog.
 */
function processUncheckedLeagueBox() {
    lastLeagueRowClicked = undefined;
    removeTeamPrompt();
    $('#' + TEAM_SELECTION_LIST).empty();

}

/**
 * Invoked when a checkbox is clicked in the team selection table.
 * If the box is checked, add the team id to the global array,
 * remove the team prompt and reset the text associated with the 'Add Teams' button.
 *
 * If the box is unchecked, remove the team id from the global array.
 *
 * checkboxElement - Checkbox that was clicked. Indicates row.
 */
function processTeamSelection(checkboxElement) {
    let rowElement = checkboxElement.parentNode.parentNode;
    let teamId = rowElement.getAttribute(DATA_ID);

    if (checkboxElement.checked) {
        removeTeamPrompt();
        resetAddTeamsButtonText();
        checkedTeamIds.push(teamId);
    } else {
        let index = checkedTeamIds.indexOf(teamId);
        checkedTeamIds.splice(index, 1);
    }
}

/**
 * Sets the 'Add Teams' button text on the 'Add Teams To League' dialog.
 */
function setAddTeamsButtonText() {
    addTextToAddTeamsButton('green', ADD_TEAMS_BUTTON_TEXT);
}

/**
 * Resets the 'Add Teams' button text on the 'Add Teams To League' dialog,
 * in case a warning was shown.
 */
function resetAddTeamsButtonText() {
    if (addTeamsWarningShown) {
        addTextToAddTeamsButton('green', ADD_TEAMS_BUTTON_TEXT);
        addTeamsWarningShown = false;
    }
}

/**
 * Adds text to the 'Add Teams' button on the 'Add Teams To League' dialog.
 *
 * colour - Colour to display text in 'red'/'green'.
 * text - Text to be displayed at button.
 */
function addTextToAddTeamsButton(colour, text) {
    let dialog = document.getElementById(DIALOG_ADD_LEAGUE_TEAMS);
    let textFieldElement = dialog.getElementsByClassName(ADD_BUTTON_TEXT)[0];
    textFieldElement.innerHTML = text;
    textFieldElement.style.color = colour;
}

/**
 * Displays the league prompt.
 */
function showLeaguePrompt() {
    setVisibilityOnPrompt(LEAGUE_PROMPT, 'visible');
}

/**
 * Removes the team prompt.
 */
function removeLeaguePrompt() {
    setVisibilityOnPrompt(LEAGUE_PROMPT, 'hidden');
}

/**
 * Displays the team prompt.
 */
function showTeamPrompt() {
    setVisibilityOnPrompt(TEAM_PROMPT, 'visible');
}

/**
 * Removes the team prompt.
 */
function removeTeamPrompt() {
    setVisibilityOnPrompt(TEAM_PROMPT, 'hidden');
}

/**
 * Sets the visibility on a prompt field.
 *
 * prompt - Prompt field to set the visibility on.
 * visibility - Visibility to be set on prompt 'visible'/'hidden'.
 */
function setVisibilityOnPrompt(prompt, visibility) {
    let dialog = document.getElementById(DIALOG_ADD_LEAGUE_TEAMS);
    let promptElement = dialog.getElementsByClassName(prompt)[0];
    promptElement.style.visibility = visibility;
}

/**
 * Invoked by 'Add Teams' button on the 'Add Teams To League' dialog.
 *
 * Sends a request to the server if there are teams to be added to the league.
 */
function addTeamsToLeagueOnServer(event) {
    if (checkedTeamIds.length == 0) {
        addTextToAddTeamsButton('red', ADD_TEAMS_BUTTON_WARNING);
        addTeamsWarningShown = true;
    } else {
        setTeamsToLeagueOnServer(event);
    }
}

/**
 * Sends a request to the server to add the teams to the league.
 */
function setTeamsToLeagueOnServer(event) {
    let dialog = event.data.dialog;
    let baseDialogName = event.data.baseDialogName;
    let urlForReload = event.data.url;
    let item = event.data.item;

    let urlForRequest = urlForReload + '/' + checkedTeamIds.toString();
    let type = 'PUT';

    submitUrlToServer(urlForRequest, type, serverSuccessAfterSubmitUrl, dialog,
        baseDialogName, reloadLeagueTeamsFromServer, undefined, urlForReload, item)
}

/**
 * Submits a plain url to the server, i.e.  no form,
 * for a given url and HTTP method type.
 *
 * Parameters.
 *
 * urlForRequest - URL to be used.
 * type - Type of method to be used in request, e.g. GET/POST/PUT.
 * successFunction - Function to be executed after a successful request.
 * dialog - Dialog 'element' being processed.
 * baseDialogName - The dialog name from which the above dialog is propagated from.
 * reloadTableFunction - Function to be used to reload the items into the table.
 * newScrollPosition - Scroll position to be used in table after a successful request.
 * urlForReload - URL to be used to reload the items into the table.
 * Item being processed, league/club/team.
 */
function submitUrlToServer(urlForRequest, type, successFunction, dialog,
    baseDialogName, reloadTableFunction, newScrollPosition, urlForReload, item) {
    $.ajax({
        async: false,
        cache: false,
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    successFunction(result, dialog, baseDialogName, reloadTableFunction,
                        newScrollPosition, urlForReload, item);
                },
        type: type,
        url: urlForRequest
    });
}

/**
 * Invoked after a plain URL (no form) has been successfully submitted to the server.
 *
 * Reloads the items from the server, closes the dialog
 * and displays a message on the previous screen.
 *
 * Parameters.
 *
 * response - Response from server.
 * dialog - Dialog 'element' being processed.
 * baseDialogName - The dialog name from which the above dialog is propagated from.
 *                  This is needed to display the maintenance message.
 * reloadTableFunction - Function to be used to reload the items into the table.
 * newScrollPosition - Scroll position to be used in table after a successful request.
 * url - URL to be used to reload the items into the table.
 * item - Item being processed, league/club/team.
 */
function serverSuccessAfterSubmitUrl(response, dialog, baseDialogName,
    reloadTableFunction, newScrollPosition, url, item) {

    if (response.status) {
        cancelDialog(dialog);
        reloadTableFunction(newScrollPosition, url, item);
        displayMaintainMessage(baseDialogName, response.message);
    }
}

/**
 * Object to expose variables/functions to test service.
 */
var leagueExports = {};

leagueExports.setUpDialogButtons = setUpDialogButtons;
leagueExports.setUpDeleteDialogButtons = setUpDeleteDialogButtons;
leagueExports.displayError = displayError;
leagueExports.openCreateLeagueDialog = openCreateLeagueDialog;
leagueExports.createItemOnServer = createItemOnServer;
leagueExports.openDialog = openDialog;
leagueExports.initialiseDialog = initialiseDialog;
leagueExports.getLeaguesFromServer = getLeaguesFromServer;
leagueExports.displayLeaguesInTable = displayLeaguesInTable;
leagueExports.displayLeagues = displayLeagues;
leagueExports.updateLeague = updateLeague;
leagueExports.updateItemOnServer = updateItemOnServer;
leagueExports.deleteLeague = deleteLeague;
module.exports = leagueExports;
