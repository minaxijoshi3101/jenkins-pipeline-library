package com.mnit.util

def call(Map pipelineParams, String TYPE) { 
 
env.EMAIL_TO_LIST=pipelineParams.EMAIL_TO_LIST 
         
        if("SUCCESS".equals(TYPE)){ 
            emailext attachLog: true,compressLog: true, body: '''${SCRIPT, template="my-email.template"}''',  
                            subject: "${currentBuild.currentResult}- ${env.JOB_NAME} - Build # ${env.BUILD_NUMBER}",  
                            mimeType: 'text/html',to: "${EMAIL_TO_LIST}" 
        }else if("FAILURE".equals(TYPE)){ 
            emailext attachLog: true,compressLog: true, body: '''${SCRIPT, template="my-email.template"}''',  
                                                subject: "${currentBuild.currentResult} - ${env.JOB_NAME} - Build # ${env.BUILD_NUMBER}",  
                                                mimeType: 'text/html',to: "${EMAIL_TO_LIST}" 
        } 
} 
