@echo off
title MDA Service


java -jar \
   -Dspring.profiles.active=QA1 \
   mdaservice\target\mdaservice-{$VERSION}.jar
