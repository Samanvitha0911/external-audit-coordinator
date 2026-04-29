# ══════════════════════════════════════════════
#  Backend Dockerfile — Spring Boot 3.x / Java 17
#  Multi-stage build: Maven build → slim runtime
# ══════════════════════════════════════════════

# ── Stage 1: Build ─────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

# Cache dependencies layer first
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Build the fat JAR (skip tests in Docker build)
COPY src/ src/
RUN ./mvnw package -DskipTests -q

# ── Stage 2: Runtime ───────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
