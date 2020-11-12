class Constants {
    static final String MASTER_BRANCH = 'master'
    static final String DEVELOP_BRANCH = 'main'
    static final String STAGING_BRANCH = 'release-'

    static final String FLAVOUR_DEVELOPMENT = 'Development'
    static final String FLAVOUR_PRODUCTION = 'Production'
    static final String FLAVOUR_STAGING = 'Staging'

    static final String DEBUG_BUILD_TYPE = 'Debug'
    static final String RELEASE_BUILD_TYPE = 'Release'
}

def initialiseBuildEnv() {
    env.BUILD_TYPE = Constants.RELEASE_BUILD_TYPE
    if (env.BRANCH_NAME == Constants.DEVELOP_BRANCH || env.CHANGE_TARGET == Constants.DEVELOP_BRANCH) { //main = develop
        env.BUILD_FLAVOUR = Constants.FLAVOUR_DEVELOPMENT
        env.BUILD_TYPE = Constants.DEBUG_BUILD_TYPE
    } else if (env.BRANCH_NAME == Constants.MASTER_BRANCH || env.CHANGE_TARGET == Constants.MASTER_BRANCH) {
        env.BUILD_FLAVOUR = Constants.FLAVOUR_PRODUCTION
    } else {
        //do staging type
        env.BUILD_FLAVOUR = Constants.FLAVOUR_STAGING
    }
}

def getApkFileName() {
    env.FILE_NAME = "${appName}-${BUILD_TYPE}-${BUILD_NUMBER}.apk"
    if (env.BUILD_FLAVOUR == Constants.FLAVOUR_PRODUCTION) {
        env.FILE_NAME = "${appName}.apk"
    } else if (env.BUILD_FLAVOUR == Constants.FLAVOUR_STAGING) {
        env.FILE_NAME = "${appName}-Staging-${BUILD_NUMBER}.apk"
    }
}

pipeline {
    agent {dockerfile true}
    environment {
        appName = 'Sunflower'
    }
    stages {
        stage("Initialise") {
            steps {
                initialiseBuildEnv()
                getApkFileName()

                echo "Branch to build is: ${env.BRANCH_NAME}"
                echo "Change branch is: ${env.CHANGE_BRANCH}"
                echo "Target branch is: ${env.CHANGE_TARGET}"
                echo "Build number: ${env.BUILD_NUMBER}"

                echo "Flavour is: ${env.BUILD_FLAVOUR}"
                echo "Build type: ${env.BUILD_TYPE}"
            }
        }
        stage("Test") {
            steps {
                echo 'Testing'
//                sh './gradlew testProductionReleaseUnitTest'
            }
        }
        stage("Build") {
            steps {
                echo 'Building apk'
                sh "./gradlew clean assemble${BUILD_FLAVOUR}${BUILD_TYPE}"

                echo "Successful build ${currentBuild.fullDisplayName}"
                echo "Url:  ${currentBuild.absoluteUrl}"
                echo "Workspace: ${env.WORKSPACE}"
                echo "DIR: ${currentBuild.fullProjectName}"
            }
        }
//        stage("Quality Control") {
//            steps {
//                sh './gradlew testProductionReleaseUnitTestCoverage'
//            }
//        }
        stage("Deploy") {
            environment {
                apkLocation = "${env.WORKSPACE}/app/build/outputs/apk/production/release/app-production-release-unsigned.apk"
                newApk = "${env.WORKSPACE}/app/build/outputs/${env.FILE_NAME}"
            }
            steps {
                echo 'Deploy apk'
                echo apkLocation
                echo newApk

                script {
                    if (fileExists(apkLocation)) {
                        writeFile(file: newApk, encoding: "UTF-8", text: readFile(file: apkLocation, encoding: "UTF-8"))
                        echo 'Successfully renamed file'
                    }
                }
            }
        }
        stage("Post Actions") {
            steps {
                echo 'Do after build things'
            }
        }
    }
}