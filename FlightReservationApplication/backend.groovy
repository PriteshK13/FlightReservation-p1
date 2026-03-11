pipeline{
    agent any 
    stages{
        stage('Code-pull'){
            steps{

               git branch: 'main', url: 'https://github.com/PriteshK13/FlightReservation-p1.git'
            }
        }
         stage('Build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    mvn clean package
                '''
            }
        }
         stage('QA-Test'){
            steps{
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-token') {
                  sh'''
                     cd FlightReservationApplication
                     mvn sonar:sonar -Dsonar.projectKey=flight-reservation   
                  '''
                }
            }
        }
        stage('Docker-build'){
            steps{
                sh'''
                    cd FlightReservationApplication
                    docker build . -t priteshk13/flightreservation-new:latest
                    docker push priteshk13/flightreservation-new:latest
                    docker rmi priteshk13/flightreservation-new:latest
                  '''
            }
        }
           stage('Deploy'){
            steps{
                sh'''
                    cd FlightReservationApplication   
                    kubectl apply -f k8s/
                '''
            }
        }

    }
}


