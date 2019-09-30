/**
 * Holds the current status of the input,
 * in case the user enters the league name to be processed
 */
var validLeague = false;

/**
 * Binds the processForm function to the submit event on the form
 * Focuses the insertion point on the league input field
 */
function setUpLeagueDocument(){
    $("#formLeague").submit(processForm);
    $("#inputLeague").focus();
}

/**
 * Function is bound to the submit event on the form
 * If input has been entered, submit it to the server,
 * else if input is empty display error message
 */
function processForm(event) {
    event.preventDefault();

    var errorMessage = "Enter League Name.";
    var inputLeague = $("#inputLeague").val();

    if (inputLeague == "") {
        $("#errorMessage1").text(errorMessage).css("color", "red").show();
    } else if (validLeague) {
        submitForm();
    }
	$("#inputLeague").focus();
}

/**
 * Submits the form to the server
 * On success calls the serverSuccess function
 * On error displays the XmlHttpResponse on the console log
 */
function submitForm() {
    $.ajax({
        url: 'rest/LeagueService/leagues',
        type: "POST",
        contentType: 'application/x-www-form-urlencoded',
        data: $("#formLeague").serialize(),
        cache: false,
        dataType: "json",
        success: serverSuccess,
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( textStatus );
            console.log( jqXhr );
            console.log( jqXhr.status );
            console.log( jqXhr.statusText );
            console.log(jqXhr.responseText);
            }
    });
}

/**
 * Bound to the oninput event on the league input field
 * Ignores empty input, i.e. the empty string
 */
function validateLeagueInput(league){
    initialiseLeagueJS();
    if (league != "") {
        validLeague = validateLeagueName(league);
    }
}

/**
 * Initialises boolean fields and error messages
 */
function initialiseLeagueJS() {
    validLeague = false;
    $("#errorMessage1").text("");
    $("#errorMessage2").text("");
    $("#errorMessage3").text("");
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
 * Displays an error message in the next available position on the form
 */
function displayError(errorMessage) {
    if ($("#errorMessage1").text() == "") {
        $("#errorMessage1").text(errorMessage).css("color", "red").show();
    } else if ($("#errorMessage2").text() == "") {
        $("#errorMessage2").text(errorMessage).css("color", "red").show();
    } else {
        $("#errorMessage3").text(errorMessage).css("color", "red").show();
    }
}
/**
 * Bound to a server successful response
 * Display the response message, according to the status
 */
function serverSuccess(response, textStatus, jQxhr){
    if (response.status) {
        $("#errorMessage1").text(response.message).css("color", "green").show();
        $("#inputLeague").val("");
    } else {
        $("#errorMessage1").text(response.message).css("color", "red").show();
    }
}

/**
 * Object to expose functions to test service.
 */
var testFunctions = {};

testFunctions.validateLeagueName = validateLeagueName;
testFunctions.displayError = displayError;
testFunctions.submitForm = submitForm;
module.exports = testFunctions;
