#mdaservice

MDA_SERVICE_VERSION=1.1.4-SNAPSHOT \
  && TEST_ENVIRONMENT=DV1 \
  && TEST_RUN_ENVIRONMENT=QA1 \
  && INT_ENVIRONMENT=INT \
  && RUN_ENVIRONMENT=INT

# package
# clean, then package skip tests
# clean & run
mvn clean package -DSERVICE_TEST_ENVIRONMENT=${TEST_ENVIRONMENT} \
\
&& mvn clean && if [ -d target ]; then
    echo mvn clean failed; else
    mvn package -DskipTests; fi \
\
&& pushd mdaservice >/dev/null && if [ -f target/mdaservice-${MDA_SERVICE_VERSION}.jar ]; then
  mvn spring-boot:run -Dspring-boot.run.profiles=${TEST_RUN_ENVIRONMENT}; else
  echo packaging jar failed; fi && popd >/dev/null || popd >/dev/null \
&& find . -name mdaservice-${MDA_SERVICE_VERSION}.jar


# clean, then package skip tests
mvn clean && if [ ! -d target ]; then
  mvn package \
    -DSERVICE_TEST_ENVIRONMENT=${INT_ENVIRONMENT}; else
  echo mvn clean failed; fi

# run the jar
java -jar \
   -Dspring.profiles.active=${RUN_ENVIRONMENT} \
   mdaservice/target/mdaservice-${MDA_SERVICE_VERSION}.jar
