pipeline {
    agent any
    stages {
        stage('PULL'){
            steps{
                git branch: 'main', url: 'https://github.com/PriteshK13/FlightReservation-p1.git'
            }
        }
        stage('BUILD'){
            steps {
                sh'''
                    cd frontend
                    npm install
                    npm run build
                '''
            }
        }
        stage('DEPLOY'){
            steps{
                sh'''
                cd frontend
                aws s3 sync dist/ s3://cbz-frontend-pritesh-1302/ 
                '''
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            cleanWs()
        }
    }
}