pipeline {
    agent {
        label 'maven-docker-agent'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}
