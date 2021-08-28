For projects with roots in the Java ecosystem the combination if [Liquibase](https://www.liquibase.org/) and [Jooq](https://www.jooq.org/) is a popular approach on creating and evolving database schemas, as well as interacting with the created data afterwards. 

While Liquibase gives you a powerful language to define your schema in a database vendor agnostic way:

<!-- snippet:liquibase_example_user -->
<!-- /snippet:liquibase_example_user -->

JooQ provides a way to work with the generated database in a type safe manner, providing and DSL based on the database structure itself:

<!-- snippet:jooq_example_user -->
<!-- /snippet:jooq_example_user -->

To provide the database specific DSL, JooQ runs a code generator that generates all needed classes based on the tables, columns, indices and foreign keys in an already populated and running database. 

This means, that during the build process we first need to start a database, then execute Liquibase and let it create all tables, columns and indices and finally let Jooq do it's magic by running the code generator against this database. Automating this tends to be kind of tedious because you need to orchestrate all those step to ensure everything works as intended.

While some of that manual work is already covered by the [JooQ plugin for Gradle](https://github.com/etiennestuder/gradle-jooq-plugin) thanks to a new [extension](https://www.jooq.org/doc/latest/manual/code-generation/codegen-liquibase/) from the JooQ Team we can now configure the plugin in a way, that it not only runs the code generator, but also instantiates an in-memory SQL database and populates it using the Liquibase schema beforehand. 

Assuming you are already using the JooQ Gradle plugin, the first step is to add the needed dependencies for the JooQ extension to the `jooqGenerator`classpath which is provided by the plugin:

<!-- snippet:gradle_jooq_extension_dependency -->
<!-- /snippet:gradle_jooq_extension_dependency -->

Here especially the `slf4j-jdk14` dependency is crucial because otherwise the process becomes hard to debug in case something fails. 

Next step is to configure the plugin:

<!-- snippet:gradle_jooq_extension_configuration -->
<!-- /snippet:gradle_jooq_extension_configuration -->

the important part here is the `org.jooq.meta.extensions.liquibase.LiquibaseDatabase` setting, which instructs the plugin to use an in-memory database for the code generation part.

Now by calling:

<!-- snippet:gradle_jooq_run -->
<!-- /snippet:gradle_jooq_run -->

we can start the database creation and DSL generation process, without the need of complicated (and especially in a CI error prone) database setup.


```
./gradlew generateJooq

> Task :todo-backend-spring-boot-jooq:generateJooq
[...]
SLF4J: Actual binding is of type [org.slf4j.impl.JDK14LoggerFactory]
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Successfully acquired change log lock
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Creating database history table with name: PUBLIC.DATABASECHANGELOG
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Reading from PUBLIC.DATABASECHANGELOG
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Table users created
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Table users_tokens created
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Table lists created
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Table lists_items created
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: ChangeSet src/main/resources/db/changelog/db.changelog-master.yaml::1::pelle ran successfully in 19ms
Aug 28, 2021 8:57:47 PM liquibase.logging.core.Slf4jLogger info
INFO: Successfully released change log lock
```