package com.mnit.erp

def call(Map pipelineParams) {

    def fileWrite = libraryResource "$APP_NAME/configMap.yaml"
	writeFile file: "${WORKSPACE}/configMap.yaml", text: fileWrite
    credId = "${ENVIRONMENT}-k8s-master"
    withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'user')]) { 
        sh '''
            cd ${WORKSPACE}/
            sed -i "s;%APP_NAME%;${APP_NAME};" configMap.yaml
            sed -i "s;%NAMESPACE%;${NAMESPACE};" configMap.yaml
            sed -i "s;%HOST_IP%;${HOST_IP};" configMap.yaml
            sed -i "s;%DB_NAME%;${DB_NAME};" configMap.yaml
            sed -i "s;%ENVIRONMENT%;${ENVIRONMENT};" configMap.yaml
            echo 'Curiosity4ERP#' | sudo -S kubectl apply -f configMap.yaml
        '''
    }
}