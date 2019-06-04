#!/bin/bash
gcloud firebase test android run \
  --type instrumentation \
  --app ../app/build/outputs/apk/debug/app-debug.apk \
  --test ../app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk \
  --device-ids Nexus6 \
  --os-version-ids 21 \
  --locales en \
  --orientations portrait \
  --async