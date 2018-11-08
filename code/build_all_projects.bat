
set CUR_DIR=%CD%

set REPOSITORIES_PATH=C:\Users\ojcch\Documents\Repositories\Git

set APPCORE_REPO_PATH=%REPOSITORIES_PATH%\appcore
set TXT_ANALYZER_REPO_PATH=%REPOSITORIES_PATH%\text-analyzer
set BUG_REPORT_COMPLETION_REPO_PATH=%REPOSITORIES_PATH%\bug_report_completion

REM repo update
cd "%APPCORE_REPO_PATH%" && git pull
cd "%TXT_ANALYZER_REPO_PATH%" && git pull
cd "%BUG_REPORT_COMPLETION_REPO_PATH%" && git pull


REM project building
cd "%APPCORE_REPO_PATH%\appcore" && call gradlew clean install && @echo on
cd "%TXT_ANALYZER_REPO_PATH%\text-analyzer" && call gradlew clean install && @echo on
cd "%BUG_REPORT_COMPLETION_REPO_PATH%\code\bug_report_coding" && call gradlew clean install && @echo on
cd "%BUG_REPORT_COMPLETION_REPO_PATH%\code\bug_report_patterns" && call gradlew clean install && @echo on
cd "%BUG_REPORT_COMPLETION_REPO_PATH%\code\bug_report_classifier" && call gradlew clean install && @echo on
cd "%BUG_REPORT_COMPLETION_REPO_PATH%\code\bug_report_parser\bugparser" && call gradlew clean install && @echo on
cd "%BUG_REPORT_COMPLETION_REPO_PATH%\code\tools" && call gradlew clean install && @echo on


cd "%CUR_DIR%"