pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checkout'
                git credentialsId: 'jenkinstoken', url: 'http://212.129.149.40/171250027_expelliarmus/backend-cold4'
            }
        }
        stage('Build') {
            steps {
                echo 'Building'
                sh 'mvn clean'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
                sh '''
                ssh root@39.96.75.119 'cd service && make deploy-app'
                '''
            }
        }
    }
}