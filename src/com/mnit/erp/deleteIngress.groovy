package com.mnit.erp

def call(Map pipelineParams) {

    withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'user')]) { 
        sh '''
            echo Curiosity4ERP# | sudo -S kubectl delete ingress $ENVIRONMENT-$NAMESPACE-ingress -n $NAMESPACE
        '''
    }
}