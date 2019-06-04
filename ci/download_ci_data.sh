#!/bin/bash
curl -X POST https://content.dropboxapi.com/2/files/download \
  --header "Authorization: Bearer $DROPBOX_KEY" \
  --header "Dropbox-API-Arg: {\"path\": \"/$SIGNING_ARCHIVE_NAME\"}" \
  -o "./$SIGNING_ARCHIVE_NAME" \
  && unzip -o $SIGNING_ARCHIVE_NAME \
  && rm $SIGNING_ARCHIVE_NAME