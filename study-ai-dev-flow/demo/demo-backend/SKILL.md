# Demo Backend Project Skills

## Architecture
- Spring Boot 3.3 with Java 21
- Spring Data JPA with H2 database
- Layered architecture: Controller → Service → Repository

## Code Conventions

### Controller Layer
- Use `@RestController` annotation
- Use `@RequestMapping("/api/xxx")` for base path
- Return `ResponseEntity<T>` with proper status codes
- Handle exceptions using `@ExceptionHandler`

### Service Layer
- Use `@Service` annotation
- Use `@RequiredArgsConstructor` for dependency injection
- Implement business logic in service classes

### Entity Layer
- Use `@Entity` annotation
- Use Lombok `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- Use `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)` for primary keys

### Repository Layer
- Extend `JpaRepository<T, ID>`
- Define custom query methods using Spring Data JPA naming conventions

### DTO Layer
- Use Lombok `@Data`
- Separate request and response DTOs
- Use validation annotations (`@NotNull`, `@Size`, etc.)

## API Standards
- Use RESTful design patterns
- Use appropriate HTTP methods: GET, POST, PUT, DELETE
- Return JSON responses
- Use consistent error handling

## Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Lombok
- H2 Database (runtime)

## Testing
- Use Spring Boot Test
- Use Mockito for mocking
- Write unit tests for services
- Write integration tests for controllers