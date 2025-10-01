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
     triggers {
            githubPush()
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
       stage('Check if git repo exists') {
           steps {
               sh '''
                       REPO_DIR="test-for-jenkins"

                       if [ -d "$REPO_DIR" ]; then
                           echo "Repository folder exists! Removing it..."
                           rm -rf "$REPO_DIR"
                       else
                           echo "Repository folder does not exist. Nothing to remove."
                       fi
                       '''
           }
       }
        stage('Clone the git repo for manifest'){
            steps {
                sh 'git clone https://github.com/noevchanmakara126/manifest-testing.git'
                sh 'ls'
            }
       }
       stage('Clone the git repo'){
                   steps {
                       sh 'git clone https://github.com/noevchanmakara126/test-for-jenkins.git'
                       sh 'cd test-for-jenkins '
                       sh 'ls'
                   }
              }

       stage('Build Image'){
            steps {
                container('docker'){
                   sh 'docker build -t makarajr126/spring-app:latest .'
                   sh 'docker images'
                }
            }
       }
       stage('Push Image'){
            steps {
                container('docker'){
                    sh 'docker push makarajr126/spring-app:latest'
                }
            }
       }
    }
}
