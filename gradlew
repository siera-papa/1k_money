#!/bin/sh
set -e
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_URL="https://raw.githubusercontent.com/gradle/gradle/v9.4.1/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Gradle wrapper JAR missing; downloading official Gradle 9.4.1 wrapper..." >&2
  mkdir -p "$(dirname "$WRAPPER_JAR")"
  if command -v curl >/dev/null 2>&1; then
    curl -fL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$WRAPPER_JAR" "$WRAPPER_URL"
  else
    echo "Install curl or wget, then run again." >&2
    exit 1
  fi
fi

if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD=java
fi
exec "$JAVACMD" -Xmx64m -Xms64m -classpath "$WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
