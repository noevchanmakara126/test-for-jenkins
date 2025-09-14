pipeline {
    agent any

    tools {
        jdk 'Java-21'       // Name you added in Jenkins Global Tool Configuration
        maven 'Maven-3.8.8' // Name you added in Jenkins Global Tool Configuration
    }

    environment {
        DOCKER_IMAGE = "makarajr126/spring-app"
        SERVER_USER  = "makarajr"
        SERVER_HOST  = "167.172.139.6"
        SSH_CRED_ID  = "bee195fe-71cc-45d6-9f12-858b7bed765c" // Jenkins SSH credential ID
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out Git repository...'
                git branch: 'main', url: 'https://github.com/noevchanmakara126/test-for-jenkins.git'
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Building and testing Spring Boot project...'
                sh 'mvn clean package' // Add -DskipTests if you want to skip tests
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    echo 'Logging in to Docker Hub...'
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    echo "Pushing Docker image ${DOCKER_IMAGE}:${env.BUILD_NUMBER}..."
                    sh "docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                echo 'Deploying Docker container on remote server...'
                sshagent([env.SSH_CRED_ID]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_HOST} \\
                    "docker pull ${DOCKER_IMAGE}:${env.BUILD_NUMBER} && \\
                     docker stop spring-app || true && \\
                     docker rm spring-app || true && \\
                     docker run -d --name spring-app -p 9090:8080 ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Spring Boot CI/CD pipeline succeeded!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs!'
        }
    }
}
