# Installation

## 1. Prerequisites

- **JDK 21** installed and available on the system PATH. Verify with:
  ```bash
  java -version
  ```
- The Gradle wrapper (`./gradlew`) is included, so no separate Gradle installation is required.

## 2. Get the Code

Clone the project repository (repository URL not provided in this documentation).
```bash
git clone <repository-url>
cd larch
```

## 3. Install Dependencies

Resolve all dependencies and compile the project without running tests:
```bash
./gradlew build -x test
```

## 4. Environment Variables

The project includes an example environment file (`./.env.example`). Copy it to create your own local environment file:
```bash
cp .env.example .env
```
Edit `.env` if you need to change the `PORT` value (defaults to `8080`).

## 5. Run Development Server

Start the application in development mode:
```bash
./gradlew run
```
The server will start and listen on the port defined by `PORT` (default `http://localhost:8080`).

## 6. Run Tests

Execute the test suite:
```bash
./gradlew test
```

## 7. Production Build

Create an optimized production build:
```bash
./gradlew build
```
The built artefacts will be placed in `build/` (exact location depends on Gradle configuration; typically a JAR in `build/libs/` or an application distribution in `build/install/`).

## 8. Troubleshooting

- **Port already in use**: Change the `PORT` variable in your `.env` file or environment to a free port.
- **Java version mismatch**: Ensure JDK 21 is used. The build is configured with `jvmToolchain(21)`. If multiple JDKs are installed, set `JAVA_HOME` appropriately.
- **Gradle wrapper fails**: Try making `gradlew` executable with `chmod +x gradlew`.