# VolleyBallProject
To implement a Volleyball league system.



TO RUN THE GRADLE BUILD FILE build.gradle

Change to the VolleyBallProject directory, the directory that holds build.gradle
The parameters "--console=plain --info" give more information
All artifacts are built in the the VolleyBallProject/build directory.



TO BUILD WAR FILE

>gradlew war --console=plain --info

VolleyBallLeagueSystem.war is created in build/libs directory



TO RUN APP

>gradlew appRun --console=plain --info

Access web app at http://localhost:8080/VolleyBallLeagueSystem/createleague.html



TO RUN THE UNIT TESTS

>gradlew test --console=plain --info

Test results are located at build/reports/tests/test/index.html
