def call(Map pipelineparam)
{
  env.REPO_NAME=pipelineparam.REPO_NAME
  env.BRANCH=pipelineparam.BRANCH
  env.GIT_URL=pipelineparam.GIT_URL
  env.GIT_GROUP=pipelineparam.GIT_GROUP
  pipeline
  {
    node
    {
      stage("check-scm")
      {
      sh '''
      rm -rf $WORKSPACE/
      git clone $GIT_URL"/"$GIT_GROUP"/"$REPO_NAME
      cd $REPO_NAME
      git checkout $BRANCH
      echo "$(ls)"
      '''
      }
      stage("build code")
      {
        sh '''
        echo $PWD
        cd $REPO_NAME
        mvn clean install
        '''
      }
      stage("push image to docker registry")
      {
        
      }
    }
  }
}
