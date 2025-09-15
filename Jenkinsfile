pipeline {
    agent any

    environment {
        // Your Docker Hub username
        DOCKER_HUB_USERNAME = 'makarajr126'
        // Name of the Docker image
        DOCKER_IMAGE_NAME = 'spring-app'
        // Credential ID configured in Jenkins for Docker Hub
        DOCKER_CRED_ID = 'a990e212-cc96-415d-9fe6-035b49c77db4'
    }

    stages {
        stage('1. Checkout') {
            steps {
                echo 'Cloning repository...'
                git url: 'https://github.com/noevchanmakara126/test-for-jenkins.git'
            }
        }

        stage('2. Build') {
            steps {
                echo 'Building and testing with Maven...'
                withMaven(maven: 'M3') {
//                     sh 'mvn clean package -DskipTests' // Skips tests to speed up the build stage
                }
            }
        }

        stage('3. Run Unit Tests') {
            steps {
                echo 'Running unit tests...'
                withMaven(maven: 'M3') {
                    sh 'mvn test'
                }
            }
        }

        stage('4. Build and Push Docker Image') {
            steps {
                echo 'Building Docker image...'
                // Builds the Docker image and tags it with a unique build number
                script {
                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def imageTag = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:${commitHash}"
                    docker.build(imageTag)

                    // Login and push the image to the registry
                    withDockerRegistry(credentialsId: "${DOCKER_CRED_ID}", url: "") {
                        echo 'Pushing image to Docker Hub...'
                        sh "docker push ${imageTag}"
                    }
                }
            }
        }

        stage('5. Deploy to Production') {
            steps {
                echo 'Deploying with Docker Compose on a remote server...'
                // You can use a Jenkins SSH Agent or a dedicated server agent for this step.
                // Replace 'user@your-server' and the path to your docker-compose.yml file.
                sshagent(['433582c6-5ec0-45a7-bcb3-10dbc91b6759']) {
                    sh "ssh makara@167.172.139.6 'cd /home/makara/PracticeJenkins/test-for-jenkins && docker-compose pull && docker-compose up -d --build --force-recreate'"
                }
            }
        }
    }
}