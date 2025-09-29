pipeline {
    agent {
        kubernetes {
            label 'jenkins-kaniko-agent'
            defaultContainer 'maven'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven
    image: maven:3.9.3-eclipse-temurin-17
    command:
    - cat
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    securityContext:
      runAsUser: 0
"""
        }
    }

    triggers {
        githubPush() // Trigger on GitHub push
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME   = 'spring-app'
        DOCKER_CRED_ID      = 'd3b37208-0637-449b-bbd2-e15241f4409c'  // Docker Hub PAT credential ID
    }

    stages {

        stage('Build JAR') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                container('maven') {
                    sh 'mvn test'
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                container('kaniko') {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CRED_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        script {
                            def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                            sh """
                            /kaniko/executor \
                              --dockerfile=Dockerfile \
                              --context=dir://. \
                              --destination=${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:latest \
                              --destination=${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:${commitHash} \
                              --registry-user=${DOCKER_USER} \
                              --registry-pass=${DOCKER_PASS}
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed. Check logs for details."
        }
    }
}
