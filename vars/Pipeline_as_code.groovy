/*************************************************************************
**** Description :: This groovy code is used to run the JAVA  pipeline ****
**** Created By  :: Pramod Vish.                                       ****
**** Created On  :: 12/14/2017                                        ****
**** version     :: 1.0                                               ****
**************************************************************************/
import com.devops.scm.*
import com.devops.build.*
import com.devops.sonar.*
import com.devops.reports.*
//import com.devops.notification.*

def call(body) 
{
   def config = [:]
   body.resolveStrategy = Closure.DELEGATE_FIRST
   body.delegate = config
   body()
   timestamps {
     try {
        def m = new maven()
        m.createReportDirectory("${config.REPORT_DIRECTORY}")
        def html = new htmlReport()
        currentBuild.result = "SUCCESS"
        NEXT_STAGE = "none"
          BRANCH = 'dev'
	  stage ('\u2776 Code Checkout') {
          def g = new git()
          g.Checkout("${config.GIT_URL}","${BRANCH}","${config.GIT_CREDENTIALS}")
          NEXT_STAGE='Maven_Build_Process'
        }
        stage ('\u2777 Pre-Build Tasks') {
           parallel (
             "\u2460 Maven Build" : {
                while(NEXT_STAGE != 'Maven_Build_Process') {
                  continue
                }    
                def g = new maven()
	            g.mvnBuild("${config.MAVEN_GOAL}")
                NEXT_STAGE='Maven_Skip_Test'
             },
             "\u2461 Maven skipTest" : {
                while(NEXT_STAGE != 'Maven_Skip_Test') {
                 continue
               }
	            g = new maven()
	            g.mvnTestSkip()
             },
             failFast: true
           )
	    }
       stage ('\u2778 Build Tasks') {
           parallel (
             "\u2460 Mave Build" : {
               def g = new maven()
                g.mvnBuild("${config.MAVEN_GOAL}, ${config.MAVEN_HOME}")
                NEXT_STAGE='code_analysis'
             },
             "\u2461 Code Analysis" : {
                while(NEXT_STAGE != 'code_analysis') {
                  continue
                }
                def g = new javaAnalysis()
	            g.javaSonarAnalysis("${config.SONAR_PROPERTY}")
             },
             failFast: true
           )
	    }
        stage ('\u2779 Post-Build Tasks') {
           parallel (
             "\u2462 Deployment Alert" : {
               while(NEXT_STAGE != 'send_alert') {
                continue
               }
//                def e = new email()
//                e.sendDeployEmail("${config.BRANCH}, $BUILD_NAME")
            },
             failFast: true
          )
        }
     }
     catch (Exception caughtError) {
        wrap([$class: 'AnsiColorBuildWrapper']) {
            print "\u001B[41mERROR => pipeline failed, check detailed logs..."
            currentBuild.result = "FAILURE"
            throw caughtError
        }
     }
     finally {
         //def e = new email()
         String BODY = new File("${WORKSPACE}/${config.EMAIL_TEMPLATE}").text   
         e.sendemail("${currentBuild.result}","$BODY","${config.RECIPIENT}")
   }
  }
}

