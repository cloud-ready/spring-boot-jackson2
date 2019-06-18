# spring-boot-jackson2

[![Sonar](https://sonarcloud.io/api/project_badges/measure?project=top.infra%3Aspring-boot-jackson2&metric=alert_status)sonarcloud](https://sonarcloud.io/dashboard?id=top.infra%3Aspring-boot-jackson2)  

[Maven Site release (github.io)](https://cloud-ready.github.io/cloud-ready/release/build-docker/cloud-ready-parent/spring-boot-jackson2/index.html)  
[Maven Site snapshot (infra.top)](https://cloud-ready.github.io/cloud-ready/snapshot/build-docker/cloud-ready-parent/spring-boot-jackson2/index.html)  

[Artifacts (release)](https://oss.sonatype.org/content/repositories/releases/top/infra/spring-boot-jackson2/)  
[Artifacts (snapshot)](https://oss.sonatype.org/content/repositories/snapshots/top/infra/spring-boot-jackson2/)  

[Source Repository (github)](https://github.com/cloud-ready/spring-boot-jackson2/tree/develop)  
[Source Repository (gitlab)](https://gitlab.com/gitlab-cloud-ready/spring-boot-jackson2/tree/develop)  

[![pipeline status](https://gitlab.com/gitlab-cloud-ready/spring-boot-jackson2/badges/develop/pipeline.svg)gitlab-ci](https://gitlab.com/gitlab-cloud-ready/spring-boot-jackson2/pipelines)  
[![Build Status](https://travis-ci.org/cloud-ready/spring-boot-jackson2.svg?branch=develop)travis-ci](https://travis-ci.org/cloud-ready/spring-boot-jackson2)  


spring-boot-jackson2

Jackson2 customizers and extensions for spring-boot applications.


## spring-boot-jackson2-apache-commons

Serializer and Deserializer of org.apache.commons:commons-lang3
 `org.apache.commons.lang3.tuple.Pair`.


## spring-boot-jackson2-core

com.fasterxml.jackson.datatype:jackson-datatype-guava

com.google.guava:guava

com.fasterxml.jackson.datatype:jackson-datatype-jdk8

com.fasterxml.jackson.datatype:jackson-datatype-jsr310

com.fasterxml.jackson.module:jackson-module-parameter-names (optional)

org.springframework:spring-web


`top.infra.jackson2.Jackson2Utils` can customize ObjectMapper instance just like spring-boot's JacksonAutoConfiguration does.


```yaml
# see: spring-boot-jackson2-jaxb
spring.jackson.jaxb.enabled: false
```

## spring-boot-jackson2-hal

org.springframework.hateoas:spring-hateoas
 `org.springframework.hateoas.hal.Jackson2HalModule`


## spring-boot-jackson2-jaxb

com.fasterxml.jackson.module:jackson-module-jaxb-annotations
 `com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule`

org.springframework:spring-oxm


## spring-boot-jackson2-joda

Set up JacksonJodaDateFormat for DateTime of joda-time

com.fasterxml.jackson.datatype:jackson-datatype-joda

joda-time:joda-time

## Build this project

```bash
time ./mvnw -e -ntp spotbugs:spotbugs spotbugs:check pmd:pmd pmd:check
time JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home" ./mvnw -e -ntp clean package
time JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-11.0.2.jdk/Contents/Home" ./mvnw -e -ntp -Djava.version=11 clean package
```
