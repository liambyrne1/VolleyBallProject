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
 * Uses the class selector to assign the validateInput function to the
 * input field on each dialog.
 *
 * Sets up the events on the three dialogs create, update and delete.
 */
function setUpMaintainLeagueDocument() {
    getLeaguesForTable(undefined, URL_END, ITEM);

    let parameters = {'dialogName' : DIALOG_CREATE_ITEM,
                      'baseDialogName' : DIALOG_LEAGUE_MAINTENANCE};
    $('#create-button').click(parameters, openCreateItemDialog);

    setUpDialogButtons(DIALOG_CREATE_ITEM, DIALOG_LEAGUE_MAINTENANCE, createItemOnServer,
        URL_END, 'created', ITEM, reloadLeaguesFromServer);

    setUpDialogButtons(DIALOG_UPDATE_ITEM_NAME, DIALOG_LEAGUE_MAINTENANCE,
        updateItemOnServer, URL_END, 'updated', ITEM, reloadLeaguesFromServer);

    setUpDeleteDialogButtons(DIALOG_DELETE_ITEM, DIALOG_LEAGUE_MAINTENANCE,
        deleteItemOnServer, URL_END, ITEM, reloadLeaguesFromServer);

}

/* Read Code Section. */

function getLeaguesForTable(newScrollPosition, urlEnd, item) {
    getItemsFromServer(newScrollPosition, displayItemsInTextAnd3ButtonTable,
        urlEnd, item, DIALOG_LEAGUE_MAINTENANCE, LEAGUE_LIST,
        'Update', 'updateItemName(this, DIALOG_UPDATE_ITEM_NAME, ' +
            'DIALOG_LEAGUE_MAINTENANCE)',
        'Delete', 'deleteItem(this, DIALOG_DELETE_ITEM, DIALOG_LEAGUE_MAINTENANCE)',
        'Team', 'maintainTeams()');
}

function reloadLeaguesFromServer(newScrollPosition, url, item) {
    $('#' + LEAGUE_LIST).empty();
    getLeaguesForTable(newScrollPosition, url, item);
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
    alert('Not yet implemented');
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
