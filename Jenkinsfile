pipeline {
    agent {dockerfile true}
    environment {
        appName = 'Android-Work-Manager'
    }
    stages {
        stage("Test") {
            steps {
                echo 'Testing'
                sh './gradlew testProductionReleaseUnitTest'
            }
        }
        stage("Build") {
            steps {
                echo 'Building apk'
                sh './gradlew assembleProductionRelease'
            }
        }
        stage("Quality Control") {
            steps {
                sh './gradlew testProductionReleaseUnitTestCoverage'
            }
        }
        stage("Deploy") {
            steps {
                echo 'Deploy apk'
            }
        }
        stage("Post Actions") {
            steps {
                echo 'Do after build things'
            }
        }
    }
}