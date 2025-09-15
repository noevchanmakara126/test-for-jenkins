pipeline {
    agent any

    triggers {
        githubPush()   // 👈 Automatically trigger build when GitHub notifies Jenkins
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME = 'spring-app'
        DOCKER_CRED_ID = 'a990e212-cc96-415d-9fe6-035b49c77db4'
        SSH_CRED_ID = '433582c6-5ec0-45a7-bcb3-10dbc91b6759'
    }

    stages {
        stage('Build JAR') {
            steps {
                withMaven(maven: 'M3') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                withMaven(maven: 'M3') {
                    sh 'mvn test'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def latestTag = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
                        sh "docker build -t ${latestTag}:latest ."
                    withDockerRegistry(credentialsId: "${DOCKER_CRED_ID}", url: "") {

                        sh "docker push ${latestTag}"
                    }
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                sshagent(["${SSH_CRED_ID}"]) {
                    sh """
                        ssh makara@167.172.139.6 '
                          cd /home/makara/PracticeJenkins/test-for-jenkins &&
                          docker-compose pull &&
                          docker-compose up -d --build --force-recreate
                        '
                    """
                }
            }
        }
    }
}
