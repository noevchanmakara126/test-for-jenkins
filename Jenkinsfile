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
       stage('Check if git repo manifest exists') {
                  steps {
                      sh '''
                              REPO_DIR="manifest-testing"

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
                sh 'ls'
                sh 'git clone https://github.com/noevchanmakara126/manifest-testing.git'
                sh 'ls'
                sh 'cd manifest-testing'
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
                   sh 'docker build -t makarajr126/spring-app:${BUILD_NUMBER} .'
                   sh 'docker images'
                }
            }
       }
       stage('Update Manifest') {
           steps {
               sh '''
                   cd manifest-testing
                   sed -i "s|image: .*|image: makarajr126/spring-app:${BUILD_NUMBER}|" deployment.yaml
                   cat deployment.yaml
               '''
           }
       }
        stage('Commit & Push Manifest') {
                   steps {
                       withCredentials([string(credentialsId: 'a094976e-2529-476f-befa-7137fc60af94', variable: 'GIT_TOKEN')]) {
                           sh '''
                               cd manifest-testing
                               git config user.name "Noev Chanmakara"
                               git config user.email "jrmakara97@gmail.com"
                               git add deployment.yaml
                               git commit -m "Update image tag to ${BUILD_NUMBER}" || echo "No changes to commit"
                               git push https://${GIT_USER}:${GIT_PASS}@github.com/noevchanmakara126/manifest-testing.git HEAD:main
                           '''
                       }
                   }
       }
       stage('Push Image'){
            steps {
                container('docker'){
                    sh 'docker push makarajr126/spring-app:${BUILD_NUMBER}'
                }
            }
       }
    }
}
