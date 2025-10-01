pipeline {
    agent {
        kubernetes {
            label 'docker-agent'
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:latest
    args: ['\${computer.jnlpmac}', '\${computer.name}']
    tty: true
  - name: docker
    image: docker:24.0.5
    command:
    - cat
    tty: true
  volumes:
  - name: workspace-volume
    emptyDir: {}
"""
        }
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('d3b37208-0637-449b-bbd2-e15241f4409c')
        DOCKER_IMAGE = "makarajr126/spring-app"
        CONTAINER_NAME = "spring-app"
    }

    stages {
        stage('Stop & Remove Existing Container') {
            steps {
                container('docker') {
                    sh """
                    if [ \$(docker ps -q -f name=${CONTAINER_NAME}) ]; then
                        echo "Stopping container ${CONTAINER_NAME}"
                        docker stop ${CONTAINER_NAME}
                    fi

                    if [ \$(docker ps -a -q -f name=${CONTAINER_NAME}) ]; then
                        echo "Removing container ${CONTAINER_NAME}"
                        docker rm ${CONTAINER_NAME}
                    fi
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                container('docker') {
                    sh """
                    docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .
                    docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                        docker push ${DOCKER_IMAGE}:latest
                        docker logout
                        """
                    }
                }
            }
        }

        stage('Deploy New Container') {
            steps {
                container('docker') {
                    sh """
                    docker pull ${DOCKER_IMAGE}:${BUILD_NUMBER}
                    docker run -d --name ${CONTAINER_NAME} -p 9090:9090 ${DOCKER_IMAGE}:${BUILD_NUMBER}
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful! Running version: ${BUILD_NUMBER}"
        }
        failure {
            echo "❌ Deployment failed. Check logs."
        }
    }
}
