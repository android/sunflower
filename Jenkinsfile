pipeline {
    agent {dockerfile true}
    environment {
        appName = 'Android-Work-Manager'
    }
    stages {
        stage("Test") {
            steps {
                echo 'Testing'
//                sh './gradlew testDebugUnitTest'
            }
        }
        stage("Build") {
            steps {
                echo 'Building apk'
                sh './gradlew assembleDebug'
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