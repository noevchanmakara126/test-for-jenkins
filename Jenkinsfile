pipeline {
    agent any

    stages {
        stage('Pull Spring App Image') {
            steps {
                script {
                    sh 'docker pull makarjr126/spring-app:latest'
                }
            }
        }

        stage('Run Spring App Container') {
            steps {
                script {
                    // Stop and remove old container if exists
                    sh '''
                        if [ "$(docker ps -aq -f name=spring-app)" ]; then
                            docker rm -f spring-app || true
                        fi
                    '''

                    // Run new container
                    sh 'docker run -d --name spring-app -p 8080:8080 makarjr126/spring-app:latest'
                }
            }
        }
    }
}
