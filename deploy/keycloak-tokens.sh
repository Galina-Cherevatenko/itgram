#!/bin/bash

KCHOST=http://localhost:8080
REALM=ru-itgram
CLIENT_ID=ru-itgram-service
UNAME=ru-itgram-test
PASSWORD=otus

# shellcheck disable=SC2006
#ACCESS_TOKEN=`curl \
#  -d "client_id=$CLIENT_ID" -d "client_secret=$CLIENT_SECRET" \
#  -d "username=$UNAME" -d "password=$PASSWORD" \
#  -d "grant_type=password" \
#  "$KCHOST/auth/realms/$REALM/protocol/openid-connect/token"  | jq -r '.access_token'`

ACCESS_TOKEN=`curl \
  -d "client_id=$CLIENT_ID" \
  -d "username=$UNAME" \
  -d "password=$PASSWORD" \
  -d "grant_type=password" \
  "$KCHOST/realms/$REALM/protocol/openid-connect/token"  | jq -r '.access_token'`
echo "$ACCESS_TOKEN"
