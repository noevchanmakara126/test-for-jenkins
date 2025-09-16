pipeline {
    agent any

    triggers {
        githubPush()   // Trigger on GitHub push
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME   = 'spring-app'
        DOCKER_CRED_ID      = 'dckr_pat_Qe92AJ2xHNtCPno75wyGny-mOjA'  // Docker Hub PAT credential ID
        SSH_CRED_ID         = '433582c6-5ec0-45a7-bcb3-10dbc91b6759'  // SSH credential ID
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
                    def latestTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
                    def commitTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:${commitHash}"

                    // Build Docker image with both tags
                    sh "docker build -t ${latestTag} ."
                    sh "docker rm -f jenkins-container"
                    sh "doccker run -d -p 9090:9090 --name jenkins-container ${latestTag}"

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

    post {
        failure {
            echo "Pipeline failed. Please check the logs."
        }
        success {
            echo "Pipeline completed successfully!"
        }
    }
}
