pipeline {
    agent any
    stages {
        stage('PULL') {
            steps{
                git branch: 'main', url: 'https://github.com/PriteshK13/FlightReservation-p1.git'
            }
        }
        stage('Build') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    java -version
                     mvn -version

                    cd FlightReservationApplication
                    mvn clean package -DskipTests
                '''
  }
}
        stage('QA-Test') {
             steps {
                withSonarQubeEnv('sonar') {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    cd FlightReservationApplication

                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=flight-app \
                    -DskipTests
                '''
    }
  }
}
        stage('Docker-build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    docker build . -t priteshk13/flight-reservation-app:latest
                    docker push priteshk13/flight-reservation-app:latest
                    docker rmi priteshk13/flight-reservation-app:latest
                '''
            }
        }
         stage('DEPLOY') {
            steps{
                sh'''
                    cd FlightReservationApplication
                    kubectl apply -f k8s/
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