# Studieadministrasjon

API for administrasjon av studenter og emner i kunnskapssektoren.

## Teknologi

- **Backend:** Java 24, Spring Boot, Spring for GraphQL
- **Database:** PostgreSQL
- **Build:** Maven
- **CI/CD:** GitHub Actions
- **Container:** Docker

## Komme i gang

_(Kommer snart - applikasjonen er under utvikling)_

## Utviklingsmiljø

### Krav
- Java 24+
- Maven 3.9+
- Docker Desktop
- PostgreSQL 16+

### Kjør lokalt

```bash
# Clone repository
git clone https://github.com/ishfaqkhan80/studieadministrasjon.git
cd studieadministrasjon

# Bygg prosjektet
mvn clean install

# Kjør applikasjonen
mvn spring-boot:run
```

## Testing

```bash
# Kjør alle tester
mvn test

# Kjør med coverage
mvn verify
```

## CI/CD Pipeline

Prosjektet bruker GitHub Actions for automatisk bygg og testing.

Pipeline kjører automatisk ved:
- Push til `main`
- Pull requests

## Lisens

Dette er et læringsprosjekt.
