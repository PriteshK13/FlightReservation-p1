pipeline {
    agent any
    stages {
        stage('PULL') {
            steps{
                git branch: 'main', url: 'https://github.com/PriteshK13/FlightReservation-p1.git'
            }
        }
        stage('code-build') {
            steps{
                '''
                cd FlightReservationApplication
                mvn clean package
                '''
            }
        }
        stage('QA-Test') {
            steps{
                withSonarQubeEnv(installationName:'sonar',credentialsId: 'Sonar-token') {
                    sh '''
                    cd FlightReservationApplication
                    mvn sonar:sonar -Dsonar.projectkey=flight-reservation
                
                    '''
                }
            }
        }
    }
}