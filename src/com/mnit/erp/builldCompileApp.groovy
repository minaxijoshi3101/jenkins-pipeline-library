package com.mnit.erp

def call(Map pipelineParams) {
    if(pipelineParams.APP_TYPE == "JAVA")
    {
        sh '''
            cd $REPO
            mvn deploy -P docker -Ddocker.host=${DOCKER_HOST} -Ddocker.registry.name=${DOCKER_REGISTRY} -Dmaven.test.skip=true
        '''
    }
    else if(pipelineParams.APP_TYPE == "NODE")
    {
        sh '''
            cd $REPO
            npm install
            if [ $ENVIRONMENT == 'dev' ]
            then
                #npm run build:dev
                npm run build
            elif [ $ENVIRONMENT == 'uat' ]
            then
                npm run build:uat
            elif [ $ENVIRONMENT == 'prd' ]
            then
                npm run build:prd
            else
                npm run build
            fi

            version=$(jq -r .version package.json)
            sudo su -c "docker build -t ${DOCKER_REGISTRY}${REPO}:$version ."
            sudo su -c "docker build -t ${DOCKER_REGISTRY}${REPO}:latest ."
            sudo su -c "docker push ${DOCKER_REGISTRY}${REPO}:$version"
            sudo su -c "docker push ${DOCKER_REGISTRY}${REPO}:latest"
        '''
    }
    else {
        echo "No build for the APP_TYPE"+pipelineParams.APP_TYPE
    }
}