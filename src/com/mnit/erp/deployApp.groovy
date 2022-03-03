package com.mnit.erp

def call(Map pipelineParams) {

    def fileWrite = libraryResource "app-service.yaml"
	writeFile file: "${WORKSPACE}/${REPO}/app-service.yaml", text: fileWrite
    withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'user')]) { 
        sh '''
            cd ${WORKSPACE}/${REPO}/
            sed -i "s;%APP_NAME%;${APP_NAME};" app-service.yaml
            sed -i "s;%NAMESPACE%;${NAMESPACE};" app-service.yaml
            sed -i "s;%DOCKER_REGISTRY%;${DOCKER_REGISTRY};" app-service.yaml
            sed -i "s;%CONTAINER_PORT%;${CONTAINER_PORT};" app-service.yaml
            sed -i "s;%HOST_PORT%;${HOST_PORT};" app-service.yaml
            sed -i "s;%HOST_IP%;${HOST_IP};" app-service.yaml
            sed -i "s;%REPO%;${REPO};" app-service.yaml
            sed -i "s;%REPLICAS%;${REPLICAS};" app-service.yaml
            sed -i "s;%ENVIRONMENT%;${ENVIRONMENT};" app-service.yaml
            echo Curiosity4ERP#| sudo -S kubectl apply -f app-service.yaml
        '''
    }
}