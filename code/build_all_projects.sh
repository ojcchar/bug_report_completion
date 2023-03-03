#!/bin/bash
set -x #echo on

export JAVA_HOME=`/usr/libexec/java_home -v 12.0.2`

export CUR_DIR=`pwd`

export REPOSITORIES_PATH=/Users/ojcchar/repositories/Projects

export APPCORE_REPO_PATH=$REPOSITORIES_PATH/appcore
export TXT_ANALYZER_REPO_PATH=$REPOSITORIES_PATH/text-analyzer
export BUG_REPORT_COMPLETION_REPO_PATH=$REPOSITORIES_PATH/bug_report_completion

# repo update
cd "$APPCORE_REPO_PATH" && git pull
cd "$TXT_ANALYZER_REPO_PATH" && git pull
cd "$BUG_REPORT_COMPLETION_REPO_PATH" && git pull

# project building
cd "$APPCORE_REPO_PATH/appcore" && ./gradlew clean testClasses install
cd "$TXT_ANALYZER_REPO_PATH/text-analyzer" && ./gradlew clean testClasses install
cd "$BUG_REPORT_COMPLETION_REPO_PATH/code/bug_report_coding" && ./gradlew clean testClasses install
cd "$BUG_REPORT_COMPLETION_REPO_PATH/code/bug_report_patterns" && ./gradlew clean testClasses install
cd "$BUG_REPORT_COMPLETION_REPO_PATH/code/bug_report_classifier" && ./gradlew clean testClasses install
cd "$BUG_REPORT_COMPLETION_REPO_PATH/code/bug_report_parser/bugparser" && ./gradlew clean testClasses install
cd "$BUG_REPORT_COMPLETION_REPO_PATH/code/tools" && ./gradlew clean testClasses install

cd $CUR_DIR
