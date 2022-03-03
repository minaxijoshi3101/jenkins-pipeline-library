package com.mnit.erp

def call(Map pipelineParams) {

  def svcs = pipelineParams.SERVICES
  env.DNS = pipelineParams.DNS
    withCredentials([usernamePassword(credentialsId: 'dev-k8s-master', passwordVariable: 'pwd', usernameVariable: 'usr')]) { 
sh '''
echo "---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: $ENVIRONMENT-$NAMESPACE-ingress
  namespace: $NAMESPACE
  annotations:
    kubernetes.io/ingress.class: nginx
spec:
  rules:
  - host: $DNS # change the IP address here
    http:
      paths:" >> ${WORKSPACE}/ingress.yaml
'''
svcs.each {
  env.SVC = it
sh '''
echo "
      - path: /
        pathType: Prefix
        backend:
          service:
            name: $ENVIRONMENT-$SVC-svc
            port:
              number: 8080
            " >> ${WORKSPACE}/ingress.yaml
'''
}
sh '''
  echo Curiosity4ERP# | sudo -S kubectl apply -f ${WORKSPACE}/ingress.yaml
'''
    }
}