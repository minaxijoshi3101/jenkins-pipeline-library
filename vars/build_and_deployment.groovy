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
        
        '''
      }
      stage("upload artifacts to nexus repo")
      {
        nexusArtifactUploader artifacts: [
          [artifactId: 'java-mysql-example', 
           classifier: '', 
           file: 'target/usermanagement_javasqlproject-1.0-SNAPSHOT.jar', 
           type: 'jar'
          ]
        ], 
          credentialsId: 'nexus3', 
          groupId: 'org.funtimecoding', 
          nexusUrl: 'http://52.140.68.208:8081/', 
          nexusVersion: 'nexus3', 
          protocol: 'http', 
          repository: 'http://52.140.68.208:8081/repository/app-releases/', 
          version: '1.0-SNAPSHOT'
      }
