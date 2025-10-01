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
        stage('Docker Login') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId: 'adfa3fe4-30a1-472d-8e57-14f82295a72f', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    }
                }
            }
        }

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
