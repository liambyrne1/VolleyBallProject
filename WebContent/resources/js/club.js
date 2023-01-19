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
 * The id of the tbody element of the team table.
 */
 const TEAM_LIST = 'team-list';

/**
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
 * Sets up the events on the three dialogs create, update and delete.
 *
 * Sets up the create and cancel events on the team maintenance dialog.
 */
function setUpMaintainClubDocument() {
    getClubsForTable(undefined, URL_END, ITEM);

    let parameters = {'dialogName' : DIALOG_CREATE_ITEM,
                      'baseDialogName' : DIALOG_CLUB_MAINTENANCE};
    $('#create-button').click(parameters, openCreateItemDialog);

    $('#close-button').click(gotoAdminLoginPage);

    document.addEventListener('keydown', assignEscapeToCloseButton);

    setUpDialogButtons(DIALOG_CREATE_ITEM, DIALOG_CLUB_MAINTENANCE, createItemOnServer,
        URL_END, 'created', ITEM, reloadClubsFromServer);

    setUpDialogButtons(DIALOG_UPDATE_ITEM_NAME, DIALOG_CLUB_MAINTENANCE,
        updateItemOnServer, URL_END, 'updated', ITEM, reloadClubsFromServer);

    setUpDeleteDialogButtons(DIALOG_DELETE_ITEM, DIALOG_CLUB_MAINTENANCE,
        deleteItemOnServer, URL_END, 'deleted', ITEM, reloadClubsFromServer);

    parameters = {'dialogName' : DIALOG_CREATE_CLUB_TEAM,
                  'baseDialogName' : DIALOG_TEAM_MAINTENANCE};
    $('#create-team-button').click(parameters, openCreateItemDialog);

    setUpCancelEvent(DIALOG_TEAM_MAINTENANCE);

}

/* Read Code Section. */

/**
 * Gets the clubs from the server and displays them in a table.
 * Each row contains a club name, update button, delete button
 * and team button.
 */
function getClubsForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd3ButtonTable,
        urlEnd, item, DIALOG_CLUB_MAINTENANCE, CLUB_LIST,
        'Update', 'updateItemName(this, DIALOG_UPDATE_ITEM_NAME, ' +
            'DIALOG_CLUB_MAINTENANCE)',
        'Delete', 'deleteItem(this, DIALOG_DELETE_ITEM, DIALOG_CLUB_MAINTENANCE)',
        'Team', 'maintainTeams(this, URL_TEAM_START, DIALOG_CLUB_MAINTENANCE, ' +
            'DIALOG_TEAM_MAINTENANCE, TEAM_LIST, setUpMaintainTeamForm)');
}

/**
 * Gets the teams from the server and displays them in a table.
 * Each row contains a team name, update button and delete button.
 */
function getTeamsForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd2ButtonTable,
        urlEnd, item, DIALOG_TEAM_MAINTENANCE, TEAM_LIST,
        'Update', 'updateItemName(this, DIALOG_UPDATE_CLUB_TEAM_NAME, DIALOG_TEAM_MAINTENANCE)',
        'Delete', 'deleteItem(this, DIALOG_DELETE_CLUB_TEAM, DIALOG_TEAM_MAINTENANCE)');
}

/**
 * Invoked after a club create/update/delete.
 * Clears the club table, and reloads it with the new data.
 */
function reloadClubsFromServer(newScrollPosition, url, item) {
    $('#' + CLUB_LIST).empty();
    getClubsForTable(newScrollPosition, url, item);
}

/**
 * Invoked after a team create/update/delete.
 * Clears the team table, and reloads it with the new data.
 */
function reloadTeamsFromServer(newScrollPosition, url, item) {
    $('#' + TEAM_LIST).empty();
    getTeamsForTable(newScrollPosition, url, item);
}

/* Maintain Team Code Section. */

/**
 * Called when Maintain Club Team dialog is opened.
 *
 * Loads the teams for a given club from the server into the table.
 *
 * Sets up the events on the three team dialogs
 * create team, update team and delete team.
 *
 * Parameters
 *
 * itemName - Name of club for which teams are being accessed,
 *            used in table header.
 * urlEnd - URL used to retrive/update teams from server.
 * item - item being accessed i.e. 'team'.
 */
function setUpMaintainTeamForm(itemName, urlEnd, item) {
    document.getElementById(DIALOG_TEAM_HEADER).innerHTML = itemName;
    getTeamsForTable(undefined, urlEnd, item);

    setUpDialogButtons(DIALOG_CREATE_CLUB_TEAM, DIALOG_TEAM_MAINTENANCE,
        createItemOnServer, urlEnd, 'created', item, reloadTeamsFromServer);

    setUpDialogButtons(DIALOG_UPDATE_CLUB_TEAM_NAME, DIALOG_TEAM_MAINTENANCE,
        updateItemOnServer, urlEnd, 'updated', item, reloadTeamsFromServer);

    setUpDeleteDialogButtons(DIALOG_DELETE_CLUB_TEAM, DIALOG_TEAM_MAINTENANCE,
        deleteItemOnServer, urlEnd, 'deleted', item, reloadTeamsFromServer);

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
