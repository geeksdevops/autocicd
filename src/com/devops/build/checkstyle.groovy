package com.devops.build

/********************************************
** Function to perform the linting
*********************************************/
def checkstyleReport(String CHECKSTYLE_FILE)
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	    println "\u001B[32mINFO => displaying checkstyle analysis report, please wait..."
		checkstyle canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', failedTotalAll: '9000', failedTotalHigh: '9000', failedTotalLow: '9000', failedTotalNormal: '9000', healthy: '1', pattern: "${CHECKSTYLE_FILE}", unHealthy: '', unstableTotalAll: '9000', unstableTotalHigh: '9000', unstableTotalLow: '10', unstableTotalNormal: '10'
	  }
   }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41mERROR => checkstyle analysis report failed, exiting..."
		 currentBuild.result = 'FAILED'
         throw caughtException
      }
   }
}

