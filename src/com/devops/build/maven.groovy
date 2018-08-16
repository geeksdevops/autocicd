/************************************************************
***** Description :: This Package is used for Maven Build *****
***** Author      :: Pramod Vishwakarma                  *****
***** Date        :: 08/17/2018                         *****
***** Revision    :: 1.0                                *****
************************************************************/

package com.devops.build

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
** Function to Build Maven Project
*********************************************/
def mvnBuild(String MAVEN_GOAL)
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	    println "\u001B[32mINFO => Building Maven build, please wait..."
		sh """
		   mvn $MAVEN_GOAL
		"""
	  }
   }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41mERROR => failed to install Maven module..."
		 currentBuild.result = 'FAILED'
         throw caughtException
      }
   }
}

/********************************************
** Function to Build MAVEN Skip Test 
*********************************************/
def mvnTestSkip(String MVN_SKIP_GOAL)
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	    println "\u001B[32mINFO => Testing Maven build, please wait..."
		sh "mvn $MVN_SKIP_GOAL"
		currentBuild.result = 'SUCCESS'
		step([$class: 'StashNotifier'])
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41mERROR => failed to install Maven build..."
		 currentBuild.result = 'FAILED'
		 step([$class: 'StashNotifier'])
         throw error
      }
   }
}

/********************************************
** Function to perform the linting
*********************************************/
def esLint(String ESLINT_CONFIG_FILE, String CHECKSTYLE_FILE)
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	    println "\u001B[32mINFO => performing node code linting, please wait..."
		sh """
		   eslint -c $ESLINT_CONFIG_FILE -f checkstyle . | tee $CHECKSTYLE_FILE || echo "ESLint failed, continuing with the build process..."
		"""
	  }
   }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41mERROR => node code linting failed..."
		 currentBuild.result = 'FAILED'
         throw caughtException
      }
   }
}


