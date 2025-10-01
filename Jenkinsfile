pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo "Building Spring Boot project..."
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                echo "Checking if app is already running..."

                // Stop old process if running
                sh '''
                PID=$(pgrep -f "spring-boot-application.jar" || true)
                if [ ! -z "$PID" ]; then
                  echo "Stopping existing Spring Boot app with PID $PID"
                  kill -9 $PID
                  sleep 5
                fi
                '''

                // Run new build
                sh '''
                echo "Starting new Spring Boot app..."
                nohup java -jar target/*.jar > app.log 2>&1 &
                sleep 10
                '''
            }
        }

        stage('Verify') {
            steps {
                echo "Verifying if Spring Boot app is running..."
                sh '''
                PID=$(pgrep -f "spring-boot-application.jar" || true)
                if [ -z "$PID" ]; then
                  echo "App failed to start!"
                  exit 1
                else
                  echo "App is running with PID $PID"
                fi
                '''
            }
        }
    }
}
