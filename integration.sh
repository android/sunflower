#!/usr/bin/env bash

./dockerized_device.sh "./gradlew cAT -Pandroid.testInstrumentationRunnerArguments.class='integration.TestSuite'" 0af4c07d

# Rest of the connected tests
# ./dockerized_device.sh "./gradlew cAT -Pandroid.testInstrumentationRunnerArguments.notPackage=integration" 0af4c07d