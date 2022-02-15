"use strict";

/**
 * The name of the club maintenance dialog. The base dialog of the web page.
 */
 const DIALOG_CLUB_MAINTENANCE = 'dialog-club-maintenance';

/**
 * The id of the tbody element of the club table.
 */
 const CLUB_LIST = 'club-list';

/**
 * The id of the tbody element of the team table.
 */
 const TEAM_LIST = 'team-list';

/**
 * The name of the current item i.e. league/club/team.
 */
 const ITEM = 'Club';

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
 * The name of the team maintenance dialog.
 */
 const DIALOG_TEAM_MAINTENANCE = 'dialog-club-team-maintenance';

/**
 * The name of the create team dialog.
 */
 const DIALOG_CREATE_CLUB_TEAM = 'dialog-create-club-team';

/**
 * The name of the update team dialog.
 */
 const DIALOG_UPDATE_CLUB_TEAM_NAME = 'dialog-update-club-team-name';

/**
 * The name of the delete team dialog.
 */
 const DIALOG_DELETE_CLUB_TEAM = 'dialog-delete-club-team';

/**
 * The URL end part that relates to the current item i.e.  Clubs
 */
 const URL_END = 'clubs';

/**
 * The URL start part that relates to the team processing.
 */
 const URL_TEAM_START = 'club';

/**
 * The URL end part that relates to the team processing.
 */
 const URL_TEAM_END = 'teams';

/**
 * Correction for scroll position in the table.
 * The JQuery .position() method returns the position of a element relative to
 * the offset parent, in this case the tbody element relative to the table element.
 * Therefore the scroll adjustment =
 * height of the thead element + padding (30px) + height of the tbody top border (2px).
 */
const SCROLL_ADJUSTMENT = 32;

/**
 * Called when Maintain Club page is loaded.
 *
 * Loads the clubs from the server into the table.
 *
 * Assigns the openCreateItemDialog function to the create button.
 *
 * Uses the class selector to assign the validateInput function to the
 * input field on each dialog.
 *
 * Sets up the events on the three dialogs create, update and delete.
 */
function setUpMaintainClubDocument() {
    getClubsForTable(undefined, URL_END, ITEM);

    let parameters = {'dialogName' : DIALOG_CREATE_ITEM,
                      'baseDialogName' : DIALOG_CLUB_MAINTENANCE};
    $('#create-button').click(parameters, openCreateItemDialog);

    setUpDialogButtons(DIALOG_CREATE_ITEM, DIALOG_CLUB_MAINTENANCE, createItemOnServer,
        URL_END, 'created', ITEM, reloadClubsFromServer);

    setUpDialogButtons(DIALOG_UPDATE_ITEM_NAME, DIALOG_CLUB_MAINTENANCE,
        updateItemOnServer, URL_END, 'updated', ITEM, reloadClubsFromServer);

    setUpDeleteDialogButtons(DIALOG_DELETE_ITEM, DIALOG_CLUB_MAINTENANCE,
        deleteItemOnServer, URL_END, ITEM, reloadClubsFromServer);

    /*
    Attach function to create and cancel button on maintain team dialog.
    This should only be done once.
    */
    parameters = {'dialogName' : DIALOG_CREATE_CLUB_TEAM,
                  'baseDialogName' : DIALOG_TEAM_MAINTENANCE};
    $('#create-team-button').click(parameters, openCreateItemDialog);

    let dialog = document.getElementById(DIALOG_TEAM_MAINTENANCE);
    let cancelButtonElement = dialog.getElementsByClassName('dialog-cancel-button')[0];
    parameters = {'dialog' : dialog};
    $(cancelButtonElement).click(parameters, cancelDialogViaButton);

}

/* Read Code Section. */

function getClubsForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd3ButtonTable,
        urlEnd, item, DIALOG_CLUB_MAINTENANCE, CLUB_LIST,
        'Update', 'updateItemName(this, DIALOG_UPDATE_ITEM_NAME, ' +
            'DIALOG_CLUB_MAINTENANCE)',
        'Delete', 'deleteItem(this, DIALOG_DELETE_ITEM, DIALOG_CLUB_MAINTENANCE)',
        'Team', 'maintainTeams(this, URL_TEAM_START, DIALOG_CLUB_MAINTENANCE, ' +
            'DIALOG_TEAM_MAINTENANCE, TEAM_LIST)');
}

function getTeamsForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd2ButtonTable,
        urlEnd, item, DIALOG_TEAM_MAINTENANCE, TEAM_LIST,
        'Update', 'updateItemName(this, DIALOG_UPDATE_CLUB_TEAM_NAME, DIALOG_TEAM_MAINTENANCE)',
        'Delete', 'deleteItem(this, DIALOG_DELETE_CLUB_TEAM, DIALOG_TEAM_MAINTENANCE)');
}

function reloadClubsFromServer(newScrollPosition, url, item) {
    $('#' + CLUB_LIST).empty();
    getClubsForTable(newScrollPosition, url, item);
}

function reloadTeamsFromServer(newScrollPosition, url, item) {
    $('#' + TEAM_LIST).empty();
    getTeamsForTable(newScrollPosition, url, item);
}

/* Maintain Team Code Section. */

/**
 * Invoked by team button in table.
 * Team button is in third data cell of row.
 * Retrieve the item id and name from the table,
 * construct the teams url
 * and open the teams dialog.
 *
 * Parameters
 *
 * buttonElement - Button element which invoked the action, defining row.
 * url_start - Start of URL to list the teams in the table. The club id has
 *             appended to this string.
 * baseDialogName - The dialog from which the below dialog is propagated from.
 *                  This is required to clear the main maintenance message
 *                  on the previous dialog.
 * dialogName - The dialog which is being opened.
 */
function maintainTeams(buttonElement, url_start, baseDialogName, dialogName,
    tbodyElementId) {
    clearMaintainMessage(baseDialogName);
    clearMaintainMessage(dialogName);
    $('#' + tbodyElementId).empty();
    let rowElement = buttonElement.parentNode.parentNode;
    let itemId = rowElement.getAttribute("data-id");
    let url = url_start + '/' + itemId + '/' + URL_TEAM_END;
    // get name from first data cell in row.
    let itemName = rowElement.firstChild.firstChild.nodeValue;
    openMaintainTeamDialog(itemName, url, dialogName);
}

/**
 * Opens the team maintenance dialog.
 * Display the old item name and store its database id number.
 */
function openMaintainTeamDialog(itemName, teamUrl, dialogName) {
    // access openDialog via leagueExports to allow 'mocha' to stub it out.
    let dialog = openDialog(dialogName);
    setUpMaintainTeamForm(itemName, teamUrl, 'team');
}

/**
 * Called when Maintain Team dialog is opened.
 *
 * Loads the clubs from the server into the table.
 *
 * Assigns the openCreateItemDialog function to the create button.
 *
 * Uses the class selector to assign the validateInput function to the
 * input field on each dialog.
 *
 * Sets up the events on the three dialogs create, update and delete.
 */
function setUpMaintainTeamForm(itemName, urlEnd, item) {
    document.getElementById('dialog-club').innerHTML = itemName;
    getTeamsForTable(undefined, urlEnd, item);

    setUpDialogButtons(DIALOG_CREATE_CLUB_TEAM, DIALOG_TEAM_MAINTENANCE,
        createItemOnServer, urlEnd, 'created', item, reloadTeamsFromServer);

    setUpDialogButtons(DIALOG_UPDATE_CLUB_TEAM_NAME, DIALOG_TEAM_MAINTENANCE,
        updateItemOnServer, urlEnd, 'updated', item, reloadTeamsFromServer);

    setUpDeleteDialogButtons(DIALOG_DELETE_CLUB_TEAM, DIALOG_TEAM_MAINTENANCE,
        deleteItemOnServer, urlEnd, item, reloadTeamsFromServer);

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
