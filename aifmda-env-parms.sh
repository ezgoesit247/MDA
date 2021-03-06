#mdaservice

functionlist=()

export MDA_SERVICE_VERSION=1.1.4-SNAPSHOT
export TEST_INT_ENVIRONMENT=DV1
export TEST_RUN_ENVIRONMENT=QA1
export INT_ENVIRONMENT=INT
export RUN_ENVIRONMENT=INT

function init_INT_db {
  PSWD='P@ssw0rd!'
  mysql -A -uaifmda_admin -p`sed 's/!/\\!/g' <<<$(echo $PSWD)` -h host.docker.internal aifmda < mdaservice/src/test/resources/aifmda-init-INT.sql

  #mysql -A -uaifmda_admin -p$(echo P@ssw0rd\!) -h host.docker.internal aifmda < mdaservice/src/test/resources/aifmda-init-INT.sql

}

functionlist+=(test_integrations)
function test_integrations {
mvn test -DSERVICE_TEST_ENVIRONMENT=${TEST_INT_ENVIRONMENT}
}

functionlist+=(run_integrations)
function run_integrations {
init_INT_db
mvn test -DSERVICE_TEST_ENVIRONMENT=${INT_ENVIRONMENT}
}

functionlist+=(build_integration)
function build_integration {
mvn clean && if [ ! -d target ]; then
mvn package -DskipTests \
-DSERVICE_TEST_ENVIRONMENT=${INT_ENVIRONMENT}; 
else echo mvn clean failed && exit 1; fi
ls mdaservice/target/mdaservice-${MDA_SERVICE_VERSION}.jar
}

functionlist+=(run_app)
function run_app {
java -jar \
-Dspring.profiles.active=${RUN_ENVIRONMENT} \
./mdaservice-${MDA_SERVICE_VERSION}.jar
}

# package
# clean, then package skip tests
# clean & run
functionlist+=(do_test_integration)
function do_test_integration {
mvn clean package -DSERVICE_TEST_ENVIRONMENT=${TEST_INT_ENVIRONMENT} \
\
&& mvn clean && if [ -d target ]; then
echo mvn clean failed; 
else
mvn package -DskipTests; fi \
\
&& pushd mdaservice >/dev/null && if [ -f target/mdaservice-${MDA_SERVICE_VERSION}.jar ]; then
mvn spring-boot:run -Dspring-boot.run.profiles=${TEST_RUN_ENVIRONMENT}; 
else echo packaging jar failed; fi && popd >/dev/null || popd >/dev/null \
&& find . -name mdaservice-${MDA_SERVICE_VERSION}.jar
}

### SPRING BOOT IMAGE
functionlist+=(build_test_image)
function build_test_image {
mvn clean package -DSERVICE_TEST_ENVIRONMENT=${TEST_INT_ENVIRONMENT} \
\
&& mvn clean && if [ -d target ]; then
echo mvn clean failed;
else 
mvn package -DskipTests; fi \
  \
&& pushd mdaservice >/dev/null && if [ -f target/mdaservice-${MDA_SERVICE_VERSION}.jar ]; then
mvn spring-boot:build-image -DSERVICE_TEST_ENVIRONMENT=${TEST_INT_ENVIRONMENT} -Dspring-boot.run.profiles=${TEST_RUN_ENVIRONMENT}; 
else
echo packaging jar failed; fi && popd >/dev/null || popd >/dev/null \
&& find . -name mdaservice-${MDA_SERVICE_VERSION}.jar
}


### INTEGRATION

# clean, then package skip tests
functionlist+=(do_integration)
function do_integration {
init_INT_db
mvn clean && if [ ! -d target ]; then
mvn package -DSERVICE_TEST_ENVIRONMENT=${INT_ENVIRONMENT}; else
echo mvn clean failed; fi
}


# run the application
functionlist+=(do_run)
function do_run {
java -jar \
-Dspring.profiles.active=${RUN_ENVIRONMENT} \
mdaservice/target/mdaservice-${MDA_SERVICE_VERSION}.jar
}

echo aifmda functions:
for i in ${functionlist[@]}; do echo ${i}; done
