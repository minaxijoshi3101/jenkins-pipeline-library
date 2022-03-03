import com.mnit.erp.environmentVars;
import com.mnit.erp.generateIngress;
import com.mnit.util.sendEmail;
def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      env.REPO = pipelineParams.REPO
      pipeline {
        new environmentVars().call(pipelineParams)
        node(pipelineParams.BUILD_NODE) {
          sh '''
            rm -rf ${WORKSPACE}/*
          '''
          stage("Delete Service") {
            new generateIngress().call(pipelineParams)
          }
        }
        if(pipelineParams.EMAIL_TO_LIST?.trim()){   
          echo "email send enabled"   
          //new sendEmail().call(pipelineParams,"SUCCESS")   
        } 

      }
    }
  }catch (err) {
    echo "in catch block" 
    echo "Caught: ${err}" 
    currentBuild.result = 'FAILURE' 
    if(pipelineParams.EMAIL_TO_LIST?.trim()){   
      echo "email send enabled"   
      //sendEmail().call(pipelineParams,"FAILURE")   
    }  
    throw err
  }
}
