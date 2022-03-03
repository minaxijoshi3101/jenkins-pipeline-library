package com.mnit.erp

def call(Map pipelineParams) {

    def fileWrite = libraryResource "$APP_NAME/rootSecrets.yaml"
    def fileWrite2 = libraryResource "$APP_NAME/serviceSecrets.yaml"
    writeFile file: "${WORKSPACE}/rootSecrets.yaml", text: fileWrite
	writeFile file: "${WORKSPACE}/serviceSecrets.yaml", text: fileWrite2
    credId = "${ENVIRONMENT}-k8s-master"
    withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'user')]) { 
        sh '''
            cd ${WORKSPACE}/
            sed -i "s;%APP_NAME%;${APP_NAME};" serviceSecrets.yaml
            sed -i "s;%NAMESPACE%;${NAMESPACE};" serviceSecrets.yaml
            sed -i "s;%ENVIRONMENT%;${ENVIRONMENT};" serviceSecrets.yaml
            sed -i "s;%APP_NAME%;${APP_NAME};" rootSecrets.yaml
            sed -i "s;%NAMESPACE%;${NAMESPACE};" rootSecrets.yaml
            sed -i "s;%ENVIRONMENT%;${ENVIRONMENT};" rootSecrets.yaml
            echo 'Curiosity4ERP#' | sudo -S kubectl apply -f serviceSecrets.yaml
            echo 'Curiosity4ERP#' | sudo -S kubectl apply -f rootSecrets.yaml
        '''
    }
}