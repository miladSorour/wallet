#!/bin/bash

echo "The application will start in ${APP_SLEEP}s..." && sleep "${APP_SLEEP}"

exec java "${JAVA_OPTS}" -noverify -XX:+UseContainerSupport -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "org.milad.wallet.WalletApplication"  "$@"