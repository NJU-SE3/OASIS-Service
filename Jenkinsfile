pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checkout'
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/NJU-SE3/OASIS-Service']]])
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
                sh 'ls'
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