# New Kitchen Sink
This application represents a migration of the kitchensink project under jboss git repositories
## Table of Contents
- [General](#General)
- [Installation](#installation)


## General
This applications is intended to replace the kitchensink project under https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink.
It uses Java 21 and replaces Jakarta EE with Spring Boot 3.3.4 (with all Spring 6 dependencies)
As such related to the original app it presents the following updates among others:
- Spring rest controllers as opposed to EE controllers
- For db it uses an in memory h2 database which is accessed via Spring Data Jpa
- A separation between transport object (MemberDTO) and actual persistence entity (Member), a conversion via model mapper bridges between the 2
- Validation performed via Spring validation, based itself on jakarta EE annotations
- Exception handling done via Controller Advice
- event handling done async way via ApplicationEvent and @Listener annotation
- default db population done based on property via a @PostConstruct
- transaction mgmt activated on create and update operations (even if for now the transaction contains only 1 dml)
- filter checking the "Authorization" header and trying to do the standard permission check, as no authn server is in place, no actual check happens but the filter is in place and checks at least for the presence of the token
- added Correlation interceptor, that for now creates(if not already existing) a new correlation token before reaching endpoint and binds it to a thread local for future use(for example logging and tracking)
- Spring integration tests

