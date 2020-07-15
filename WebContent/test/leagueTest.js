/**
 * Mocha test suite to test the league service.
 */
var assert = require('assert');
var sinon = require('sinon');
var expect = require('chai').expect;
const { JSDOM } = require('jsdom');

/**
 * Load in the functions to be tested.
 */
var validatorExports = require('../js/validator.js')
var validateLeagueInput = validatorExports.validateLeagueInput;

var leagueExports = require('../js/league.js')
var setUpDialogButtons = leagueExports.setUpDialogButtons;
var setUpDeleteDialogButtons = leagueExports.setUpDeleteDialogButtons;
var displayError = leagueExports.displayError;
var openCreateLeagueDialog = leagueExports.openCreateLeagueDialog;
var createLeagueOnServer = leagueExports.createLeagueOnServer;
var getLeaguesFromServer = leagueExports.getLeaguesFromServer;
var displayLeaguesInTable = leagueExports.displayLeaguesInTable;
var displayLeagues = leagueExports.displayLeagues;
var updateLeague = leagueExports.updateLeague;
var updateLeagueOnServer = leagueExports.updateLeagueOnServer;
var deleteLeague = leagueExports.deleteLeague;

global.validInput = true;

describe('Create Operation - createLeagueOnServer', function() {
    let actualValues = [{'id':12,'name':'Division 01'},
                        {'id':24,'name':'Division 03'},
                        {'id':36,'name':'Division 05'},
                        {'id':48,'name':'Division 07'},
                        {'id':60,'name':'Division 09'},
                        {'id':72,'name':'Division 02'},
                        {'id':84,'name':'Division 04'},
                        {'id':96,'name':'Division 06'},
                        {'id':108,'name':'Division 08'},
                        {'id':120,'name':'Division 10'}];

    before(function(done) {
        JSDOM.fromFile('maintainleague.html')
            .then((dom) => {
                window = dom.window;
                document = window.document;
                $ = require('jquery');
            })
            .then(done, done);
    });

    it('should create league on system', function() {

        sinon.stub(leagueExports, 'getLeaguesFromServer').callsFake(function fakeFn() {
            displayLeaguesInTable(actualValues);
        });

        // load league table from actualValues by the above stub.
        leagueExports.getLeaguesFromServer();

        // stub out openDialog to avoid calling ally.js code.
        // openDialog is called by openCreateLeagueDialog.
        sinon.stub(leagueExports, 'openDialog');

        // user clicks on create button. call openCreateLeagueDialog().
        // this opens the create league dialog.
        openCreateLeagueDialog();

        // assert input field is empty.
        let dialogElement = document.getElementById('dialog-create-league');
        let inputField = dialogElement.getElementsByClassName('dialog-input')[0].value;
        expect(inputField).to.equal('');

        // user sets input field to 'Division 12'.
        // call validateLeagueInput to set validInput to true.
        let newLeague = 'Division 12';
        dialogElement.getElementsByClassName('dialog-input')[0].value = newLeague;
        //validateLeagueInput(newLeague);
        console.log('global.validInput = ' + global.validInput);

        // set up the create button on the create league dialog.
        setUpDialogButtons(createLeagueOnServer);

        // stub out AJAX call in submitToServer.
        let ajaxStub = sinon.stub($, 'ajax');

        // user clicks update button on update league dialog.
        dialogElement.getElementsByClassName('submit-button')[0].click();

        // assert the 'create' arguments to the $.ajax call in submitToServer.
        // the data property contains a %20 instead of a space,
        // new-league-name=Division%2012
        // verifies league with name Division 12 is created.
        let ajaxArguments = ajaxStub.getCall(0).args[0]; 

        expect(ajaxArguments.cache).to.equal(false);
        expect(ajaxArguments.contentType).to.equal('application/x-www-form-urlencoded');
        expect(ajaxArguments.data).to.equal('new-league-name=Division%2012');
        expect(ajaxArguments.dataType).to.equal('json');
        expect(ajaxArguments.type).to.equal('POST');
        expect(ajaxArguments.url).to.equal('rest/LeagueService/leagues');

        leagueExports.getLeaguesFromServer.restore();
        leagueExports.openDialog.restore();
        ajaxStub.restore();
    });
});

describe('Read Operation - getLeaguesFromServer', function() {
    let actualValues = [{'id':12,'name':'Division 01'},
                        {'id':24,'name':'Division 03'},
                        {'id':36,'name':'Division 05'},
                        {'id':48,'name':'Division 07'},
                        {'id':60,'name':'Division 09'},
                        {'id':72,'name':'Division 02'},
                        {'id':84,'name':'Division 04'},
                        {'id':96,'name':'Division 06'},
                        {'id':108,'name':'Division 08'},
                        {'id':120,'name':'Division 10'}];

    /**
     * asserts the league table against 'expectedValues'.
     * asserts the 'data-league-id' attribute of each row element
     * is equal to the expected league id
     * asserts the first data cell of each row contains the expected league name.
     */
    function assertTable(expectedValues) {
        let tbodyElement = document.getElementById("league-list");
        let rowElement = tbodyElement.firstChild;
        let dataCellElement;
        let expectedLeagueId;
        let expectedLeagueName;

        for (let index = 0; index < expectedValues.length; index++) {
            rowElement = rowElement.nextSibling;
            expectedLeagueId = expectedValues[index].id.toString();
            expect(rowElement.getAttribute('data-league-id')).to.equal(expectedLeagueId);
            
            dataCellElement = rowElement.firstChild;
            expectedLeagueName = expectedValues[index].name;
            expect(dataCellElement.innerHTML).to.equal(expectedLeagueName);
        }
    }

    before(function(done) {
        JSDOM.fromFile('maintainleague.html')
            .then((dom) => {
                window = dom.window;
                document = window.document;
                $ = require('jquery');
            })
            .then(done, done);
    });

    it('should display leagues in a table on browser', function() {
        sinon.stub(leagueExports, 'getLeaguesFromServer').callsFake(function fakeFn() {
            displayLeaguesInTable(actualValues);
        });

        // load league table from actualValues by the above stub.
        leagueExports.getLeaguesFromServer();

        assertTable(actualValues);

        leagueExports.getLeaguesFromServer.restore();
    });
});

describe('Update Operation - updateLeague', function() {
    let actualValues = [{'id':12,'name':'Division 01'},
                        {'id':24,'name':'Division 03'},
                        {'id':36,'name':'Division 05'},
                        {'id':48,'name':'Division 07'},
                        {'id':60,'name':'Division 09'},
                        {'id':72,'name':'Division 02'},
                        {'id':84,'name':'Division 04'},
                        {'id':96,'name':'Division 06'},
                        {'id':108,'name':'Division 08'},
                        {'id':120,'name':'Division 10'}];

    before(function(done) {
        JSDOM.fromFile('maintainleague.html')
            .then((dom) => {
                window = dom.window;
                document = window.document;
                $ = require('jquery');
            })
            .then(done, done);
    });

    it('should update league on system', function() {

        sinon.stub(leagueExports, 'getLeaguesFromServer').callsFake(function fakeFn() {
            displayLeaguesInTable(actualValues);
        });

        // load league table from actualValues by the above stub.
        leagueExports.getLeaguesFromServer();

        // stub out openDialog to avoid calling ally.js code.
        // openDialog is called by updateLeague.
        sinon.stub(leagueExports, 'openDialog');

        // user selects 9th row in table. league id: 108 league name Division 08
        let tbodyElement = document.getElementById("league-list");
        let rowElement = tbodyElement.childNodes[9];

        // user clicks on update button. call updateLeague(this).
        // this opens the update league dialog.
        // the update button is in the second dataCell of the row element.
        let dataCellElement = rowElement.childNodes[1];
        let updateButtonElement = dataCellElement.firstChild;
        updateLeague(updateButtonElement);

        // update button opens the update league dialog.
        // assert id and old league name. Assert input field is empty.
        let dialogElement = document.getElementById('dialog-update-league');

        let inputField = dialogElement.getElementsByClassName('dialog-input')[0].value;
        let oldLeagueName =
            dialogElement.getElementsByClassName('dialog-old-name')[0].innerHTML;
        let leagueId =
            parseInt(dialogElement.getElementsByClassName('dialog-object-id')[0].value);

        expect(inputField).to.equal('');
        expect(oldLeagueName).to.equal('Division 08');
        expect(leagueId).to.equal(108);

        // user sets input field to 'Division 11'.
        // call validateLeagueInput to set validInput to true.
        let newLeague = 'Division 11';
        dialogElement.getElementsByClassName('dialog-input')[0].value = newLeague;
        validateLeagueInput(newLeague);

        // set up the update button on the update league dialog.
        setUpDialogButtons(updateLeagueOnServer);

        // stub out AJAX call in submitToServer.
        let ajaxStub = sinon.stub($, 'ajax');

        // user clicks update button on update league dialog.
        dialogElement.getElementsByClassName('submit-button')[0].click();

        // assert the 'update' arguments to the $.ajax call in submitToServer.
        // the data property contains a %20 instead of a space,
        // new-league-name=Division%2011
        // verifies league with id 108 is updated.
        let ajaxArguments = ajaxStub.getCall(0).args[0]; 

        expect(ajaxArguments.cache).to.equal(false);
        expect(ajaxArguments.contentType).to.equal('application/x-www-form-urlencoded');
        expect(ajaxArguments.data).to.equal(
            'league-id=108&new-league-name=Division%2011');
        expect(ajaxArguments.dataType).to.equal('json');
        expect(ajaxArguments.type).to.equal('PUT');
        expect(ajaxArguments.url).to.equal('rest/LeagueService/leagues');

        leagueExports.getLeaguesFromServer.restore();
        leagueExports.openDialog.restore();
        ajaxStub.restore();
    });
});

describe('Delete Operation - deleteLeague', function() {
    let actualValues = [{'id':12,'name':'Division 01'},
                        {'id':24,'name':'Division 03'},
                        {'id':36,'name':'Division 05'},
                        {'id':48,'name':'Division 07'},
                        {'id':60,'name':'Division 09'},
                        {'id':72,'name':'Division 02'},
                        {'id':84,'name':'Division 04'},
                        {'id':96,'name':'Division 06'},
                        {'id':108,'name':'Division 08'},
                        {'id':120,'name':'Division 10'}];

    before(function(done) {
        JSDOM.fromFile('maintainleague.html')
            .then((dom) => {
                window = dom.window;
                document = window.document;
                $ = require('jquery');
            })
            .then(done, done);
    });

    it('should delete league from system', function() {

        sinon.stub(leagueExports, 'getLeaguesFromServer').callsFake(function fakeFn() {
            displayLeaguesInTable(actualValues);
        });

        // load league table from actualValues by the above stub.
        leagueExports.getLeaguesFromServer();

        // stub out openDialog to avoid calling ally.js code.
        // openDialog is called by deleteLeague.
        sinon.stub(leagueExports, 'openDialog');

        // user selects 7th row in table. league id: 84 league name Division 04
        let tbodyElement = document.getElementById("league-list");
        let rowElement = tbodyElement.childNodes[7];

        // user clicks on delete button. call deleteLeague(this).
        // this opens the delete league dialog.
        // the delete button is in the third dataCell of the row element.
        let dataCellElement = rowElement.childNodes[2];
        let deleteButtonElement = dataCellElement.firstChild;
        deleteLeague(deleteButtonElement);

        // delete button opens the delete league dialog.
        // get span element and assert id and name.
        let dialogElement = document.getElementById('dialog-delete-league');
        let spanElement = dialogElement.getElementsByClassName('dialog-object-name')[0];
        let actualLeagueId = parseInt(spanElement.getAttribute('data-league-id'));
        expect(actualLeagueId).to.equal(84);
        expect(spanElement.innerHTML).to.equal('Division 04');

        // set up the delete button on the delete league dialog.
        setUpDeleteDialogButtons();

        // stub out AJAX call in deleteLeagueOnServer.
        let ajaxStub = sinon.stub($, 'ajax');

        // user clicks delete button on delete league dialog.
        dialogElement.getElementsByClassName('dialog-delete-button')[0].click();

        // assert the 'delete' arguments to the $.ajax call in deleteLeagueOnServer.
        // verifies league with id 84 is deleted.
        let ajaxArguments = ajaxStub.getCall(0).args[0]; 

        expect(ajaxArguments.async).to.equal(false);
        expect(ajaxArguments.cache).to.equal(false);
        expect(ajaxArguments.dataType).to.equal('json');
        expect(ajaxArguments.type).to.equal('DELETE');
        expect(ajaxArguments.url).to.equal('rest/LeagueService/leagues/84');

        leagueExports.getLeaguesFromServer.restore();
        leagueExports.openDialog.restore();
        ajaxStub.restore();
    });
});
