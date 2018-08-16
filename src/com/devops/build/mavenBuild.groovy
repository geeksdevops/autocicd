/***************************************************************************
***** Description :: This Package is used to perform RubyOnRails tasks *****
***** Author      :: Pramod Vishwakarma                                *****
***** Date        :: 12/18/2017                                        *****
***** Revision    :: 1.0                                               *****
****************************************************************************/
package com.fcaa.devops.build.ruby

/**************************************************
***** Function to create the report directory *****
***************************************************/
def createReportDirectory(String REPORT_DIRECTORY)
{
   try {
     wrap([$class: 'AnsiColorBuildWrapper']) {
        println "\u001B[32mINFO => Creating directory $REPORT_DIRECTORY if not already exist..."
        sh "mkdir -p $REPORT_DIRECTORY"
     }
   }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
        println "\u001B[41mERROR => failed to create the directory $REPORT_DIRECTORY, exiting..."
        currentBuild.result = 'FAILED'
        throw caughtException
      }
   }
}
/********************************************
***** Function to Maven Build routes    *****
*********************************************/
def mvnBuild(String MAVEN_GOAL)
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
        println "\u001B[32mINFO => Build proccess started, please wait..."
        sh "mvn $MAVEN_GOAL"
      }
   }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
        println "\u001B[41mERROR => failed to project MAVEN build , exiting..."
        currentBuild.result = 'FAILED'
        throw caughtException
      }
   }
}

