#mdaclient

mvn clean package


# clean, then package skip tests
mvn clean && if [ ! -d target ]; then
  mvn test; else
    echo mvn clean failed; fi \
  && mvn package -DskipTests

# clean & run
ENV=QA1
mvn clean && if [ ! -d target ]; then
  mvn spring-boot:run -Dspring-boot.run.profiles=${ENV}; else
  echo mvn clean failed; fi

# clean, then package skip tests
SERVICE_TEST_ENVIRONMENT=DV1
mvn clean && if [ ! -d target ]; then
  mvn package \
    -DSERVICE_TEST_ENVIRONMENT=${SERVICE_TEST_ENVIRONMENT} \
    -DskipTests; else
  echo mvn clean failed; fi

# run the jar with parameter
MDA_CLIENT_VERSION=1.1.0-SNAPSHOT
REEL_IDENTIFIER=REEL123
java -jar \
   -Dspring.profiles.active=${ENV} \
   target/mdaclient-${MDA_CLIENT_VERSION}.jar \
   ${REEL_IDENTIFIER}
