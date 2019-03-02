#!/usr/bin/env bash

./gradlew moxy-x:assemble
./gradlew moxy-x-android:assemble
./gradlew moxy-x-androidx:assemble
./gradlew moxy-x-app-compat:assemble
./gradlew moxy-x-compile:assemble

echo "artifacts built"

cd moxy/artifacts-maven

pwd

gpg2 -ab moxy-x-javadoc.jar
gpg2 -ab moxy-x-sources.jar
gpg2 -ab moxy-x.jar
gpg2 -ab moxy-x.pom

echo "gpg for moxy-x built"

cd .. && cd ..

cd moxy-android/artifacts-maven

gpg2 -ab moxy-x-android-javadoc.jar
gpg2 -ab moxy-x-android-sources.jar
gpg2 -ab moxy-x-android.jar
gpg2 -ab moxy-x-android.pom

echo "gpg for moxy-x-android built"

cd .. && cd ..

cd moxy-androidx/artifacts-maven

gpg2 -ab moxy-x-androidx-javadoc.jar
gpg2 -ab moxy-x-androidx-sources.jar
gpg2 -ab moxy-x-androidx.jar
gpg2 -ab moxy-x-androidx.pom

echo "gpg for moxy-x-androidX built"

cd .. && cd ..

cd moxy-app-compat/artifacts-maven

gpg2 -ab moxy-x-app-compat-javadoc.jar
gpg2 -ab moxy-x-app-compat-sources.jar
gpg2 -ab moxy-x-app-compat.jar
gpg2 -ab moxy-x-app-compat.pom

echo "gpg for moxy-x-app-compat built"

cd .. && cd ..

cd moxy-compiler/artifacts-maven

gpg2 -ab moxy-x-compiler-javadoc.jar
gpg2 -ab moxy-x-compiler-sources.jar
gpg2 -ab moxy-x-compiler.jar
gpg2 -ab moxy-x-compiler.pom

echo "gpg for moxy-x-app-compiler built"