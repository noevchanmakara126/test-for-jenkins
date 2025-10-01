pipeline {
    agent {
        kubernetes {
            label 'kaniko-agent'
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:latest
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest-debug  # -debug includes shell for sh steps
    command: ["sleep"]
    args: ["infinity"]
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
        DOCKERHUB_CREDENTIALS = credentials('adfa3fe4-30a1-472d-8e57-14f82295a72f')
        DOCKER_IMAGE = "makarajr126/spring-app"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Push Image with Kaniko') {
            steps {
                container('kaniko') {
                    // Create docker config.json for authentication
                    sh '''
                        echo "{\"auths\":{\"https://index.docker.io/v1/\":{\"auth\":\"$(echo -n "${DOCKERHUB_CREDENTIALS_USR}:${DOCKERHUB_CREDENTIALS_PSW}" | base64 | tr -d \\\\n)\"}}}" > /kaniko/.docker/config.json
                    '''

                    // Build and push both tags
                    sh """
                        /kaniko/executor \
                          --dockerfile=Dockerfile \
                          --context=dir://workspace/spring-mini-project \
                          --destination=${DOCKER_IMAGE}:${BUILD_NUMBER} \
                          --destination=${DOCKER_IMAGE}:latest \
                          --cache=true
                    """
                }
            }
        }

        // Optional: Deploy stage (if you still want to run the container somewhere)
        // Note: You can't use `docker run` in Kaniko pod — consider deploying to Kubernetes instead
        stage('Deploy to Kubernetes (Optional)') {
            steps {
                script {
                    echo "✅ Image pushed successfully. Consider deploying to Kubernetes via kubectl or Helm."
                    // Example: kubectl set image deployment/spring-app *=${DOCKER_IMAGE}:${BUILD_NUMBER}
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build and push successful! Image: ${DOCKER_IMAGE}:${BUILD_NUMBER}"
        }
        failure {
            echo "❌ Build or push failed."
        }
    }
}