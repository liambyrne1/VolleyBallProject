"use strict";

/**
 * Common message displayed when server is unable to respond.
 */
const serverUnavailabeMessage = 'Volleyball System is unavailable';

/**
 * Variables used in ally.js code.
 * Code used for handling dialogs.
 */
let disabledHandle;
let tabHandle;
let keyHandle;
let hiddenHandle;

/* Keeps a list of the opened dialogs, in the order in which they were opened. */
const openedDialogs = [];

/**
 * The URL end part that relates to the team processing.
 */
 const URL_TEAM_END = 'teams';

/**
 * Identifies the header in the team table.
 */
 const DIALOG_TEAM_HEADER = 'dialog-team-header';

/**
 * Identifies a name holder on a form, either for a club or league name.
 */
const DIALOG_OBJECT_NAME = 'dialog-object-name';

/* Set up dialog button Code Section. */

/**
 * Sets up the dialog buttons on the create/update dialogs. Both dialogs
 * have embedded forms, and are activated by the form submit button,
 *
 * Passes the form element to the submit function as an event data parameter.
 *
 * Assigns a function to the submit button, the parameter pass into this
 * function.
 *
 * Assigns an input event to the input field of the dialog. The event calls the
 * validateInput function.
 *
 * Assigns the 'cancelDialog' function to the cancel button.
 *
 * Parameters.
 *
 * Dialog being processed, create/update dialog.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 * Function assigned to create/update button.
 * URL stem that identifies the URL to be used.
 * Action taken 'created'/'updated'.
 * Item being updated, league/club/team.
 * Function to be used to reload the items into the table.
 */
function setUpDialogButtons(dialogName, baseDialogName, serverFunction, urlStem, action,
    item, reloadTableFunction) {
    let dialog = document.getElementById(dialogName);
    let dialogFormElement = dialog.getElementsByClassName('dialog-form')[0];
    $(dialogFormElement).off('submit');
    let submitparams = {'dialog' : dialog,
                        'baseDialogName' : baseDialogName,
                        'form' : dialogFormElement,
                        'urlStem' : urlStem,
                        'action' : action,
                        'item' : item,
                        'reloadTableFunction' : reloadTableFunction};
    $(dialogFormElement).submit(submitparams, serverFunction);


    let inputElement =
        dialog.getElementsByClassName('dialog-input')[0];
    $(inputElement).off('input');
    let parameters = {'dialog' : dialog,
                      'inputElement' : inputElement};
    $(inputElement).on('input', parameters, validateInput);

    let cancelButtonElement =
        dialog.getElementsByClassName('dialog-cancel-button')[0];
    $(cancelButtonElement).off('click');
    parameters = {'dialog' : dialog};
    $(cancelButtonElement).click(parameters, cancelDialogViaButton);
}

/**
 * Assign functions to the Delete and Cancel buttons on the Delete dialog.
 *
 * Parameters.
 *
 * Dialog being processed, delete dialog.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 * Function assigned to delete button.
 * URL stem that identifies the URL to be used.
 * Action taken 'deleted'/'removed'.
 * Item being updated, league/club/team.
 * Function to be used to reload the items into the table.
 */
function setUpDeleteDialogButtons(dialogName, baseDialogName, serverFunction, urlStem,
    action, item, reloadTableFunction) {
    let dialog = document.getElementById(dialogName);
    
    let deleteButtonElement =
        dialog.getElementsByClassName('dialog-delete-button')[0];
    $(deleteButtonElement).off('click');
    let parameters = {'dialog' : dialog,
                      'baseDialogName' : baseDialogName,
                      'urlStem' : urlStem,
                      'action' : action,
                      'item' : item,
                      'reloadTableFunction' : reloadTableFunction};
    $(deleteButtonElement).click(parameters, serverFunction);

    let cancelButtonElement =
        dialog.getElementsByClassName('dialog-cancel-button')[0];
    $(cancelButtonElement).off('click');
    parameters = {'dialog' : dialog};
    $(cancelButtonElement).click(parameters, cancelDialogViaButton);
}

/* Read Code Section. */

/**
 * Requests the items from the server.
 * If successful, displays items in a table.
 *
 * Parameters.
 *
 * Initial scroll position of table when displayed.
 * Function to display items after a successful request.
 * URL stem that identifies the URL to be used.
 * Item being updated, league/club/team.
 * dialogName - The opened dialog.
 *              This is required to display the 'no items' message.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 * Button 3 text
 * Button 3 function
 */
function getItemsFromServer(newScrollPosition, displayItemsFunction,
    url, item, dialogName, tbodyElementId, button1Text, button1Function,
    button2Text, button2Function, button3Text, button3Function) {
    $.ajax({
        async: true,
        cache: false,
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    displayItemsFunction(result, newScrollPosition, item,
                        dialogName, tbodyElementId, button1Text, button1Function,
                        button2Text, button2Function, button3Text, button3Function);
                },
        type: "GET",
        url: url
    });
}

/**
 * Invoked after a successful read request.
 * Displays a text field and one button field in each row of the table.
 *
 * Parameters.
 *
 * Data from server.
 * Initial scroll position of table when displayed.
 * Item being updated, league/club/team.
 * dialogName - The opened dialog.
 *              This is required to display the 'no items' message.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 */
function displayItemsInTextAnd1ButtonTable(dataArr, newScrollPosition, item,
    dialogName, tbodyElementId, button1Text, button1Function) {
    let errorMessage = 'No ' + item + 's exist.';

    if (dataArr.length == 0) {
        displayMaintainMessage(dialogName, errorMessage);
    } else {
        displayTextAnd1ButtonTable(dataArr, newScrollPosition,
            tbodyElementId, button1Text, button1Function);
    }
}

/**
 * Displays a text field and one button field in each row of the table.
 * Displays the items in the table. Positions the scrollbar,
 * so that the item name in 'newScrollPosition', is displayed
 * at the top of the table.
 * 'newScrollPosition' is undefined when items are loaded for the first
 * time, screen initialisation. Use the first item returned from server.
 *
 * Parameters.
 *
 * Data from server.
 * Initial scroll position of table when displayed.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 */
function displayTextAnd1ButtonTable(dataArr, newScrollPosition,
    tbodyElementId, button1Text, button1Function) {
    let tbodyElement = document.getElementById(tbodyElementId);
    let index;
    let rowElement;
    let newScrollElementPosition;

    if (newScrollPosition == undefined) {
        newScrollPosition = dataArr[0].name;
    }

    for (index = 0; index < dataArr.length; index++) {
        rowElement = createTextAnd1ButtonRowElement(dataArr[index],
            button1Text, button1Function);
        if (newScrollPosition.localeCompare(dataArr[index].name) == 0) {
            newScrollElementPosition = rowElement;
        }
        tbodyElement.appendChild(rowElement);
    }

    let scrollPosition = $(newScrollElementPosition).position();
    $(tbodyElement).scrollTop(scrollPosition.top - SCROLL_ADJUSTMENT);
}

/**
 * Invoked after a successful read request.
 * Displays a text field and two button fields in each row of the table.
 *
 * Parameters.
 *
 * Data from server.
 * Initial scroll position of table when displayed.
 * Item being updated, league/club/team.
 * dialogName - The opened dialog.
 *              This is required to display the 'no items' message.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 */
function displayItemsInTextAnd2ButtonTable(dataArr, newScrollPosition, item,
    dialogName, tbodyElementId, button1Text, button1Function, button2Text,
    button2Function) {
    let errorMessage = 'No ' + item + 's exist.';

    if (dataArr.length == 0) {
        displayMaintainMessage(dialogName, errorMessage);
    } else {
        displayTextAnd2ButtonTable(dataArr, newScrollPosition,
            tbodyElementId, button1Text, button1Function,
            button2Text, button2Function);
    }
}

/**
 * Displays a text field and two button fields in each row of the table.
 * Displays the items in the table. Positions the scrollbar,
 * so that the item name in 'newScrollPosition', is displayed
 * at the top of the table.
 * 'newScrollPosition' is undefined when items are loaded for the first
 * time, screen initialisation. Use the first item returned from server.
 *
 * Parameters.
 *
 * Data from server.
 * Initial scroll position of table when displayed.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 */
function displayTextAnd2ButtonTable(dataArr, newScrollPosition,
    tbodyElementId, button1Text, button1Function, button2Text, button2Function) {
    let tbodyElement = document.getElementById(tbodyElementId);
    let index;
    let rowElement;
    let newScrollElementPosition;

    if (newScrollPosition == undefined) {
        newScrollPosition = dataArr[0].name;
    }

    for (index = 0; index < dataArr.length; index++) {
        rowElement = createTextAnd2ButtonRowElement(dataArr[index],
            button1Text, button1Function, button2Text, button2Function);
        if (newScrollPosition.localeCompare(dataArr[index].name) == 0) {
            newScrollElementPosition = rowElement;
        }
        tbodyElement.appendChild(rowElement);
    }

    let scrollPosition = $(newScrollElementPosition).position();
    $(tbodyElement).scrollTop(scrollPosition.top - SCROLL_ADJUSTMENT);
}

/**
 * Invoked after a successful read request.
 * Displays a text field and three button fields in each row of the table.
 *
 * Parameters.
 *
 * Data from server.
 * Initial scroll position of table when displayed.
 * Item being updated, league/club/team.
 * dialogName - The opened dialog.
 *              This is required to display the 'no items' message.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 * Button 3 text
 * Button 3 function
 */
function displayItemsInTextAnd3ButtonTable(dataArr, newScrollPosition, item,
    dialogName, tbodyElementId, button1Text, button1Function, button2Text,
    button2Function, button3Text, button3Function) {
    let errorMessage = 'No ' + item + 's exist.';

    if (dataArr.length == 0) {
        displayMaintainMessage(dialogName, errorMessage);
    } else {
        displayTextAnd3ButtonTable(dataArr, newScrollPosition,
            tbodyElementId, button1Text, button1Function,
            button2Text, button2Function, button3Text, button3Function);
    }
}

/**
 * Displays a text field and three button fields in each row of the table.
 * Displays the items in the table. Positions the scrollbar,
 * so that the item name in 'newScrollPosition', is displayed
 * at the top of the table.
 * 'newScrollPosition' is undefined when items are loaded for the first
 * time, screen initialisation. Use the first item returned from server.
 *
 * Parameters.
 *
 * Data from server.
 * Initial scroll position of table when displayed.
 * id of tbody element, position to display the table.
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 * Button 3 text
 * Button 3 function
 */
function displayTextAnd3ButtonTable(dataArr, newScrollPosition,
    tbodyElementId, button1Text, button1Function, button2Text, button2Function,
    button3Text, button3Function) {
    let tbodyElement = document.getElementById(tbodyElementId);
    let index;
    let rowElement;
    let newScrollElementPosition;

    if (newScrollPosition == undefined) {
        newScrollPosition = dataArr[0].name;
    }

    for (index = 0; index < dataArr.length; index++) {
        rowElement = createTextAnd3ButtonRowElement(dataArr[index],
            button1Text, button1Function, button2Text, button2Function,
            button3Text, button3Function);
        if (newScrollPosition.localeCompare(dataArr[index].name) == 0) {
            newScrollElementPosition = rowElement;
        }
        tbodyElement.appendChild(rowElement);
    }

    let scrollPosition = $(newScrollElementPosition).position();
    $(tbodyElement).scrollTop(scrollPosition.top - SCROLL_ADJUSTMENT);
}

/**
 * Creates a row of a table with 2 data cells. The first cell holds text,
 * the second cell holds a button.
 * Store the data id in the 'data-id' attribute of the row.
 *
 * Parameters.
 *
 * Data
 * Button 1 text
 * Button 1 function
 */
function createTextAnd1ButtonRowElement(data, button1Text, button1Function) {
    let rowElement = document.createElement("tr");
    rowElement.setAttribute("data-id", data.id.toString());

    let cellElement = createDescriptionCellElement(data.name);
    rowElement.appendChild(cellElement);
    cellElement = createButtonCellElement(button1Text, button1Function);
    rowElement.appendChild(cellElement);

    return rowElement;
}

/**
 * Creates a row of a table with 3 data cells. The first cell holds text,
 * the remaining two cells hold buttons.
 * Store the data id in the 'data-id' attribute of the row.
 *
 * Parameters.
 *
 * Data
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 */
function createTextAnd2ButtonRowElement(data, button1Text, button1Function,
        button2Text, button2Function) {
    let rowElement = document.createElement("tr");
    rowElement.setAttribute("data-id", data.id.toString());

    let cellElement = createDescriptionCellElement(data.name);
    rowElement.appendChild(cellElement);
    cellElement = createButtonCellElement(button1Text, button1Function);
    rowElement.appendChild(cellElement);
    cellElement = createButtonCellElement(button2Text, button2Function);
    rowElement.appendChild(cellElement);

    return rowElement;
}

/**
 * Creates a row of a table with 4 data cells. The first cell holds text,
 * the remaining three cells hold buttons.
 * Store the data id in the 'data-id' attribute of the row.
 *
 * Parameters.
 *
 * Data
 * Button 1 text
 * Button 1 function
 * Button 2 text
 * Button 2 function
 * Button 3 text
 * Button 3 function
 */
function createTextAnd3ButtonRowElement(data, button1Text, button1Function,
        button2Text, button2Function, button3Text, button3Function) {
    let rowElement = document.createElement("tr");
    rowElement.setAttribute("data-id", data.id.toString());

    let cellElement = createDescriptionCellElement(data.name);
    rowElement.appendChild(cellElement);
    cellElement = createButtonCellElement(button1Text, button1Function);
    rowElement.appendChild(cellElement);
    cellElement = createButtonCellElement(button2Text, button2Function);
    rowElement.appendChild(cellElement);
    cellElement = createButtonCellElement(button3Text, button3Function);
    rowElement.appendChild(cellElement);

    return rowElement;
}

/**
 * Creates a data cell for the text.
 */
function createDescriptionCellElement(text) {
    let cellElement = document.createElement("td");
    let textNode = document.createTextNode(text);
    cellElement.appendChild(textNode);
    return cellElement;
}

/**
 * Creates a data cell for a button.
 */
function createButtonCellElement(buttonText, buttonFunction) {
    let cellElement = document.createElement("td");

    let buttonElement = document.createElement("button");
    let textNode = document.createTextNode(buttonText);
    buttonElement.appendChild(textNode);
    buttonElement.setAttribute("onclick", buttonFunction);

    cellElement.appendChild(buttonElement);
    return cellElement;
}

/* Create Code Section. */

/**
 * Invoked by the create button on the main screen.
 * Open and initialise the dialog to create an item i.e. league/club/team.
 *
 * Parameters in event.data.
 *
 * dialogName - The dialog which is being opened.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 *                  This is required to clear the main maintenance message
 *                  on the previous dialog.
 */
function openCreateItemDialog(event) {
    let dialogName = event.data.dialogName;
    let baseDialogName = event.data.baseDialogName;
    clearMaintainMessage(baseDialogName);
    let dialog = openDialog(dialogName);
    dialog.getElementsByClassName('dialog-input')[0].value = '';
}

/**
 * Invoked by the create button on the create item dialog.
 * Read input from dialog. Display error if input is empty.
 * If input is valid, submit to server.
 *
 * Parameters in event.data.
 *
 * Dialog being processed, create league/create club/create team.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 * Item being created, league/club/team.
 * URL stem that identifies the URL to be used.
 * Form on dialog being processed.
 * Action taken 'created'/'updated'.
 * Function to be used to reload the items into the table.
 */
function createItemOnServer(event) {
    event.preventDefault();

    let dialog = event.data.dialog;
    let baseDialogName = event.data.baseDialogName;
    let inputField = dialog.getElementsByClassName('dialog-input')[0];
    let inputName = inputField.value;
    let item = event.data.item;
    if (validInput) {
        let url = event.data.urlStem;
        let type = 'POST';
        let form = event.data.form;
        let successFunction = serverSuccessCreateUpdate;
        let action = event.data.action;
        let reloadTableFunction = event.data.reloadTableFunction;
        submitToServer(url, type, dialog, baseDialogName, form, successFunction,
            inputName, item, action, reloadTableFunction);
    } else {
        isEmptyText(dialog, inputName, item);
        $(inputField).focus();
    }
}

/* Update Code Section. */

/**
 * Invoked by update button in table.
 * Update button is in second data cell of row.
 * Retrieve the item id and name from the table
 * and open the update dialog.
 *
 * Parameters
 *
 * buttonElement - Button element which invoked the action, defining row.
 * dialogName - The dialog which is being opened.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 *                  This is required to clear the main maintenance message
 *                  on the previous dialog.
 */
function updateItemName(buttonElement, dialogName, baseDialogName) {
    clearMaintainMessage(baseDialogName);
    let rowElement = buttonElement.parentNode.parentNode;
    let itemId = rowElement.getAttribute("data-id");
    // get name from first data cell in row.
    let updatedItemName = rowElement.firstChild.firstChild.nodeValue;
    openUpdateItemNameDialog(dialogName, itemId, updatedItemName);
}

/**
 * Open a dialog to update a item name.
 * Display the old item name and store its database id number.
 */
function openUpdateItemNameDialog(dialogName, itemId, oldItemName) {
    // access openDialog via leagueExports to allow 'mocha' to stub it out.
    let dialog = openDialog(dialogName);

    dialog.getElementsByClassName('dialog-input')[0].value = '';
    dialog.getElementsByClassName('dialog-old-name')[0].innerHTML = oldItemName;
    dialog.getElementsByClassName('dialog-object-id')[0].value = itemId;
}

/**
 * Invoked by the update button on the update dialog.
 * Check if new name is different to old name.
 * Also display error if input is empty.
 *
 * Parameters in event.data.
 *
 * Dialog being processed, update league/update club/update team.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 * Item being created, league/club/team.
 * URL stem that identifies the URL to be used.
 * Form on dialog being processed.
 * Action taken 'created'/'updated'.
 * Function to be used to reload the items into the table.
 */
function updateItemOnServer(event) {
    event.preventDefault();

    let dialog = event.data.dialog;
    let baseDialogName = event.data.baseDialogName;
    let inputField = dialog.getElementsByClassName('dialog-input')[0];
    let inputName = inputField.value;
    let item = event.data.item;
    if (validInput) {
        if (validateDifferentName(dialog, inputName)) {
            let url = event.data.urlStem;
            let type = 'PUT';
            let form = event.data.form;
            let successFunction = serverSuccessCreateUpdate;
            let action = event.data.action;
            let reloadTableFunction = event.data.reloadTableFunction;
            submitToServer(url, type, dialog, baseDialogName, form, successFunction,
                inputName, item, action, reloadTableFunction);
        }
    } else {
        isEmptyText(dialog, inputName, item);
        $(inputField).focus();
    }
}

/**
 * Success method used after a create request or an update request.
 * Reload the items from the server, close the dialog
 * and display a message on the main screen.
 * newScrollPosition is the new item name of the created or updated item.
 * Use this to position the new item name as the top row of the table.
 *
 * Parameters.
 *
 * Response from server.
 * Dialog being processed.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 *                  This is needed to display the maintenance message.
 * Scroll position to be used in table after a successful request.
 * URL to be used in the reload table function.
 * Item being processed, league/club/team.
 * Action taken 'created'/'updated'.
 * Function to be used to reload the items into the table.
 */
function serverSuccessCreateUpdate(response, dialog, baseDialogName, newScrollPosition,
    url, item, text, reloadTableFunction) {
    if (response.status) {
        reloadTableFunction(newScrollPosition, url, item);
        cancelDialog(dialog);
        displayMaintainMessage(baseDialogName, response.message);
    } else {
        displayErrorTop(dialog, response.message);
        let inputField = dialog.getElementsByClassName('dialog-input')[0];
        $(inputField).focus();
    }
}

/**
 * If name is empty, displays error message and returns false
 */
function isEmptyText(dialog, name, item) {
    let emptyText = false;
    let errorMessage = "Enter " + item + " Name.";

    if (name == "") {
        displayErrorTop(dialog, errorMessage);
        emptyText = true;
    }
    return emptyText;
}

/**
 * Allow user to change the case of the name, therefore use a
 * case-sensitive string comparison.
 */
function validateDifferentName(dialog, newName) {
    let validReturn = true;
    let errorMessage = 'New name is the same as old name.';
    let oldNameElement = dialog.getElementsByClassName('dialog-old-name')[0];
    let oldName = $(oldNameElement).text();

    if (newName.localeCompare(oldName) == 0) {
        displayErrorTop(dialog, errorMessage);
        let inputField = dialog.getElementsByClassName('dialog-input')[0];
        $(inputField).focus();
        validReturn = false;
    }
    return validReturn;
}

/**
 * Submits the form to the server, for a create or update request,
 * for a given url, HTTP method type and a form id.
 *
 * Parameters.
 *
 * URL stem that identifies the URL to be used.
 * Type of method to be used in request, e.g. GET/POST/PUT.
 * Dialog being processed.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 * Form on dialog being processed.
 * Function to be executed after a successful request.
 * Scroll position to be used in table after a successful request.
 * Item being processed, league/club/team.
 * Action taken 'created'/'updated'.
 * Function to be used to reload the items into the table.
 */
function submitToServer(url, type, dialog, baseDialogName, form, successFunction,
    newScrollPosition, item, text, reloadTableFunction) {

    $.ajax({
        cache: false,
        contentType: 'application/x-www-form-urlencoded',
        data: $(form).serialize(),
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    successFunction(result, dialog, baseDialogName, newScrollPosition,
                        url, item, text, reloadTableFunction);
                },
        type: type,
        url: url
    });
}

/* Delete Code Section. */

/**
 * Invoked by delete button in table.
 * Delete button is in third data cell of row.
 * Retrieve the item id and name from the table
 * and open the delete dialog.
 *
 * Parameters
 *
 * buttonElement - Button element which invoked the action, defining row.
 * dialogName - The dialog which is being opened.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 *                  This is required to clear the main maintenance message
 *                  on the previous dialog.
 */
function deleteItem(buttonElement, dialogName, baseDialogName) {
    clearMaintainMessage(baseDialogName);
    let rowElement = buttonElement.parentNode.parentNode;
    let itemId = rowElement.getAttribute("data-id");
    // get name from first data cell in row.
    let deletedItemName = rowElement.firstChild.firstChild.nodeValue;
    let newScrollPosition = getDeleteScrollPosition(rowElement);
    openDeleteItemDialog(dialogName, itemId, deletedItemName, newScrollPosition);
}

/**
 * Determind the position of the scrollbar in the table after a deletion.
 * Positioned the scrollbar at the item name before the deleted item.
 * If not possible, i.e. the deleted item was displayed first in the table,
 * use the item name after the deleted item. Note this might not be present,
 * as the deleted item could be the last item on the database.
 * Returns the name of the item, that will be displayed at the top of the table.
 * Will return 'undefined' if not set.
 *
 * Parameters.
 *
 * Element Row of deleted item.
 */
function getDeleteScrollPosition(deleteElementRow) {
    let newScrollPosition;
    let previousRowElement = deleteElementRow.previousElementSibling;
    let nextRowElement;

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
 * Open a dialog to delete a item name.
 * Display a confirmation message with the item name
 * Store its database id number and the new scroll position in a data attribute.
 */
function openDeleteItemDialog(dialogName, itemId, deleteItemName, newScrollPosition) {
    // access openDialog via itemExports to allow 'mocha' to stub it out.
    let dialog = openDialog(dialogName);

    let spanElement = dialog.getElementsByClassName('dialog-object-name')[0];
    spanElement.setAttribute('data-item-id', itemId);
    spanElement.setAttribute('data-scroll-position', newScrollPosition);
    spanElement.innerHTML = deleteItemName;

}

/**
 * Invoked by the delete button on the delete dialog.
 * Get the item id and the new scroll position from the span element
 * and send a delete request to the server.
 *
 * Parameters in event.data.
 *
 * Dialog being processed, delete league/delete club/delete team.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 * URL stem that identifies the URL to be used.
 * Item being created, league/club/team.
 * Action taken 'deleted'/'removed'.
 * Function to be used to reload the items into the table.
 */
function deleteItemOnServer(event) {
    let dialog = event.data.dialog;
    let baseDialogName = event.data.baseDialogName;
    let url = event.data.urlStem;
    let item = event.data.item;
    let action = event.data.action;
    let reloadTableFunction = event.data.reloadTableFunction;
    let spanElement = dialog.getElementsByClassName('dialog-object-name')[0];
    let deleteItemName = spanElement.innerHTML;
    let itemId = spanElement.getAttribute('data-item-id');
    let newScrollPosition = spanElement.getAttribute('data-scroll-position');

    $.ajax({
        async: false,
        cache: false,
        dataType: "json",
        error: serverFailure,
        success: function (result, status, xhr) {
                    serverSuccessDelete(result, dialog, baseDialogName, deleteItemName,
                        newScrollPosition, url, item, action, reloadTableFunction);
                },
        type: "DELETE",
        url: url + '/' + itemId
    });
}

/**
 * Reload the items from the server, close the dialog
 * and display a message on the main screen.
 *
 * Parameters.
 *
 * Response from server.
 * Dialog being processed.
 * baseDialogName - The dialog from which the above dialog is propagated from.
 *                  This is needed to display the maintenance message.
 * Name of deleted item.
 * Scroll position to be used in table after a successful request.
 * URL to be used in the reload table function.
 * Item being processed, league/club/team.
 * Action taken 'deleted'/'removed'.
 * Function to be used to reload the items into the table.
 */
function serverSuccessDelete(result, dialog, baseDialogName, deleteItemName,
    newScrollPosition, url, item, action, reloadTableFunction) {
    let message = deleteItemName + ' has been ' + action + '.'

    reloadTableFunction(newScrollPosition, url, item);
    cancelDialog(dialog);
    displayMaintainMessage(baseDialogName, message);
}

/* Maintain Team Code Section. */

/**
 * Invoked by team button in the leagues or clubs table.
 * Team button is in third data cell of row.
 * Retrieves the item id and name from the table.
 * Constructs the teams url.
 * Opens the teams dialog.
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
 * tbodyElementId - id of tbody element in the teams form,
 *                  used to clear the previous data in the table.
 * setUpMaintainTeamFunction - function to set up the maintain team form.
 */
function maintainTeams(buttonElement, url_start, baseDialogName, dialogName,
    tbodyElementId, setUpMaintainTeamFunction) {
    clearMaintainMessage(baseDialogName);
    clearMaintainMessage(dialogName);
    $('#' + tbodyElementId).empty();
    let rowElement = buttonElement.parentNode.parentNode;
    let itemId = rowElement.getAttribute("data-id");
    let url = url_start + '/' + itemId + '/' + URL_TEAM_END;
    // get name from first data cell in row.
    let itemName = rowElement.firstChild.firstChild.nodeValue;
    openMaintainTeamDialog(itemName, url, dialogName, setUpMaintainTeamFunction,
        itemId);
}

/**
 * Opens the team maintenance dialog.
 *
 * Parameters
 *
 * itemName - Name of club or league for which teams are being accessed.
 * teamUrl - URL used to retrive/update teams from server.
 * dialogName - The dialog which is being opened.
 * setUpMaintainTeamFunction - function to set up the maintain team form.
 * itemId - id of club or league for which teams are being accessed.
 */
function openMaintainTeamDialog(itemName, teamUrl, dialogName, setUpMaintainTeamFunction,
    itemId) {
    // access openDialog via leagueExports to allow 'mocha' to stub it out.
    let dialog = openDialog(dialogName);
    setUpMaintainTeamFunction(itemName, teamUrl, 'team', itemId);
}

/* Common Code Section. */

/**
 * Opens a new dialog.
 *
 * Removes the Escape key shortcut from the close button on the main page.
 * If this was not done, the Escape key would close the main page as well as
 * the dialog.
 *
 * Make sure that no element outside of the dialog
 * can be interacted with while the dialog is visible.
 */
function openDialog(dialogName) {
    disengagePreviousDialog();
    openedDialogs.push(dialogName);
    if (openedDialogs.length == 1) {
        document.removeEventListener('keydown', assignEscapeToCloseButton);
    }

    let dialog = document.getElementById(dialogName);

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

    // React to enter and escapes keys as mandated by ARIA Practices
    keyHandle = ally.when.key({
        escape: function(event, disengage) {
                    cancelDialog(dialog);
                }
    });

    // create or show the dialog
    dialog.hidden = false;
    initialiseDialog(dialog);

    // the dialog is visible on screen, so find the first
    // keyboard focusable element (giving any element with
    // autofocus attribute precendence). If the dialog does
    // not contain any keyboard focusabe elements, focus will
    // be given to the dialog itself.
    ally.when.visibleArea({
        context: dialog,
        callback: function(context) {
          var element = ally.query.firstTabbable({
            context: context, // context === dialog
            defaultToContext: true,
          });
          element.focus();
        },
    });

    // Make sure that no element outside of the dialog
    // is exposed via the Accessibility Tree, to prevent
    // screen readers from navigating to content it shouldn't
    // be seeing while the dialog is open. See example:
    // https://marcysutton.com/slides/mobile-a11y-seattlejs/#/36
    hiddenHandle = ally.maintain.hidden({
        filter: dialog,
    });

    return dialog;

}

/**
 * Call cancelDialog via the button click event.
 */
function cancelDialogViaButton(event) {
    let dialog = event.data.dialog;
    cancelDialog(dialog);
}

/**
 * Closes a dialog. Undo disabling elements outside of the dialog.
 * Engage the previous dialog, if there is one.
 *
 * Re-assigns the Escape key shortcut to the close button on the main page
 * if no dialogs are opened. A time interval is required. Otherwise the Escape
 * key that closes the dialog, will also close the main page.
 *
 * dialog - The dialog 'element' to be closed.
 */
function cancelDialog(dialog) {

    // undo disabling elements outside of the dialog
    disabledHandle.disengage();

    // undo trapping Tab key focus
    tabHandle.disengage();

    // undo hiding elements outside of the dialog
    hiddenHandle.disengage();

    // undo listening to keyboard
    keyHandle.disengage();

    dialog.hidden = true;

    openedDialogs.pop();
    let elementInterval = setInterval(function() {
        if (openedDialogs.length == 0) {
            document.addEventListener('keydown', assignEscapeToCloseButton);
        }
        clearInterval(elementInterval);
    }, 0);
    engagePreviousDialog();

}

/**
 * A new dialog has been opened. Disengage the previous dialog if there is one.
 * Therefore when the new dialog is opened, the user cannot access the previous
 * dialog.
 */
function disengagePreviousDialog() {

    if (openedDialogs.length > 0) {

        // undo disabling elements outside of the dialog
        disabledHandle.disengage();

        // undo trapping Tab key focus
        tabHandle.disengage();

        // undo hiding elements outside of the dialog
        hiddenHandle.disengage();

        // undo listening to keyboard
        keyHandle.disengage();

    }
}

/**
 * A dialog has just been closed. Engage the previous dialog if there is one.
 * i.e. the user can only interact with the previous dialog and cannot access
 * an element outside it.
 * openedDialogs contains a list of the opened dialogs.
 */
function engagePreviousDialog() {
    let length = openedDialogs.length;

    if (length > 0) {
        engageDialog(openedDialogs[length - 1]);
    }
}

function engageDialog(dialogName) {
    let dialog = document.getElementById(dialogName);

    disabledHandle = ally.maintain.disabled({
        filter: dialog
    });

    tabHandle = ally.maintain.tabFocus({
        context: dialog,
    });

    ally.when.visibleArea({
        context: dialog,
        callback: function(context) {
          var element = ally.query.firstTabbable({
            context: context, // context === dialog
            defaultToContext: true,
          });
          element.focus();
        },
    });

    keyHandle = ally.when.key({
        escape: function(event, disengage) {
                    cancelDialog(dialog);
                }
    });

    hiddenHandle = ally.maintain.hidden({
        filter: dialog,
    });

}

/* Error Display Code Section. */

/**
 * Initialises boolean fields and error messages
 */
function initialiseDialog(containingElement) {
    validInput = false;

    let errorMessage1 = containingElement.getElementsByClassName('dialog-message-1');
    if (errorMessage1.length !== 0) {
        errorMessage1[0].innerHTML = '';
        containingElement.getElementsByClassName('dialog-message-2')[0].innerHTML = '';
        containingElement.getElementsByClassName('dialog-message-3')[0].innerHTML = '';
    }
}

/**
 * displays an error message in the next available error position on the dialog.
 */
function displayError(dialog, errorMessage) {
    let dialogMessageElement
        = dialog.getElementsByClassName('dialog-message-1')[0];
    if (dialogMessageElement.innerHTML == "") {
        dialogMessageElement.innerHTML = errorMessage;
        dialogMessageElement.style.color = "red";
    } else {
        dialogMessageElement
            = dialog.getElementsByClassName('dialog-message-2')[0];
        if (dialogMessageElement.innerHTML == "") {
            dialogMessageElement.innerHTML = errorMessage;
            dialogMessageElement.style.color = "red";
        } else {
            dialogMessageElement
                = dialog.getElementsByClassName('dialog-message-3')[0];
            dialogMessageElement.innerHTML = errorMessage;
            dialogMessageElement.style.color = "red";
        }
    }
}

/**
 * displays an error message in the top error position on the dialog.
 * this prevents error messages being repeated.
 */
function displayErrorTop(dialog, errorMessage) {
    let dialogMessageElement = dialog.getElementsByClassName('dialog-message-1')[0];
    dialogMessageElement.innerHTML = errorMessage;
    dialogMessageElement.style.color = "red";
}

/**
 * Clears the message on the maintainance screen.
 */
function clearMaintainMessage(dialogName) {
    let dialog = document.getElementById(dialogName);
    let maintainMessageElement = dialog.getElementsByClassName('maintain-message')[0];
    $(maintainMessageElement).text('');
}


/**
 * Displays a message on the maintainance screen.
 */
function displayMaintainMessage(dialogName, message) {
    let dialog = document.getElementById(dialogName);
    let maintainMessageElement = dialog.getElementsByClassName('maintain-message')[0];
    $(maintainMessageElement).text(message).css("color", "green").show();
}

function findMaintainMessageElement() {
}

/**
 * Sets up the cancel event on a given dialog.
 * Attaches the cancelDialogViaButton function to the cancel button.
 *
 * dialogName - Name of dialog to set cancel event on.
 */
function setUpCancelEvent(dialogName) { 
    let dialog = document.getElementById(dialogName);
    let cancelButtonElement = dialog.getElementsByClassName('dialog-cancel-button')[0];
    let parameters = {'dialog' : dialog};
    $(cancelButtonElement).click(parameters, cancelDialogViaButton);
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
 * Assigns the Escape key to the Close button on the main Maintenance
 * web page.
 * A time interval has to be set. This avoids getting an NS_BINDING_ABORTED error
 * on FireFox.
 */
function assignEscapeToCloseButton(event) {
    let elementInterval = setInterval(function() {
        if (event.key == "Escape") {
            gotoAdminLoginPage(event);
        }
        clearInterval(elementInterval);
    }, 0);
}

/**
 * Takes user back to Admin Login Page.
 */
function gotoAdminLoginPage(event) {
    document.location = 'adminloginpage';
}

