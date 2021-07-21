def call(Map pipelineparam)
{
  env.REPO_NAME=pipelineparam.REPO_NAME
  env.BRANCH=pipelineparam.BRANCH
  pipeline
  {
    node
    {
      stage("check-scm")
      {
      sh '''
      git clone $REPO_NAME
      cd $REPO_NAME
      git checkout $BRANCH
      '''
      }
      stage("build code")
      {
        sh '''
        mvn clean install
        
        '''
      }
      stage("push image to docker registry")
      {
        
      }
    }
  }
}
