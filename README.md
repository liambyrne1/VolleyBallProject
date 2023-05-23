# VolleyBallProject
To implement a Volleyball league system.



SYSTEM OVERVIEW

For an overview of system please see:-
https://github.com/liambyrne1/VolleyBallProject/documentation/design/overview.docx



SYSTEM ENVIRONMENT

To set up the system, all you need is a Java Environment, Gradle and a PostgreSQL database. For download see:-
https://github.com/liambyrne1/VolleyBallProject/blob/master/documentation/general/Set Up Environment.docx



SYSTEM EXECUTION

A simple example is to move the three top teams from league 05 to league 04. See:-
https://github.com/liambyrne1/VolleyBallProject/blob/master/documentation/general/System Execution.docx


TO RUN THE GRADLE BUILD FILE build.gradle

Change to the VolleyBallProject directory, the directory that holds build.gradle
The parameters "--console=plain --info" give more information
All artifacts are built in the the VolleyBallProject/build directory.



TO BUILD WAR FILE

>gradlew war --console=plain --info

VolleyBallProject.war is created in build/libs directory



