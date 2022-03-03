package com.mnit.erp

def call(Map pipelineParams) {

    withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'user')]) { 
        sh '''
            echo Curiosity4ERP# | sudo -S kubectl delete deployment $ENVIRONMENT-$APP_NAME-deployment -n $NAMESPACE
        '''
    }
}