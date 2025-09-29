pipeline {
    agent {
        kubernetes {
            label 'jenkins-maven-kaniko'
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
    volumeMounts:
    - name: workspace-volume
      mountPath: /workspace
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    securityContext:
      runAsUser: 0
    volumeMounts:
    - name: workspace-volume
      mountPath: /workspace
  - name: jnlp
    image: jenkins/inbound-agent:3341.v0766d82b_dec0-1
    volumeMounts:
    - name: workspace-volume
      mountPath: /workspace
  volumes:
  - emptyDir: {}
    name: workspace-volume
"""
        }
    }

    triggers {
        githubPush()   // Trigger on GitHub push
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME   = 'spring-app'
        DOCKER_CRED_ID      = 'd3b37208-0637-449b-bbd2-e15241f4409c'  // Docker Hub PAT credential ID
        GIT_COMMIT          = ''
    }

    stages {
        stage('Checkout') {
            steps {
                container('maven') {
                    checkout scm
                    script {
                        env.GIT_COMMIT = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    }
                }
            }
        }

        stage('Build JAR') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests -DoutputDirectory=/workspace'
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
                            // Docker tag using Jenkins build number
                            def buildTag = "${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}"
                            sh """
                            /kaniko/executor \
                              --dockerfile=/workspace/Dockerfile \
                              --context=dir:///workspace \
                              --destination=${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:latest \
                              --destination=${buildTag} \
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
