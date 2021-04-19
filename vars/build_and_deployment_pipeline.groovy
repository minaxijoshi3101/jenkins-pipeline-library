def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      env.REPO = pipelineParams.REPO
      pipeline {
        new environmentVars().call(pipelineParams)
        node(pipelineParams.BUILD_NODE) {
          stage("Code Checkout") {
            env.SCM_URL="git@github.com:"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
            echo "Code checkout from SCM Repo"
            sh ''' 
                rm -rf ${REPO}
                git clone --single-branch --branch ${BRANCH} ${SCM_URL}
                ''' 
            echo "Checkout is completed!"
          }
          
          stage("Build") {
             if(pipelineParams.APP_TYPE == "JAVA")
              {
                  sh '''
                      cd $REPO
                      mvn deploy -P docker -Ddocker.host=${DOCKER_HOST} -Ddocker.registry.name=${DOCKER_REGISTRY} -Dmaven.test.skip=true
                  '''
              }
             else {
                      echo "No build for the APP_TYPE"+pipelineParams.APP_TYPE
             }
          }
           stage("Static Code Analysis") {
           }
           stage("Delete Previous Deployment") {
             
              withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'user')]) { 
                sh '''
                    echo Curiosity4ERP# | sudo -S kubectl delete deployment $ENVIRONMENT-$APP_NAME-deployment -n $NAMESPACE
                '''
    }

              stage("Deploy") {
                
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
             if(pipelineParams.EMAIL_TO_LIST?.trim()){   
          echo "email send enabled"   
          //new sendEmail().call(pipelineParams,"SUCCESS")   
        } 
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
