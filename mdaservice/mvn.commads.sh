#mdaservice

SERVICE_TEST_ENVIRONMENT=DV1
mvn clean package -DSERVICE_TEST_ENVIRONMENT=${SERVICE_TEST_ENVIRONMENT}

# clean, then package skip tests
mvn clean && if [ ! -d target ]; then
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

# run the jar
MDA_SERVICE_VERSION=1.1.4-SNAPSHOT
java -jar \
   -Dspring.profiles.active=QA1 \
   target/mdaservice-${MDA_SERVICE_VERSION}.jar
