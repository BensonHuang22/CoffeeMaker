# CoffeeMaker

## Resources

  * [Environment Installation Instructions](https://courses.csc.ncsu.edu/csc326/install/)
  * [CoffeeMaker Setup Instructions](https://courses.csc.ncsu.edu/csc326/onboarding/setup/)
  * [Onboarding Project Instructions](https://courses.csc.ncsu.edu/csc326/onboarding/)

## CoffeeMaker Technical Documentation

  * [CoffeeMaker Requirements](technical-documents/requirements)
  * [CoffeeMaker Architecture & Design](technical-documents/architecture-design)
  * [System Tests](technical-documents/system-tests)

## Technical Process
  * [GitHub Process](technical-documents/github-process)
  * [Static Analysis](technical-documents/static-analysis)
  * [Maven Build](technical-documents/maven-build.md)

## Project Management

  * [Project Management Documents](project-management)

## Retrospectives

  * [Retros](retros)

## GitHub Pages
You can view your project's documentation through a private GitHub pages site.  To find the site URL (which is automatically generated), go to your repo in github.com and select the Actions tab.  Select the last build named "pages build and deployment" and click the link under the deploy action.  Add the link to your README.md file for easy access.
  
## `coffee_maker` Backend Quick Start

1. Open IntelliJ.  In IntelliJ, open your repository folder.

2. Right click on `coffee_maker/pom.xml` and select **Add as Maven Project**.
	 
3. Maven Project Reload. Right click on `pom.xml` and select **Maven > Sync Project**.

4. Make sure MySQL is running.  There is nothing to configure: `application.properties` defaults to a local MySQL as `root` with no password, and the `coffee_maker` and `coffee_maker_test` schemas are created on first use.
     * If your MySQL uses a different host, port, user, or password, copy `coffee_maker/.env.template` to `coffee_maker/.env` and edit it.  `.env` is gitignored, so your settings stay on your machine.
     * Do not edit `application.properties`.  That file is committed, so editing it would push your local settings to the whole team.

5. Open `coffee_maker/src/main/java`.  Right click on `CoffeeMakerApplication` and select **Run CoffeeMakerApplication.main()**.


## `coffee_maker_frontend` Frontend Quick Start

1. Open a Terminal and change directory to `~/git/<repo_name>/coffee_maker_frontend`

2. Update libraries. Run `npm install`

3. Run the application. Run `npm run dev`
     * The frontend expects the backend at `http://localhost:8080`.  To point it somewhere else, create `coffee_maker_frontend/.env.local` containing `VITE_API_BASE_URL=http://localhost:9090`.  Vite reads `.env.local`, and `*.local` is already gitignored - that is why the frontend uses that name rather than the backend's `.env`.


4. Browse to http://localhost:3000/

