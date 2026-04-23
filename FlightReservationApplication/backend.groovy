pipeline {
    agent any
    stages {
        stage('PULL') {
            steps{
                git branch: 'main', url: 'https://github.com/PriteshK13/FlightReservation-p1.git'
            }
        }
    }
}