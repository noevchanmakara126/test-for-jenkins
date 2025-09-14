pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "your-dockerhub-username/spring-app"
        SERVER_USER  = "makara"
        SERVER_HOST  = "167.172.139.6"
        SSH_CRED_ID  = "a468b279-b1b8-44d9-9b40-a5d74fc849d1" // Jenkins credential ID for SSH
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/noevchanmakara126/test-for-jenkins.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building Spring Boot project...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging jar...'
                sh 'mvn package -DskipTests'
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
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
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
            echo 'Spring Boot CI/CD pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed. Check logs.'
        }
    }
}
