@echo off
mvn clean package -pl mdaservice -am -DSERVICE_TEST_ENVIRONMENT=DV1

pause
