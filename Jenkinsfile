pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: docker
    image: docker:24.0.7-cli
    command:
    - cat
    tty: true
    volumeMounts:
    - name: dockersock
      mountPath: /var/run/docker.sock
  volumes:
  - name: dockersock
    hostPath:
      path: /var/run/docker.sock
"""
        }
    }

    stages {
        stage('Pull Spring App Image') {
            steps {
                container('docker') {
                    sh 'docker pull makarjr126/spring-app:latest'
                }
            }
        }

        stage('Run Spring App Container') {
            steps {
                container('docker') {
                    sh '''
                        docker rm -f spring-app || true
                        docker run -d --name spring-app -p 8080:8080 makarjr126/spring-app:latest
                    '''
                }
            }
        }
    }
}
