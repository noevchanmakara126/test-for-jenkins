pipeline {
    // 1. Use a specific agent label (must match your Kubernetes Pod Template label)
    agent {
        label 'maven-docker-agent'
    }

    triggers {
        githubPush()   // Trigger on GitHub push
    }

    environment {
        DOCKER_HUB_USERNAME = 'makarajr126'
        DOCKER_IMAGE_NAME   = 'spring-app'
        DOCKER_CRED_ID      = 'd3b37208-0637-449b-bbd2-e15241f4409c'  // Docker Hub PAT credential ID
        SSH_CRED_ID         = '433582c6-5ec0-45a7-bcb3-10dbc91b6759'  // SSH credential ID
        // IMPORTANT: The Docker agent needs to know where the daemon is.
        // This variable is necessary if you're connecting to the Kubernetes host's Docker or a dind service.
        DOCKER_HOST         = 'tcp://dind-service:2376' // Example for DIND setup
    }

    stages {
        stage('Build JAR') {
            // Run this stage inside the 'maven' container
            agent { container 'maven' }
            steps {
                // withMaven is not needed since the agent image already has Maven installed and configured
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            // Run this stage inside the 'maven' container
            agent { container 'maven' }
            steps {
                sh 'mvn test'
            }
        }

        // --- Docker Cleanup (Run in the 'docker' container) ---
        stage('Remove Old Container and Image'){
           agent { container 'docker' }
           steps {
            // Note: -f (force) is used to avoid failure if the container/image doesn't exist
            sh 'docker rm -f spring-app-container || true'
            sh 'docker rmi -f ${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:latest || true'
           }
        }

        // --- Docker Build & Run (Run in the 'docker' container) ---
        stage('Build & Run Docker Image') {
            agent { container 'docker' }
            steps {
                script {
                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def latestTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
                    def commitTag  = "${env.DOCKER_HUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:${commitHash}"

                    // 1. Login to Docker Hub
                    // The 'docker' plugin helper handles the login using the credential ID
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CRED_ID}") {

                        // 2. Build the image
                        def app = docker.build("${latestTag}", ".")

                        // 3. Push to Docker Hub
                        app.push()
                        app.push("${commitTag}")

                        echo "✅ Image pushed with tag: ${commitTag}"

                        // 4. Run the container locally (on the Docker host connected to the agent)
                        app.run("-d -p 9090:9090 --name spring-app-container")
                        echo "✅ New container 'spring-app-container' started on port 9090"
                    }
                }
            }
        }
    }

    post {
        failure {
            echo "❌ Pipeline failed. Please check the logs."
        }
        success {
            echo "✅ Pipeline completed successfully! Application is running on port 9090."
        }
    }
}