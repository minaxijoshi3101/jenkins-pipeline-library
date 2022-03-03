package com.mnit.util

def call(Map pipelineParams) {
    env.SCM_URL="git@github.com:"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
    echo "Code checkout from SCM Repo"
    sh ''' 
        rm -rf ${REPO}
		git clone --single-branch --branch ${BRANCH} ${SCM_URL}
    ''' 
    echo "Checkout is completed!"
}
