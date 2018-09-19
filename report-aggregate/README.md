# report-aggregate

see: [stackoverflow aggregate-findbugs-report-in-maven-3-0-5](https://stackoverflow.com/questions/34829200/aggregate-findbugs-report-in-maven-3-0-5)  

Generate site
```bash
# site:stage is invalid for github site
mvn -U -Dgithub-site-publish=false -Dinfrastructure=opensource -Djacoco=true -Dsite=true clean package site site:stage
```
