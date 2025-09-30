pipeline {
    agent any

    triggers {
        githubPush()   // Trigger on GitHub push
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME   = 'spring-app'
        DOCKER_CRED_ID      = 'd3b37208-0637-449b-bbd2-e15241f4409c'  // Docker Hub PAT credential ID
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
        stage('remove container and images'){
           steps {
            sh 'docker rm -f spring-app-container '
            sh 'docker rmi -f makarajr126/spring-app:latest '
           }
        }

//         stage('Build & Push Docker Image') {
//             steps {
//                 script {
//                     def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
//                     def latestTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
//                     def commitTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:${commitHash}"
//
//                     docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CRED_ID}") {
//                         def app = docker.build("${latestTag}", ".")
//
//                        app.run("-d -p 9090:9090 --name spring-app-container")
//
//                     }
//                 }
//             }
//         }
    }

    post {
        failure {
            echo "❌ Pipeline failed. Please check the logs."
        }
        success {
            echo "✅ Pipeline completed successfully!"
        }
    }
}