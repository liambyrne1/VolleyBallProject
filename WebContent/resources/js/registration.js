"use strict";

/**
 * Registration form input fields.
 */
const FIRST_NAME = 'first-name';
const LAST_NAME = 'last-name';
const EMAIL = 'email';
const PASSWORD = 'password';
const MATCHING_PASSWORD = 'matching-password';

/**
 * Registration form error fields.
 */
const FIRST_NAME_ERROR = 'first-name-error';
const LAST_NAME_ERROR = 'last-name-error';
const EMAIL_ERROR = 'email-error';
const PASSWORD_ERROR = 'password-error';
const MATCHING_PASSWORD_ERROR = 'matching-password-error';

/**
 * Registration form error messages.
 */
const FIRST_NAME_REQUIRED_MESSAGE = 'Please enter first name!';
const LAST_NAME_REQUIRED_MESSAGE = 'Please enter last name!';
const EMAIL_REQUIRED_MESSAGE = 'Please enter email!';
const PASSWORD_REQUIRED_MESSAGE = 'Please enter password!';
const MATCHING_PASSWORD_REQUIRED_MESSAGE = 'Please enter password confirmation!';

const FIRST_NAME_ALPHA_MESSAGE = 'Only letters!';
const FIRST_NAME_LENGTH_MESSAGE = 'Only 30 characters,';

const LAST_NAME_ALPHA_MESSAGE = 'Only letters!';
const LAST_NAME_LENGTH_MESSAGE = 'Only 30 characters,';

const EMAIL_MESSAGE = 'Your email is invalid!';

const PASSWORD_SHORT_MESSAGE = 'Password must be 8 characters or more,';
const PASSWORD_LONG_MESSAGE = 'Password must be 25 characters or less,';

const MATCHING_PASSWORD_MESSAGE = 'Confirmation must match password!';

/**
 * Span elements that make up each error line.
 */
const ERROR_MESSAGE_1 = 'error-message-1';
const ERROR_MESSAGE_2 = 'error-message-2';

/**
 * Maximum length for a first name or a last name.
 */
const MAXIMUM_NAME_LENGTH = 30;

/**
 * Minimum length for a password.
 */
const MINIMUM_PASSWORD_LENGTH = 8;

/**
 * Maximum length for a password.
 */
const MAXIMUM_PASSWORD_LENGTH = 25;

/**
 * Regular expression to test email.
 */
const EMAIL_PATTERN =
    /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;

/**
 * Main error message field.
 */
const MAIN_ERROR_MESSAGE_FIELD = 'main-error-message';

/**
 * Main error message.
 */
const MAIN_ERROR_MESSAGE = 'Please fix errors!';

/**
 * Indicates valid input for each field.
 */
let validFirstName = true;
let validLastName = true;
let validEmail = true;
let validPasswordNotShort = true;
let validPasswordNotLong = true;

function setUpRegistrationDocument() {
    document.getElementById(FIRST_NAME).focus();

    setUpFieldEvents();
    let dialog = document.getElementById('dialog-registration');
    let dialogFormElement = dialog.getElementsByClassName('dialog-content')[0];
    $(dialogFormElement).off('submit');
    let submitparams = {'form' : dialogFormElement};
    $(dialogFormElement).submit(submitparams, serverFunction);

}

/**
 * Sets up the events on the fields.
 */
function setUpFieldEvents() {
    let fieldElement = document.getElementById(FIRST_NAME);
    let parameters = {'validationFunction' : validateFirstName};
    $(fieldElement).on('input', parameters, validate);

    fieldElement = document.getElementById(LAST_NAME);
    parameters = {'validationFunction' : validateLastName};
    $(fieldElement).on('input', parameters, validate);

    fieldElement = document.getElementById(EMAIL);
    parameters = {'validationFunction' : clearEmailError};
    $(fieldElement).on('input', parameters, validate);

    parameters = {'validationFunction' : validateEmail};
    $(fieldElement).on('focusout', parameters, validate);

    fieldElement = document.getElementById(PASSWORD);
    parameters = {'validationFunction' : validatePasswordLong};
    $(fieldElement).on('input', parameters, validate);

    parameters = {'validationFunction' : validatePasswordShort};
    $(fieldElement).on('focusout', parameters, validate);

    fieldElement = document.getElementById(MATCHING_PASSWORD);
    parameters = {'validationFunction' : validateMatchingPassword};
    $(fieldElement).on('input', parameters, validate);

}

/**
 * Calls a different validation function on each field.
 */
function validate(event) {
    let validationFunction = event.data.validationFunction;
    validationFunction();
}

/**
 * Validates the first name.
 */
function validateFirstName() {
    validFirstName = validateName(FIRST_NAME, FIRST_NAME_ERROR,
        FIRST_NAME_ALPHA_MESSAGE, FIRST_NAME_LENGTH_MESSAGE);
}

/**
 * Validates the last name.
 */
function validateLastName() {
    validLastName = validateName(LAST_NAME, LAST_NAME_ERROR,
        LAST_NAME_ALPHA_MESSAGE, LAST_NAME_LENGTH_MESSAGE);
}

/**
 * Returns false if the given name is invalid and displays an error message.
 * Can display up to two error messages.
 * If the name is valid, uppercase the first character and lowercase the
 * remaining characters.
 *
 * User may leave field blank, but has to return to it, before submiting.
 *
 * nameField - Field of name to validate.
 * errorField - Field at which to display error.
 * alphaMessage - Error message if name contains non alpha characters.
 * lengthMessage - Error message if name is to long.
 */
function validateName(nameField, errorField, alphaMessage, lengthMessage) {
    let returnFlag = true;
    clearError(errorField);
    clearMainErrorMessage();
    let inputElement = document.getElementById(nameField);
    let name = inputElement.value;

    if (name != '') {
        let index = name.search(/[^a-zA-Z]/);
        if (index != -1) {
            displayError(errorField, alphaMessage);
            returnFlag = false;
        }

        if (name.length > MAXIMUM_NAME_LENGTH) {
            let errorMessage = lengthMessage + ' input is ' + name.length + '!';
            displayError(errorField, errorMessage);
            returnFlag = false;
        }

        if (returnFlag) {
            let firstChar = name.substr(0, 1);
            let remainingChars = name.substr(1);
            inputElement.value = firstChar.toUpperCase() + remainingChars.toLowerCase();
        }
    }

    return returnFlag;
}

/**
 * Clears the email error. Activated when the user inputs a character
 * on the email field, in case the email error had been already displayed.
 *
 * This function will not be executed if the user does not input a character,
 * i.e. clicks on the field, and then clicks on the submit button. This is why,
 * the validateEmail function also has to clear the error field.
 */
function clearEmailError() {
    clearError(EMAIL_ERROR);
    clearMainErrorMessage();
}

/**
 * Validates the email. Activated
 * when the user leaves the email field.
 *
 * Clears the error field, in case the error message has already been displayed.
 *
 * It would be incorrect to clear the main error message in this function. The
 * user could click this field, and then click on another field without changing any
 * data. Also if the user was on this field and pressed the 'Enter' key to submit
 * form, if there was errors on the form, this function would clear the main
 * error message.
 */
function validateEmail() {
    validEmail = true;
    let email = document.getElementById(EMAIL).value;

    if (email != '') {
        clearError(EMAIL_ERROR);
        if (email.match(EMAIL_PATTERN) == null) {
            displayError(EMAIL_ERROR, EMAIL_MESSAGE);
            validEmail = false;
        }
    }
}

/**
 * Checks if the password is to long. Activated when
 * the user inputs a character on this field.
 *
 * Checks if the user changes the password so that it matches the confirmation.
 * If this does happen, remove any error message at the confirmation field.
 */
function validatePasswordLong() {
    validPasswordNotLong = true;
    clearError(PASSWORD_ERROR);
    clearMainErrorMessage();
    let password = document.getElementById(PASSWORD).value;

    if (password.length > MAXIMUM_PASSWORD_LENGTH) {
        let errorMessage = PASSWORD_LONG_MESSAGE + ' input is ' + password.length + '!';
        displayError(PASSWORD_ERROR, errorMessage);
        validPasswordNotLong = false;
    } else {
        let matchingPassword = document.getElementById(MATCHING_PASSWORD).value;

        if (password.localeCompare(matchingPassword) == 0) {
            clearError(MATCHING_PASSWORD_ERROR);
            clearMainErrorMessage();
        }
    }
}

/**
 * Checks if the password is to short. Activated
 * when the user leaves the password field.
 */
function validatePasswordShort() {
    validPasswordNotShort = true;
    let password = document.getElementById(PASSWORD).value;
    let passwordLength = password.length;

    if (passwordLength > 0 && passwordLength < MINIMUM_PASSWORD_LENGTH) {
        clearError(PASSWORD_ERROR);
        clearMainErrorMessage();
        let errorMessage = PASSWORD_SHORT_MESSAGE + ' input is ' + password.length + '!';
        displayError(PASSWORD_ERROR, errorMessage);
        validPasswordNotShort = false;
    }
}

/**
 * Checks the confirmation of the password. Activated when
 * the user inputs a character on this field.
 */
function validateMatchingPassword() {
    clearError(MATCHING_PASSWORD_ERROR);
    clearMainErrorMessage();
    let matchingPassword = document.getElementById(MATCHING_PASSWORD).value;

    if (matchingPassword != '') {
        let password = document.getElementById(PASSWORD).value;
        let segmentPassword = password.substr(0, matchingPassword.length);

        if (segmentPassword.localeCompare(matchingPassword) != 0) {
            displayError(MATCHING_PASSWORD_ERROR, MATCHING_PASSWORD_MESSAGE);
        }
    }
}

/**
 * Checks all fields are present and valid. Also checks the password
 * is the same as the confirmation. If everything is all right, sends the request
 * to the server.
 * This function is activated by the submit button on the form.
 *
 * Call validateEmail() and validatePasswordShort(). These functions get activated
 * when the user leaves a input field, i.e.  they are 'focusout' events. If the user
 * presses the 'Enter' button on one of these fields, the form is submitted before
 * these events have time to run, hence sending invalid data to the server.
 */
function serverFunction(event) {
    event.preventDefault();

    validateEmail();
    validatePasswordShort();

    let validFields = validFirstName && validLastName && validEmail &&
        validPasswordNotShort && validPasswordNotLong;
    let validConfirmation = validPasswordConfirmation();

    if (allFieldsPresent() && validFields && validConfirmation) {
        sendRequestToServer(event);
    }
    else
    {
        displayMainErrorMessage();
        focusOnEarliestError();
    }
}

/**
 * Checks the password is the same as the confirmation, just before the request is
 * sent to the server. The user can change the password after entering the
 * confirmation.
 * Clears the error message, in case it has already been shown.
 *
 * Does not perform check if confirmation field is empty. Allows system to generate the
 * 'Please enter password confirmation!' message.
 */
function validPasswordConfirmation() {
    let returnFlag = true;

    let password = document.getElementById(PASSWORD).value;
    let matchingPassword = document.getElementById(MATCHING_PASSWORD).value;

    if (matchingPassword != '') {
        if (password.localeCompare(matchingPassword) != 0) {
            clearError(MATCHING_PASSWORD_ERROR);
            clearMainErrorMessage();
            displayError(MATCHING_PASSWORD_ERROR, MATCHING_PASSWORD_MESSAGE);
            returnFlag = false;
        }
    }

    return returnFlag;
}

/**
 * Focuses on the field, highest up the page, that has an error.
 */
function focusOnEarliestError() {
    if (errorFieldIsSet(FIRST_NAME_ERROR)) {
       setFocusAtEndOfField(FIRST_NAME);  
    }
    else if (errorFieldIsSet(LAST_NAME_ERROR)) {
       setFocusAtEndOfField(LAST_NAME);  
    }
    else if (errorFieldIsSet(EMAIL_ERROR)) {
       setFocusAtEndOfField(EMAIL);  
    }
    else if (errorFieldIsSet(PASSWORD_ERROR)) {
       setFocusAtEndOfField(PASSWORD);  
    }
    else {
       setFocusAtEndOfField(MATCHING_PASSWORD);  
    }
}

/**
 * Returns true if the given error field is set. Need only test the first span
 * element of the error field.
 *
 * errorField - Error field to be tested.
 */
function errorFieldIsSet(errorField) {
    let cellElement = document.getElementById(errorField);
    let firstSpanElement = cellElement.getElementsByClassName(ERROR_MESSAGE_1)[0];

    return firstSpanElement.innerHTML != '';
}

/**
 * Sets focus at the end of the given field.
 *
 * field - Field to be focused on.
 */
function setFocusAtEndOfField(field) {
    let fieldElement = document.getElementById(field);
    let end = fieldElement.value.length;

    fieldElement.setSelectionRange(end, end);
    fieldElement.focus();
}

/**
 * Sends a registration request to the server.
 */
function sendRequestToServer(event) {
    let form = event.data.form;
    $.ajax({
        async: false,
        cache: false,
        data: $(form).serialize(),
        dataType: "html",
        error: serverFailure,
        success: function (result, status, xhr) {
                    successFunction(result);
                },
        type: 'POST',
        url: '/VolleyBallLeagueSystem/registration'
    });
}

/**
 * Returns true if all fields are present. Otherwise returns false,
 * and displays an error message at fields that are not present.
 */
function allFieldsPresent() {
    let firstNameIsPresent = isFieldPresent(FIRST_NAME, FIRST_NAME_ERROR,
        FIRST_NAME_REQUIRED_MESSAGE);

    let lastNameIsPresent = isFieldPresent(LAST_NAME, LAST_NAME_ERROR,
        LAST_NAME_REQUIRED_MESSAGE);

    let emailIsPresent = isFieldPresent(EMAIL, EMAIL_ERROR,
        EMAIL_REQUIRED_MESSAGE);

    let passwordIsPresent = isFieldPresent(PASSWORD, PASSWORD_ERROR,
        PASSWORD_REQUIRED_MESSAGE);

    let matchingPasswordIsPresent = isFieldPresent(MATCHING_PASSWORD,
        MATCHING_PASSWORD_ERROR, MATCHING_PASSWORD_REQUIRED_MESSAGE);

    let returnFlag = firstNameIsPresent && lastNameIsPresent && emailIsPresent &&
        passwordIsPresent && matchingPasswordIsPresent;

    return returnFlag;
}

/**
 * Returns false if given field name is empty, and displays the given error message
 * in the given error field.
 *
 * fieldName - Field name to be tested.
 * errorField - Error field to display error message at.
 * errorMessage - Error message to be displayed.
 */
function isFieldPresent(fieldName, errorField, errorMessage) {
    let returnFlag = true;

    if (document.getElementById(fieldName).value == '') {
        displayRequiredError(errorField, errorMessage);
        returnFlag = false;
    }

    return returnFlag;
}

/**
 * Displays a 'Please enter ...' message after the user submits an empty field.
 * The user could have already submitted the same empty message, in which case
 * the error message should not be displayed again. Therefore test whether the
 * span element of the error field has been filled or not.
 */
function displayRequiredError(errorField, errorMessage) {
    let cellElement = document.getElementById(errorField);
    let firstSpanElement = cellElement.getElementsByClassName(ERROR_MESSAGE_1)[0];

    if (firstSpanElement.innerHTML == '') {
        firstSpanElement.innerHTML = errorMessage;
    }
}

/**
 * Displays the given error message in the given error field.
 * The error field is made up of two span elements.
 * If the first span element is empty, display the message in this element.
 * If the first span element has been taken, display the message in the second
 * element, with a preceding space.
 *
 * errorField - Error field to display error message at.
 * errorMessage - Error message to be displayed.
 */
function displayError(errorField, errorMessage) {
    let cellElement = document.getElementById(errorField);
    let firstSpanElement = cellElement.getElementsByClassName(ERROR_MESSAGE_1)[0];

    if (firstSpanElement.innerHTML == '') {
        firstSpanElement.innerHTML = errorMessage;
    }
    else {
        let secondSpanElement = cellElement.getElementsByClassName(ERROR_MESSAGE_2)[0];
        secondSpanElement.innerHTML = ' ' + errorMessage;
    }
}

/**
 * Clears a given error field.
 *
 * errorField - Error field to be cleared.
 */
function clearError(errorField) {
    let cellElement = document.getElementById(errorField);
    let firstSpanElement = cellElement.getElementsByClassName(ERROR_MESSAGE_1)[0];
    firstSpanElement.innerHTML = '';
    let secondSpanElement = cellElement.getElementsByClassName(ERROR_MESSAGE_2)[0];
    secondSpanElement.innerHTML = ' ';
}

/**
 * Displays the main error message.
 */
function displayMainErrorMessage() {
    let fieldElement = document.getElementById(MAIN_ERROR_MESSAGE_FIELD);
    fieldElement.innerHTML = MAIN_ERROR_MESSAGE;
}

/**
 * Clears the main error message.
 */
function clearMainErrorMessage() {
    let fieldElement = document.getElementById(MAIN_ERROR_MESSAGE_FIELD);
    fieldElement.innerHTML = '';
}

function successFunction(newPage) {
    document.open();
    document.write(newPage);
    document.close();
}

function serverFailure(jqXhr, textStatus, errorThrown){
    console.log( 'textStatus = ' + textStatus );
    console.log( 'jqXhr = ' + jqXhr );
    console.log( 'jqXhr.status = ' + jqXhr.status );
    console.log( 'jqXhr.statusText = ' + jqXhr.statusText );
    console.log('jqXhr.responseText = ' + jqXhr.responseText );
}

