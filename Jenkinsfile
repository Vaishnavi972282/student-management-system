pipeline {
    agent any

    tools {
        maven 'Maven-3.9.x'
        jdk 'JDK-21'
    }

    environment {
        APP_NAME = 'student-management'
        DOCKER_IMAGE = 'student-management:local'
        BUILD_VERSION = '1.0.0'
    }

    stages {
        stage('1. Checkout') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('2. Validate Environment') {
            steps {
                echo 'Validating Java and Maven installation...'
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('3. Compile') {
            steps {
                echo 'Compiling source code...'
                sh 'mvn clean compile'
            }
        }

        stage('4. Run Automated Tests') {
            steps {
                echo 'Running JUnit 5 and Spring Boot MockMvc test cases...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('5. Package Application') {
            steps {
                echo 'Packaging executable Spring Boot JAR file...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('6. Archive Artifacts') {
            steps {
                echo 'Archiving built JAR artifact...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('7. Build Docker Image') {
            steps {
                echo 'Building Docker container image...'
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded! Application ${APP_NAME} built and tested successfully."
        }
        failure {
            echo 'Pipeline failed! Please check logs above for compilation or test failures.'
        }
    }
}
