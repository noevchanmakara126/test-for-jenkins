pipeline {
    agent { label 'docker-agent' } // The label of your new pod template

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE = "makarajr126/spring-app"
        CONTAINER_NAME = "spring-app-c"
    }

    stages {
        stage('Stop & Remove Existing Container') {
            steps {
                sh """
                if [ \$(docker ps -q -f name=${CONTAINER_NAME}) ]; then
                    docker stop ${CONTAINER_NAME}
                fi
                if [ \$(docker ps -a -q -f name=${CONTAINER_NAME}) ]; then
                    docker rm ${CONTAINER_NAME}
                fi
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .
                docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
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

        stage('Deploy New Container') {
            steps {
                sh """
                docker pull ${DOCKER_IMAGE}:${BUILD_NUMBER}
                docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${DOCKER_IMAGE}:${BUILD_NUMBER}
                """
            }
        }
    }

    post {
        success { echo "✅ Deployment successful! Version: ${BUILD_NUMBER}" }
        failure { echo "❌ Deployment failed." }
    }
}
