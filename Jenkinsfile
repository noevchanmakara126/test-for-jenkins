pipeline {
    agent {
        kubernetes {
            defaultContainer 'docker'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: docker
    image: docker:20.10.17
    command:
    - cat
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  - name: maven
    image: maven:3.9.3-eclipse-temurin-21
    command:
    - cat
    tty: true
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
"""
        }
    }

    environment {
        REGISTRY = "docker.io/makarajr126"   // change to your registry
        IMAGE_NAME = "spring-mini-project"
    }

    stages {
        stage('Build JAR') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                container('docker') {
                    sh '''
                    IMAGE_TAG=${BUILD_NUMBER}
                    docker build -t $REGISTRY/$IMAGE_NAME:$IMAGE_TAG .
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId: 'dadfa3fe4-30a1-472d-8e57-14f82295a72f', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        IMAGE_TAG=${BUILD_NUMBER}
                        docker push $REGISTRY/$IMAGE_NAME:$IMAGE_TAG
                        '''
                    }
                }
            }
        }

//         stage('Deploy via ArgoCD') {
//             steps {
//                 echo "Triggering ArgoCD sync..."
//                 sh '''
//                 argocd app sync spring-mini-project \
//                   --grpc-web \
//                   --auth-token $ARGOCD_TOKEN \
//                   --server $ARGOCD_SERVER
//                 '''
//             }
//         } post {
                    success {
                        echo "✅ Build and push successful! Image: "
                    }
                    failure {
                        echo "❌ Pipeline failed. Check logs."
                    }
                }

    }
}
