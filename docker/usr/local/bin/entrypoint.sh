#!/bin/bash

set -o errexit

if [ -f "$AUTH0_CLIENT_SECRET_FILE" ]; then
  export AUTH0_CLIENT_SECRET=$(cat "$AUTH0_CLIENT_SECRET_FILE")
fi

if [ -f "$SSL_KEY_STORE_PASSWORD_FILE" ]; then
  export SSL_KEY_STORE_PASSWORD=$(cat "$SSL_KEY_STORE_PASSWORD_FILE")
fi

if [ -f "$SSL_KEY_PASSWORD_FILE" ]; then
  export SSL_KEY_PASSWORD=$(cat "$SSL_KEY_PASSWORD_FILE")
fi

if [ -f "$MYSQL_PASSWORD_FILE" ]; then
  export MYSQL_PASSWORD=$(cat "$MYSQL_PASSWORD_FILE")
fi

exec "$@"
