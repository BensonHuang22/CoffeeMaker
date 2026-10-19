# CoffeeMaker Architecture

Software architecture is the structure or structures of a system — the software components, their externally visible properties, and the relationships among them. Architecture gives a high-level overview of a complex system; CoffeeMaker follows a **layered architecture**.

Design, specifically low-level design (LLD), represents the object-level view of a system. Rather than modeling the entire LLD of the system at once, model each layer, or a slice of functionality through all layers.

## Layers

CoffeeMaker is implemented using the Spring Framework. The standard Spring architecture consists of three layers, which map to the following CoffeeMaker packages:

- **Repository Layer** — interfaces to the data store and their implementations.
  - `edu.ncsu.csc326.coffee_maker.entity`
  - `edu.ncsu.csc326.coffee_maker.repositories`
- **Service Layer** — business logic, including application services and infrastructure services.
  - `edu.ncsu.csc326.coffee_maker.services`
- **Web Layer** — controllers, exception handlers, and templates used to generate what users see.
  - `edu.ncsu.csc326.coffee_maker.controllers`

Data moves between layers via Data Transfer Objects (DTOs), and is persisted via Data Access Objects (DAOs). `*Mapper` classes convert between DTOs and DAOs as data crosses layer boundaries (e.g., `RecipeMapper.mapToRecipe()` / `RecipeMapper.mapToRecipeDto()`).

## References

- Len Bass, Paul Clements, Rick Kazman, *Software Architecture in Practice*, Addison Wesley, 2nd edition, 2003.
- [Understanding Spring Web Application Architecture](https://www.petrikainulainen.net/software-development/design/understanding-spring-web-application-architecture-the-classic-way/)
