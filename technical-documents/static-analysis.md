# Static Analysis

Static analysis tools read your source code without running it and report problems they find in the text and structure: missing documentation, inconsistent naming, spacing problems, unused variables, and a handful of common bug patterns. They don't judge whether your logic is correct — that's what tests are for — but they catch a category of mistakes cheaply and consistently.

Style matters on a team project because code is read far more often than it's written. A consistent style means a reviewer spends their attention on what the code *does* rather than on how it's formatted, and a tool enforces that consistently instead of leaving it to whoever reviews the pull request.

CoffeeMaker has two codebases and one tool for each:

| Codebase | Tool | Run with |
|---|---|---|
| `coffee_maker` (Java backend) | [Checkstyle](#checkstyle-the-java-backend) | `./mvnw checkstyle:check` |
| `coffee_maker_frontend` (React frontend) | [ESLint](#eslint-the-react-frontend) | `npm run lint` |

Both run automatically in [GitHub Actions](#static-analysis-in-github-actions) on every push.

---

# Checkstyle 

Checkstyle checks Java source against a coding style. It reads the code as text and structure and reports where the style rules aren't followed: missing Javadoc, inconsistent naming, spacing problems, and common bug patterns like comparing strings with `==`.

## Checkstyle Configuration

The rules live in [`coffee_maker/config/checkstyle/csc_checkstyle.xml`](../coffee_maker/config/checkstyle/csc_checkstyle.xml). This is the CSC 216/217 style you've used in earlier courses, so most of it should look familiar. Everything in it is reported at severity `warning`.

The file sits inside the `coffee_maker` module because Checkstyle only applies to the Java backend.

Broadly, the configuration checks:

- **Javadoc** — every class, method, and field needs documentation, and class Javadoc needs an `@author` tag. `@param`/`@return`/`@throws` tags must match the method they document and must actually say something.
- **Naming** — classes, methods, fields, constants, parameters, and packages follow standard Java naming conventions.
- **Imports** — no star imports, no redundant imports, no unused imports.
- **Whitespace** — spacing around operators, after commas and semicolons, and around braces.
- **Common bug patterns** — empty catch blocks, `==` on strings, assignment inside a condition, `equals()` without `hashCode()`, a `switch` with no `default`, reassigning a parameter, and leftover `TODO:`/`FIX ME` comments.

## Running Checkstyle with Maven

From a terminal in the `coffee_maker` directory:

```
./mvnw checkstyle:check        # report problems to the console
./mvnw checkstyle:checkstyle   # generate target/reports/checkstyle.html
```

The `check` goal is also bound to Maven's `validate` phase, which is the first phase of the build lifecycle. That means Checkstyle runs automatically as part of `./mvnw test`, `./mvnw package`, and `./mvnw verify` — you'll see the warnings in the build output without asking for them.

Violations are reported but **do not fail the build**. See [Making violations fail the build](#making-violations-fail-the-build) below if you want a stricter setting.

## Running Checkstyle in IntelliJ

There are two ways, and they're useful for different things.

### The Checkstyle run configuration (no setup)

The repository ships an IntelliJ run configuration named **Checkstyle**. Open the project in IntelliJ and pick it from the run configuration dropdown in the right portion of the top toolbar, then click Run. It executes `mvn checkstyle:check` against the backend and prints the results in the Run window.

### The CheckStyle-IDEA plugin (inline highlighting)

The plugin checks your code as you type and underlines problems in the editor, which is a much tighter feedback loop than re-running a build. It has to be installed once per machine:

1. **File > Settings > Plugins** (macOS: **IntelliJ IDEA > Settings > Plugins**).
2. Search the Marketplace for **CheckStyle-IDEA** and install it, then restart IntelliJ when prompted.
3. Go to **File > Settings > Tools > Checkstyle**.
4. Set **Checkstyle version** to the newest version offered (the build uses 13.10.0).
5. Set **Scan scope** to *Only Java sources (including tests)*.
6. Under **Configuration File**, click **+**, choose **Use a local Checkstyle file**, and browse to `coffee_maker/config/checkstyle/csc_checkstyle.xml` inside your repository. Give it a description such as `CSC 326 Checks` and finish the wizard.
7. Back in the list, tick the checkbox next to the configuration you just added to make it the active one, then **Apply**.

You'll now see Checkstyle warnings inline as you edit, and a **CheckStyle** tool window at the bottom of the IDE for scanning a file, a module, or the whole project on demand.

> IntelliJ stores these settings in the `.idea` directory, which is **not** committed to the repository — every teammate configures the plugin on their own machine.

## Reading the Checkstyle output

A violation looks like this:

```
src\main\java\edu\ncsu\csc326\coffee_maker\dto\RecipeDto.java:[7,1] (javadoc) JavadocType: Type Javadoc comment is missing @author tag.
```

That's the file, the `[line, column]`, the category, the rule name, and what's wrong. The rule name is worth noting — searching the [Checkstyle checks documentation](https://checkstyle.org/checks.html) for it explains what the rule wants and why.

Expect violations on a fresh clone. The starter code carries a number of them, and most are missing `@author` tags — the class Javadoc is there, but the author is you, and the base project can't know that. Fill in your own name as you work on each file.

## Making violations fail the build

By default the build reports style problems and continues, so a Checkstyle warning never blocks a build or turns a pull request red. To make violations a hard failure instead, change `failOnViolation` to `true` in the `maven-checkstyle-plugin` section of `coffee_maker/pom.xml`:

```xml
<failOnViolation>true</failOnViolation>
```

Combined with `<violationSeverity>warning</violationSeverity>`, which is already set, this fails the build on *any* violation, locally and in GitHub Actions alike. Clean up the existing violations before flipping this on, or nothing will build.

---

# ESLint 

ESLint is the equivalent tool for JavaScript and JSX. Because JavaScript has no compiler to catch a misspelled variable name or an unreachable branch, ESLint does more of that work than Checkstyle does — a typo that would be a compile error in Java is a lint error here, and often the only thing standing between you and a blank page in the browser.

## The configuration file

The rules live in [`coffee_maker_frontend/eslint.config.js`](../coffee_maker_frontend/eslint.config.js). This is ESLint's newer "flat config" format: the file exports an array of configuration objects, and each one says which files it applies to and which rules to turn on.

The configuration layers four sets of rules:

- **`@eslint/js` recommended** — the core JavaScript checks: undefined variables (`no-undef`), unused variables, unreachable code, duplicate object keys, `case` blocks that fall through.
- **`eslint-plugin-react`** — React-specific correctness: hooks and components used properly, `key` props on list items, valid prop types. The `jsx-runtime` rules turn off the older requirement that every JSX file import `React`.
- **`eslint-plugin-react-hooks`** — the [Rules of Hooks](https://react.dev/reference/rules/rules-of-hooks): hooks are called at the top level of a component, never inside a condition or loop, and `useEffect` dependency arrays list everything the effect actually uses. These catch real bugs that are hard to see by reading.
- **`eslint-plugin-react-refresh`** — warns when a file's exports would break Vite's hot reloading during `npm run dev`.

Two rules are deliberately relaxed at the bottom of the file:

```js
'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
'no-unused-vars': ['warn', { varsIgnorePattern: '^[A-Z_]' }],
```

An unused variable is usually work in progress rather than a defect, so it's reported as a **warning**. An undefined name is a real bug, so it stays an **error**. The `dist` directory (build output) is ignored entirely.

## Running ESLint from the terminal

From a terminal in the `coffee_maker_frontend` directory:

```
npm install          # once, to install ESLint and the plugins
npm run lint         # report problems to the console
```

`npm run lint` is a shortcut for `eslint .`, which lints every `.js` and `.jsx` file under the directory. Two other invocations are worth knowing:

```
npx eslint . --fix                       # fix what can be fixed mechanically
npx eslint . -f html -o eslint-report.html   # write an HTML report
```

`--fix` repairs formatting-level problems (quoting, semicolons, spacing) on its own and leaves anything requiring a judgment call for you. Commit before running it so you can see exactly what it changed.

Unlike the Maven build, ESLint is **not** wired into `npm run dev` or `npm run build`. Vite will build code that ESLint objects to. Run `npm run lint` yourself, or rely on the GitHub Actions job below.

## Running ESLint in your editor

Both editors used in this course have an ESLint integration that highlights problems inline as you type, which is a much tighter feedback loop than re-running the command:

- **VS Code** — install the **ESLint** extension (`dbaeumer.vscode-eslint`) from the Marketplace. Open the `coffee_maker_frontend` folder (the extension finds `eslint.config.js` from the folder you open) and problems appear underlined in the editor and listed in the **Problems** panel.
- **IntelliJ IDEA** — go to **File > Settings > Languages & Frameworks > JavaScript > Code Quality Tools > ESLint** and choose **Automatic ESLint configuration**. IntelliJ picks up the project's ESLint installation from `node_modules` and the flat config file on its own.

Either way, run `npm install` in `coffee_maker_frontend` first; both integrations use the ESLint that lives in the project's `node_modules`.

## Reading the ESLint output

A run looks like this:

```
C:\...\coffee_maker_frontend\src\components\MakeRecipeComponent.jsx
  29:3  error  'e' is not defined  no-undef

✖ 1 problem (1 error, 0 warnings)
```

That's the file, the `line:column`, the severity, what's wrong, and the rule name. Look up rule names in the [ESLint rules reference](https://eslint.org/docs/latest/rules/) (or the [React Hooks rules](https://react.dev/reference/rules/rules-of-hooks) for `react-hooks/*`) to see what the rule wants and why.

Expect problems on a fresh clone here too. The starter frontend contains at least one genuine `no-undef` error, and it is a real bug that you'll fix in Milestone 0.

## Making lint problems fail the build

`eslint .` already exits with a non-zero status when there are **errors**, which is what a shell or a CI job checks. Warnings alone exit zero. Two ways to tighten that:

- Treat warnings as failures too: `npx eslint . --max-warnings 0`. Change the `lint` script in `coffee_maker_frontend/package.json` if you want that to be the default.
- Change a rule's severity in `eslint.config.js` from `'warn'` to `'error'`.

In GitHub Actions the lint step is currently marked `continue-on-error`, so problems are reported without turning the run red — see below for how to change that.

---

# Static analysis in GitHub Actions

The workflow in [`.github/workflows/build.yml`](../.github/workflows/build.yml) runs on every push to any branch, and on demand from the Actions tab using **Run workflow**.  There is no separate pull request trigger - a pull request shows the result of the build that already ran on its latest commit, so the same commit is never built twice. It has two jobs that run in parallel:

**`Build backend`** builds the Java backend with `./mvnw -B verify`, running the tests against a throwaway MySQL container that the runner starts for the job and removes afterwards. Because Checkstyle is bound to `validate` in the POM, that step reports style problems in the job log automatically. It then writes the test coverage numbers onto the run summary page, and uploads the test results, the JaCoCo coverage report, and the HTML Checkstyle report together as an artifact named `backend-reports`.

**`Lint frontend`** installs the frontend dependencies with `npm ci` and runs `npm run lint`, printing problems in the job log. It then generates an HTML report with ESLint's `html` formatter and uploads it as an artifact named `eslint-report`.

To read either report: open the **Actions** tab on GitHub, click the workflow run, then download the artifact from the summary page and open the `.html` file in a browser.

Both jobs run on the course's self-hosted runners rather than GitHub's hosted ones:

```yaml
    runs-on: [ campus ]
```

Any job you add to this workflow — or to a new workflow of your own — needs that same `runs-on` label. A job left on `ubuntu-latest` will sit in the queue and never start.

Neither job turns the run red on a violation. Checkstyle is non-blocking because of `failOnViolation` in the POM; ESLint is non-blocking because its step carries `continue-on-error: true`. Removing that line from the `Run ESLint` step is enough to make lint errors fail the workflow:

```yaml
      - name: Run ESLint
        continue-on-error: true    # <- delete this line to make lint problems fail the run
        run: npm run lint
```

Fix the existing problems before doing that, or every pull request will be red.

# Resources

**Checkstyle**

- [Checkstyle](https://checkstyle.org/)
- [Checkstyle checks reference](https://checkstyle.org/checks.html)
- [Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)
- [CheckStyle-IDEA plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea)

**ESLint**

- [ESLint](https://eslint.org/)
- [ESLint rules reference](https://eslint.org/docs/latest/rules/)
- [Configuring ESLint (flat config)](https://eslint.org/docs/latest/use/configure/)
- [Rules of Hooks](https://react.dev/reference/rules/rules-of-hooks)
- [ESLint extension for VS Code](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)
