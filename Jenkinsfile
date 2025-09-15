pipeline {
    agent any

    environment {
        // Your Docker Hub username
        DOCKER_HUB_USERNAME = 'makarajr126'
        // Name of the Docker image
        DOCKER_IMAGE_NAME = 'spring-app'
        // Credential ID configured in Jenkins for Docker Hub
        DOCKER_CRED_ID = 'a990e212-cc96-415d-9fe6-035b49c77db4'
        // Credential ID for the SSH agent
        SSH_CRED_ID = '433582c6-5ec0-45a7-bcb3-10dbc91b6759'
    }

    stages {
        stage('1. Build') {
            steps {
                echo 'Building and packaging the project...'
                withMaven(maven: 'M3') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('2. Run Unit Tests') {
            steps {
                echo 'Running unit tests...'
                withMaven(maven: 'M3') {
                    sh 'mvn test'
                }
            }
        }

        stage('3. Build and Push Docker Image') {
            steps {
                echo 'Building Docker image...'
                script {
                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def imageTag = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:${commitHash}"
                    docker.build(imageTag)

                    withDockerRegistry(credentialsId: "${DOCKER_CRED_ID}", url: "") {
                        echo 'Pushing image to Docker Hub...'
                        sh "docker push ${imageTag}"
                    }
                }
            }
        }

        stage('4. Deploy to Production') {
            steps {
                echo 'Deploying with Docker Compose on a remote server...'
                sshagent(["${SSH_CRED_ID}"]) {
                    sh "ssh makara@167.172.139.6 'cd /home/makara/PracticeJenkins/test-for-jenkins && docker-compose pull && docker-compose up -d --build --force-recreate'"
                }
            }
        }
    }
}