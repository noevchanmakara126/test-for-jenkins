pipeline {
    agent any

    triggers {
        githubPush()   // Trigger on GitHub push
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME   = 'spring-app'
        DOCKER_CRED_ID      = 'a990e212-cc96-415d-9fe6-035b49c77db4'  // Docker Hub PAT credential ID
        SSH_CRED_ID         = '433582c6-5ec0-45a7-bcb3-10dbc91b6759'  // SSH credential ID
    }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def latestTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
                    def commitTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:${commitHash}"

                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CRED_ID}") {
                                            def app = docker.build("${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}", ".")
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

    post {
        failure {
            echo "Pipeline failed. Please check the logs."
        }
        success {
            echo "Pipeline completed successfully!"
        }
    }
}
