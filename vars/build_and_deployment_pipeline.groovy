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
        }
      }
    }
