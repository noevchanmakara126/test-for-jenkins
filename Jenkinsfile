pipeline {
    agent {
        kubernetes {
            defaultContainer 'maven'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven
    image: maven:3.9.3-eclipse-temurin-21
    command:
    - cat
    tty: true
"""
        }
    }

    stages {
        stage('Build') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                PID=$(pgrep -f "spring-boot-application.jar" || true)
                if [ ! -z "$PID" ]; then
                  echo "Stopping old app (PID $PID)"
                  kill -9 $PID
                  sleep 5
                fi

                nohup java -jar target/*.jar > app.log 2>&1 &
                sleep 10
                '''
            }
        }

        stage('Verify') {
            steps {
                sh '''
                PID=$(pgrep -f "spring-boot-application.jar" || true)
                if [ -z "$PID" ]; then
                  echo "App failed to start!"
                  exit 1
                else
                  echo "App running with PID $PID"
                fi
                '''
            }
        }
    }
}
