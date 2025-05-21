# Digital Assistant

This application allows users to create a digital assistant with a name and a predefined response. Users can then interact with these assistants.

## Application Features

*   **Create and Update Assistants**:
    *   Define or modify assistants by providing a name and a specific response message.
    *   Use the `PUT /assistants/{name}` endpoint.
    *   Request body example: `{"response": "This is the assistant's unique response."}`
*   **Enhanced Chat Interaction**:
    *   Engage with an assistant using the `POST /assistants/{name}/chat` endpoint.
    *   Send your message in the request body: `{"message": "Your input message."}`
    *   The assistant will now reply by echoing your input, formatted as: `"You said: {Your input message.}"`.

## Application Configuration

This section details how to configure the Digital Assistant, particularly its data storage mechanism.

### Data Storage Backend

The application supports two primary methods for storing assistant data:

1.  **In-Memory Storage**:
    *   This is the **default** configuration.
    *   Assistant data is stored within the application's memory.
    *   **Important**: All data is lost when the application is stopped or restarted.
    *   Suitable for quick testing, development sprints, or when data persistence is not a requirement.

2.  **Redis Storage**:
    *   Uses an external Redis server to persist assistant data.
    *   Ensures that data survives application restarts and provides a more robust storage solution for production environments.
    *   Requires a running Redis instance to be accessible by the application.

### Activating a Data Storage Backend via Spring Profiles

The selection of the data storage backend is managed through Spring Profiles. The application includes two key profiles:

*   `in-memory`: Activates the in-memory storage. This profile is **active by default** if no other profile is specified.
*   `redis`: Activates Redis as the data storage backend.

**Methods to Activate a Profile (e.g., to activate `redis`):**

The `spring.profiles.active` property controls profile selection. It can be set in various ways:

1.  **Via Environment Variable (Recommended for operational flexibility):**
    Set the `SPRING_PROFILES_ACTIVE` environment variable before launching the application.
    ```bash
    SPRING_PROFILES_ACTIVE=redis java -jar build/libs/digital-assistant-service-0.0.1-SNAPSHOT.jar
    ```
    *(Note: The JAR file name and path might vary based on your build configuration. Typically found in `build/libs/` or `target/` after running `./gradlew build`)*

2.  **Via Java System Property:**
    Pass the property as a `-D` argument directly to the `java` command.
    ```bash
    java -Dspring.profiles.active=redis -jar build/libs/digital-assistant-service-0.0.1-SNAPSHOT.jar
    ```

3.  **In `application.properties` (Less flexible for environment-specific setups):**
    You can specify `spring.profiles.active=redis` within the `src/main/resources/application.properties` file. However, for managing different environments (dev, staging, prod), environment variables or system properties offer better control.

### Redis Configuration Details (Applicable when `redis` profile is active)

When the `redis` profile is selected, the application must be able to connect to your Redis instance.

*   **Configuration File (`application-redis.properties`):**
    Connection details can be specified in `src/main/resources/application-redis.properties`. This file is loaded when the `redis` profile is active.
    Example:
    ```properties
    spring.data.redis.host=localhost
    spring.data.redis.port=6379
    # spring.data.redis.password=your_redis_password (if your Redis server requires authentication)
    ```
    If this file is not present, or if properties are omitted, Spring Boot will use default Redis settings (host: `localhost`, port: `6379`, no password).

*   **Environment Variables (Override file and default settings):**
    For maximum flexibility, especially in containerized deployments (like Kubernetes), you can set Redis connection details using environment variables. Spring Boot automatically maps these:
    *   `SPRING_DATA_REDIS_HOST` (e.g., `my-custom-redis-host`)
    *   `SPRING_DATA_REDIS_PORT` (e.g., `6380`)
    *   `SPRING_DATA_REDIS_PASSWORD` (e.g., `s3cr3t`)

    If the `redis` profile is active but the application fails to connect to Redis (e.g., due to incorrect configuration, network issues, or Redis server downtime), the application will likely encounter errors and may not start or function correctly.

## Running the Digital Assistant locally
### Pre-requisites
- JDK 21

### Common Steps for Local Setup
1.  Clone the repository:
    ```bash
    git clone git@github.com:muralov/digital-assistant-service.git
    ```
2.  Navigate into the project directory:
    ```bash
    cd digital-assistant-service
    ```
3.  Build the application (this also creates the executable JAR):
    ```bash
    ./gradlew clean build
    ```

### Steps to Run the Digital Assistant Locally

1.  **Run the application:**
    *   **Using In-Memory Storage (Default Profile):**
        Refer to the "Application Configuration" section for how to manage profiles.
        ```bash
        java -jar build/libs/digital-assistant-service-0.0.1-SNAPSHOT.jar
        ```
        Alternatively, with the Spring Boot Gradle plugin:
        ```bash
        ./gradlew bootRun
        ```
    *   **Using Redis Storage:**
        Ensure your Redis server is running and accessible. Activate the `redis` profile as described in "Application Configuration". Example:
        ```bash
        SPRING_PROFILES_ACTIVE=redis java -jar build/libs/digital-assistant-service-0.0.1-SNAPSHOT.jar
        ```

2.  **Create or Update a Digital Assistant:**
    Use cURL to send a PUT request. Example for an assistant named "my-assistant":
```bash
curl -v -H "Content-Type: application/json" -X PUT \
    -d '{"response":"Hello, I am your helpful local assistant!"}' \
    "localhost:8080/assistants/my-assistant"
```

3.  **Chat with the Digital Assistant:**
    Send a POST request to the assistant's chat endpoint:
    ```bash
    curl -v -H "Content-Type: application/json" -X POST \
        -d '{"message":"A test message from me."}' \
        "localhost:8080/assistants/my-assistant/chat"
    ```
    Expected response reflecting the enhanced chat functionality: `"You said: A test message from me."`

## Installation in a Kubernetes cluster
Currently, it is already deployed a K8s cluster (in Google Cloud) and exposed to the internet. You can call with curl instructions below. The application host is `https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/`.

### Pre-requisites for Kubernetes Installation
- A running Kubernetes cluster.
- [Kyma Istio module](https://github.com/kyma-project/istio) installed for service mesh capabilities.
- [Kyma Gateway module](https://github.com/kyma-project/api-gateway) installed for exposing services.
- If using Redis storage:
    - A Redis instance deployed and accessible within your Kubernetes cluster.
    - The Kubernetes deployment manifest (`config/k8s-resources.yaml`) must be configured to activate the `redis` profile and provide necessary Redis connection details (see "Application Configuration" and the deployment manifest example).

### Steps to Deploy in Kubernetes

1.  **Create a Namespace:**
    ```bash
    kubectl create namespace assistant
    ```
2.  **Enable Istio Sidecar Injection:**
    ```bash
    kubectl label namespace assistant istio-injection=enabled --overwrite
    ```
3.  **Configure and Deploy the Digital Assistant:**
    *   **Important**: Review and customize `config/k8s-resources.yaml` before deployment.
    *   Update the container image path if you're using a custom image registry.
    *   Set environment variables within the deployment manifest to manage application configuration (refer to "Application Configuration" section):
        *   `SPRING_PROFILES_ACTIVE`: Set to `redis` for Redis storage, or `in-memory`.
        *   If using `redis`, also set `SPRING_DATA_REDIS_HOST`, `SPRING_DATA_REDIS_PORT`, and `SPRING_DATA_REDIS_PASSWORD` (if applicable) to point to your Redis service within the cluster.
    *   Apply the deployment manifest:
        ```bash
        kubectl apply -f config/k8s-resources.yaml -n assistant
        ```

4.  **Expose the Service via Kyma API Rule:**
```bash
kubectl apply -f config/kyma-apirule.yaml -n assistant
```
>**NOTE**: you have to provide your own host in the ApiRule resource according to you DNS configration.

5. Create a new digital assistant with the following name and response:
```bash
curl -v -H "Content-Type: application/json" -X PUT \
    -d '{"response":"Hello from the Kubernetes cloud!"}' \
    "https://<your-k8s-host>/assistants/k8s-assistant"
```

6.  **Chat with the Assistant:**
    ```bash
    curl -v -H "Content-Type: application/json" -X POST \
        -d '{"message":"Sending a message to K8s assistant."}' \
        "https://<your-k8s-host>/assistants/k8s-assistant/chat"
    ```
    Expected response: `"You said: Sending a message to K8s assistant."`