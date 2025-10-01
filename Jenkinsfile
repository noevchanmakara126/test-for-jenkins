pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:latest
    tty: true
  - name: maven
    image: maven:3.9.3-eclipse-temurin-21
    command: ['cat']
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest-debug
    command: ['sleep']
    args: ['infinity']
    tty: true
    volumeMounts:
    - name: docker-config
      mountPath: /kaniko/.docker
  volumes:
  - name: docker-config
    emptyDir: {}
"""
        }
    }

    environment {
        REGISTRY = "makarajr126"
        IMAGE_NAME = "spring-mini-project"
        IMAGE_TAG = "${BUILD_NUMBER}"
        FULL_IMAGE = "${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Build JAR') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                container('kaniko') {
                    // Create Docker config.json for authentication
                    sh '''
                        echo "{\"auths\":{\"https://index.docker.io/v1/\":{\"auth\":\"$(echo -n "${DOCKERHUB_CREDENTIALS_USR}:${DOCKERHUB_CREDENTIALS_PSW}" | base64 | tr -d \\\\n)\"}}}" > /kaniko/.docker/config.json
                    '''

                    // Build and push with Kaniko
                    sh """
                        /kaniko/executor \\
                          --dockerfile=Dockerfile \\
                          --context=dir://workspace/spring-mini-project \\
                          --destination=${FULL_IMAGE} \\
                          --cache=true
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build and push successful! Image: ${FULL_IMAGE}"
        }
        failure {
            echo "❌ Pipeline failed. Check logs."
        }
    }
}