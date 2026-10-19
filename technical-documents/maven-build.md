# Maven Build

Maven is the project management tool for `coffee_maker` (the Spring Boot backend). The `mvn` executable handles building, testing, running, and deploying the project, and resolves dependencies automatically. Project details live in the **POM file** (`pom.xml`) at the top of the `coffee_maker` module.

## POM File Sections

**Project Overview** — identifies the project:
- `groupId` — broad-to-specific naming, here `edu.ncsu.csc326` (education → NC State → CSC 326). Becomes the packaged project's namespace.
- `artifactId` — the project artifact's name.
- `version` — the project's version.
- `name` — the project's display name.
- `parent` — the Spring Boot starter parent this project is templated from.

**Dependencies** — libraries the project needs. Maven downloads the corresponding jars from configured repositories into the local `.m2` cache. Each dependency has a `groupId` and `artifactId`, optionally a `version` and a `scope` (e.g. `test`, so test-only libraries aren't packaged into the deployed app).

CoffeeMaker's dependencies deliberately omit `version`. When a version is left off, Maven uses the version the `spring-boot-starter-parent` specifies for that library. The parent pins a set of library versions that Spring has tested together, so upgrading the parent's `version` upgrades the whole dependency set consistently. Declaring your own `version` on a dependency overrides the parent's choice and opts you out of that guarantee, so leave it off unless you need a library the parent doesn't manage.

If a project import shows an error/warning indicator, the dependencies have a problem. First try re-importing/reloading the Maven project in your IDE (IntelliJ: right-click `pom.xml` > Maven > Reload Project; Eclipse: right-click `pom.xml` > Maven > Update Project). If that doesn't resolve it, close your IDE, delete the `.m2` directory under your user home, then restart and reload the project again.
>
To add a new dependency, search for it on the library's site or a Maven repository (Spring's own repository is at [repo.spring.io](https://repo.spring.io)).

**Properties** — project-wide settings, e.g. the Java version target.

**Build** - custom build steps. Maven defines the standard lifecycle phases (clean, compile, test, package, install, deploy), and plugins attach their goals to those phases. CoffeeMaker configures four:

- **`spring-boot-maven-plugin`** binds its `repackage` goal to the `package` phase - the step that turns the plain jar into an executable Spring Boot jar with its dependencies nested inside.
- **`maven-checkstyle-plugin`** binds its `check` goal to the `validate` phase, the first phase of the lifecycle, so every build reports coding style problems before it compiles anything; see [Static Analysis](static-analysis.md) for details.
- **`jacoco-maven-plugin`** measures how much of the code the tests actually execute. It runs three times: `prepare-agent` before the tests, to attach the coverage agent to the test JVM; `report` after the `test` phase, to write the reports under `target/site/jacoco-ut/`; and `check` during `verify`, to enforce the minimums described below.
- **`maven-surefire-plugin`** runs the tests. Its only custom setting is `argLine`, which passes the JaCoCo agent through to the forked test JVM.

That `argLine` is worth understanding, because getting it wrong silently produces a coverage report of zero:

```xml
<argLine>@{surefireArgLine} -XX:+EnableDynamicAgentLoading</argLine>
```

`@{...}` is *late replacement* - Maven expands it when the test JVM is forked, after `jacoco:prepare-agent` has set the `surefireArgLine` property. A plain `${...}` expands too early and comes out empty. Both JVM arguments must also live in a **single** `argLine` element: Maven binds a plugin parameter once, so a second `argLine` silently discards the first and the tests run with no coverage agent attached at all.

**Repositories** — the remote repositories Maven searches to resolve dependencies. CoffeeMaker's POM declares no `repositories` section, so Maven resolves everything from Maven Central, the default. A project only needs this section to pull from somewhere else — for example, Spring Milestones or Spring Snapshots for a pre-release Spring build.

## Test Coverage

`mvn verify` measures coverage for the project as a whole against these minimums:

| Measure | Minimum |
| --- | ---: |
| Line coverage | 60% |
| Branch coverage | 50% |

The check runs during `verify`, so `mvn test` and running the tests from your IDE still produce a coverage report without evaluating the minimums. A shortfall is reported as a build warning and does **not** turn the build red, because the MakeRecipe feature is not covered by any milestone - a team can do everything asked of them and still sit under the bundle minimum. Treat the numbers as the target to work toward, not as a pass/fail line. The check applies to the project as a bundle rather than class by class, so a thoroughly tested class can carry a thinner one.

To see where you stand, open `coffee_maker/target/site/jacoco-ut/index.html` after any `mvn test`. The Actions run summary also prints the numbers on the run page.

## Writing Tests That Share a Database

Every test class in this project talks to the same long-lived `coffee_maker_test` database. There is no separate database per test class, and nothing cleans up between them. One rule keeps that workable:

> **A test must never commit.**

Annotate `@SpringBootTest` test classes with `@Transactional`, and rely on `@DataJpaTest` being transactional already. Spring rolls the transaction back when the test method ends, so nothing a test writes is ever visible to the next test or to the next run.

Two things break the rule, both easy to do by accident:

- **`TRUNCATE TABLE`, `ALTER TABLE`, `CREATE`, `DROP`.** These are DDL, and in MySQL every DDL statement forces an *implicit commit*. Issuing one from inside a test ends the transaction the test was relying on, and everything before it is committed for good. Empty a table with `repository.deleteAll()` or a JPQL `DELETE FROM Entity` instead - both stay inside the transaction, and both roll back.
- **Depending on a generated id.** The `AUTO_INCREMENT` counter is not transactional: it moves forward on every insert and never rolls back. A test that assumes a row will have id `1` passes against an empty database and fails on the next run. Look rows up by a value you control, or by the id the object was actually assigned.

Assertions have a matching trap. `save()` hands back the same in-memory object you passed it, so asserting on that object only re-checks the fields you just set - the test passes even if nothing reached the database. Call `flush()` and `clear()` on the `EntityManager`, read the row back, and assert on what came out.


## Resources

- [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
- [Introduction to the Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
- [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/maven-plugin/)
- [Building Java Projects with Maven](https://spring.io/guides/gs/maven/)
