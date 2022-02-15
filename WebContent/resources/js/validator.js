"use strict";

var leagueExports = require('../js/league.js')
var initialiseDialog = leagueExports.initialiseDialog;

/**
 * Holds the current status of the user input.
 */
var validInput = false;

/* Validation Input Field Code Section. */

/**
 * Bound to the oninput event on the input field on each dialog.
 * Ignores empty input, i.e. the empty string
 */
function validateInput(event){
    let dialog = event.data.dialog;
    let inputElement = event.data.inputElement;
    let name = $(inputElement).val();
    initialiseDialog(dialog);
    if (name != "") {
        validInput = validateName(dialog, name);
    }
}

/**
 * Validates the length of the name and its characters
 */
function validateName(dialog, name) {
    var validReturn = true;

    if (name.length > 30) {
        displayLengthError(dialog, name)
        validReturn = false;
    }

    if (!validateFirstChar(dialog, name)) {
        validReturn = false;
    }

    if (!validateAllChars(dialog, name)) {
        validReturn = false;
    }
    return validReturn;
}

/**
 * Display a length error message
 */
function displayLengthError(dialog, name) {
    var errorMessage = "Can only be 30 characters or less," +
        " input is " + name.length + ".";
    displayError(dialog, errorMessage);
}

/**
 * Validates the first character of a name
 * The first character must be an uppercase letter
 * If it is not an uppercase letter, an error is displayed
 */
function validateFirstChar(dialog, name) {
    var errorMessage = "Must start with a capital letter.";
    var firstChar = name.substr(0, 1);
    var index = firstChar.search(/[A-Z]/);
    var validReturn = true;

    if (index != 0) {
        displayError(dialog, errorMessage);
        validReturn = false;
    }
    return validReturn;
}
    
/**
 * Validates all the characters of a name, even the first character.
 * If the first character is invalid, two error messages are displayed
 * Valid characters are upper/lower case letters, numbers, spaces,
 * apostrophes, brackets and dashes.
 */
function validateAllChars(dialog, name) {
    var errorMessage = "Only letters, numbers, spaces, apostrophes, brackets and dashes allowed.";
    var index = name.search(/[^a-zA-Z0-9 '()-]/);
    var validReturn = true;

    if (index != -1) {
        displayError(dialog, errorMessage);
        validReturn = false;
    }
    return validReturn;
}

/**
 * Object to expose variables/functions to test service.
 */
var validatorExports = {};

validatorExports.validateInput = validateInput;
module.exports = validatorExports;
