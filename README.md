# Challenge Spotify Backend

This is a Spring Boot application that integrates with the Spotify API.

## Getting Started

### Running the Application

To run the application, use the following Gradle command:

```bash
./gradle startApp
```

This will start the Spring Boot application, typically accessible on `http://localhost:8080`.

### Configuration

Application settings can be found and modified in `src/main/resources/application.properties`. Key configurations include:

*   **Database Properties**: Configure your PostgreSQL database connection.
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    spring.jpa.properties.hibernate.default_schema=challenge_spotify
    ```

*   **Spring Security**: Default user credentials for basic authentication.
    ```properties
    spring.security.user.name=admin
    spring.security.user.password=password
    ```

*   **Spotify API Credentials**: You must obtain your own client ID and client secret from the Spotify Developer Dashboard and update these properties.
    ```properties
    spotify.client-id=YOUR_SPOTIFY_CLIENT_ID
    spotify.client-secret=YOUR_SPOTIFY_CLIENT_SECRET
    ```

*   **Server Port**: Change the application's listening port.
    ```properties
    server.port=8080
    ```

After making changes to `application.properties`, you will need to restart the application for the changes to take effect.
