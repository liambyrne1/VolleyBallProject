# VolleyBallProject
To implement a Volleyball league system.



SYSTEM OVERVIEW

For an overview of system
please see VolleyBallProject/documentation/design/overview.docx



PROPERTY FILE

For property file set up,
please see VolleyBallProject/documentation/properties/setup.docx



TO RUN THE GRADLE BUILD FILE build.gradle

Change to the VolleyBallProject directory, the directory that holds build.gradle
The parameters "--console=plain --info" give more information
All artifacts are built in the the VolleyBallProject/build directory.



TO BUILD WAR FILE

>gradlew war --console=plain --info

VolleyBallLeagueSystem.war is created in build/libs directory



TO RUN APP

To load the database before running the app use one of the following scripts:-
VolleyBallProject/database/InitVolleyballTest.sql
VolleyBallProject/database/basicLeagueTest.sql

To run the app:-
>gradlew appRun --console=plain --info

Access web app at http://localhost:8080/VolleyBallLeagueSystem/maintainLeague.html



TO RUN THE UNIT TESTS

To run the Java unit test on a Windows system.
Automatically loads the database with the
VolleyBallLeagueSystem\database\InitVolleyballTest.sql script.

>gradlew testOnWindows --console=plain --info

To run the Java unit test on a non Windows system.
The database has to be first manually loaded with the sql script
VolleyBallLeagueSystem\database\InitVolleyballTest.sql

>gradlew testNotOnWindows --console=plain --info

Test results are located at build/reports/tests/test/index.html



MOCHA

The frontend is tested with Mocha.

To set up and run the mocha unit tests,
please see VolleyBallProject/documentation/mocha/setup/setup.docx

For the mocha output,
please see VolleyBallProject/documentation/mocha/output directory.



TO TEST GITHUB SSH PROTOCOL
