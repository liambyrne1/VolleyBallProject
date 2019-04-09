# VolleyBallManagement
To implement a Volleyball league system.



TO RUN THE GRADLE BUILD FILE build.gradle

Change to the VolleyBallManagement directory, the directory that holds build.gradle
The parameters "--console=plain --info" give more information
The dependencies are downloaded to the ~/.gradle/caches/modules-2/files-2.1 directory
(38 items, 31MB)
All artifacts are built in the the VolleyBallManagement/build directory.



TO BUILD WAR FILE

>gradlew war --console=plain --info

VolleyBallManagement.war is created in build/libs directory



TO RUN APP

>gradlew appRun --console=plain --info

Access web app at http://localhost:8080/VolleyBallManagement/createleague.html



TO RUN THE UNIT TESTS

>gradlew test --console=plain --info

Test results are located at build/reports/tests/test/index.html
