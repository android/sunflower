fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew cask install fastlane`

# Available Actions
## Android
### android unit_tests
```
fastlane android unit_tests
```
Run the unit tests
### android assemble_debug
```
fastlane android assemble_debug
```
Assemble a debug apk
### android assemble_android_test
```
fastlane android assemble_android_test
```
Assemble an android test apk
### android instrumentation_tests
```
fastlane android instrumentation_tests
```
Run the instrumentation tests in the Firebase test lab
### android deploy_internal
```
fastlane android deploy_internal
```
Deploy the app to the internal track

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
