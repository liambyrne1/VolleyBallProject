"use strict";

var leagueExports = require('../js/league.js')
var initialiseDialog = leagueExports.initialiseDialog;

/**
 * Holds the current status of the user input.
 */
var validInput = false;

/* Validation Input Field Code Section. */

/**
 * Bound to the oninput event on the league input field on each dialog.
 * Ignores empty input, i.e. the empty string
 */
function validateLeagueInput(league){
    initialiseDialog();
    if (league != "") {
        validInput = validateLeagueName(league);
    }
}

/**
 * Validates the length of the league name and its characters
 */
function validateLeagueName(league) {
    var validLeagueReturn = true;

    if (league.length > 30) {
        displayLeagueLengthError(league)
        validLeagueReturn = false;
    }

    if (!validateLeagueFirstChar(league)) {
        validLeagueReturn = false;
    }

    if (!validateLeagueAllChars(league)) {
        validLeagueReturn = false;
    }
    return validLeagueReturn;
}

/**
 * Display a league length error message
 */
function displayLeagueLengthError(league) {
    var errorMessage = "League can only be 30 characters or less," +
        " input is " + league.length + ".";
    displayError(errorMessage);
}

/**
 * Validates the first character of a league name
 * The first character must be an uppercase letter
 * If it is not an uppercase letter, an error is displayed
 */
function validateLeagueFirstChar(league) {
    var errorMessage = "League must start with a capital letter.";
    var firstChar = league.substr(0, 1);
    var index = firstChar.search(/[A-Z]/);
    var validLeagueReturn = true;

    if (index != 0) {
        displayError(errorMessage);
        validLeagueReturn = false;
    }
    return validLeagueReturn;
}
    
/**
 * Validates all the characters of a league name, even the first character.
 * If the first character is invalid, two error messages are displayed
 * Valid characters are upper/lower case letters, numbers, spaces,
 * apostrophes, brackets and dashes.
 */
function validateLeagueAllChars(league) {
    var errorMessage = "Only letters, numbers, spaces, apostrophes, brackets and dashes allowed.";
    var index = league.search(/[^a-zA-Z0-9 '()-]/);
    var validLeagueReturn = true;

    if (index != -1) {
        displayError(errorMessage);
        validLeagueReturn = false;
    }
    return validLeagueReturn;
}

/**
 * Object to expose variables/functions to test service.
 */
var validatorExports = {};

validatorExports.validateLeagueInput = validateLeagueInput;
module.exports = validatorExports;
