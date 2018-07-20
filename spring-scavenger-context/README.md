# Spring Scavenger Context

## Download
```
<dependencies>
    <dependency>
        <groupId>com.github.styner9</groupId>
        <artifactId>spring-scavenger-context</artifactId>
        <version>{VERSION}</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>bintray-styner9-utils</id>
        <url>https://dl.bintray.com/styner9/utils</url>
    </repository>
</repositories>
```

## Features

### `DeployPhase`
Use spring profiles more safely.

#### Configuration
in `application.yml` (or properties)
```yaml
spring-scavenger.context.deploy-phase:
  # default true
  enabled: true 
  
  # phase profile names: mandatory, if enabled
  candidates: dev,test,prod

  # default(or fallback) profile for missing target profile
  default: dev

  # if target profile is missing, whether throws exception or not: default false
  fail-on-missing: false
```
