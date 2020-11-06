pipeline {
    agent {dockerfile true}
    environment {
        appName = 'Android-Work-Manager'
    }
    stages {
        stage("Test") {
            steps {
                echo "Branch to build is: ${env.BRANCH_NAME}"
                echo "Change branch is: ${env.CHANGE_BRANCH}"
                echo "Target branch is: ${env.CHANGE_TARGET}"
                echo "Build number: ${env.BUILD_NUMBER}"

                echo 'Testing'
                sh './gradlew testProductionReleaseUnitTest'
            }
        }
        stage("Build") {
            steps {
                echo 'Building apk'
                sh './gradlew assembleProductionRelease'

                echo "Successful build ${currentBuild.fullDisplayName}"
                echo "Url:  ${currentBuild.absoluteUrl}"
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