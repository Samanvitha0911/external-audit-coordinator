# Fix UserService Compilation Error

## Plan:
1. [x] Create `entity/User.java` with JPA annotations, getters/setters
2. [x] Create `repository/UserRepository.java` extending JpaRepository
3. [x] Create `service/UserService.java` with `@Service` and `findByUsername`
4. [x] Fix `controller/AuthController.java` - inject UserService via constructor
5. [x] Create `db/migration/V2__add_users_table.sql` for Flyway
6. [x] Run `mvn clean compile` to verify
7. [ ] Run `mvn spring-boot:run` to verify app starts


