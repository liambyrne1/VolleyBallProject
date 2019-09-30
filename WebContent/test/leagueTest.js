/**
 * Mocha test suite to test the league service.
 */
var assert = require('assert');
var expect = require('chai').expect;
var jsdom = require('jsdom-global')();
var fs = require('fs');
global.window = window
global.$ = require('jquery');
 
/**
 * Read in the 'createleague' HTML and set it to the jsdom.
 */
const path = require("path");
const createLeagueHtmlFile = fs.readFileSync(path.resolve(__dirname, "../createleague.html"));
document.body.innerHTML = createLeagueHtmlFile;
 
/**
 * Load in the functions to be tested.
 */
var testFunctions = require('../js/league.js')
var validateLeagueName = testFunctions.validateLeagueName;
var displayError = testFunctions.displayError;
var submitForm = testFunctions.submitForm;

function initialiseErrorFields() {
    $("#errorMessage1").text("");
    $("#errorMessage2").text("");
    $("#errorMessage3").text("");
};
 
describe('validateLeagueName', function() {
    beforeEach(function() {
        initialiseErrorFields();
    });

    describe('Successful validation', function() {
        it('should return true given all the valid characters', function() {
            var leagueName = "Men's Division 02 - (North)";
            expect(validateLeagueName(leagueName)).to.equal(true);
        });
    });

    describe('Failed validation', function() {
        it('should return false given name too long', function() {
            var leagueName = "Men's Division 06 - (North - South)";
            var errorMessage = "League can only be 30 characters or less," +
                " input is 35.";
            expect(validateLeagueName(leagueName)).to.equal(false);
            expect($("#errorMessage1").text()).to.equal(errorMessage);
        });

        it('should return false given name does not begin with an uppercase', function() {
            var leagueName = "men's Division 07";
            var errorMessage = "League must start with a capital letter.";
            expect(validateLeagueName(leagueName)).to.equal(false);
            expect($("#errorMessage1").text()).to.equal(errorMessage);
        });

        it('should return false given an invalid character.', function() {
            var leagueName = "Men's Division 04; - (North)";
            var errorMessage = "Only letters, numbers, spaces, apostrophes, " +
                "brackets and dashes allowed.";
            expect(validateLeagueName(leagueName)).to.equal(false);
            expect($("#errorMessage1").text()).to.equal(errorMessage);
        });
    });
});

describe('displayError', function() {
    it('should display errors in the correct fields', function() {
            initialiseErrorFields();
            var errorMessage1 = "League can only be 30 characters or less," +
                " input is 35.";
            var errorMessage2 = "League must start with a capital letter.";
            var errorMessage3 = "Only letters, numbers, spaces, apostrophes, " +
                "brackets and dashes allowed.";

            displayError(errorMessage1);
            displayError(errorMessage2);
            displayError(errorMessage3);

            expect($("#errorMessage1").text()).to.equal(errorMessage1);
            expect($("#errorMessage2").text()).to.equal(errorMessage2);
            expect($("#errorMessage3").text()).to.equal(errorMessage3);
    });
});
