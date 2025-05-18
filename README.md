# Digital Assistant

Alllows to create a digittal assistant with a name and a response. The assistant can be called with a message and it will respond with the predefined response.

## Running the Digital Assistant locally
### Pre-requisites
- JDK 21

### Common steps
1. Clone the repository
```bash
$ git clone {git-repo-url}
```
2. Change directory to the project
```bash
$ cd digital-assistant-service
```
3. (Optional) You can use Redis storage to store digital assistants data. For that, you have to install Redis without authentication.
- **Local run**: you can have redis run locally and configure it in application.properties
    ```properties
    spring.data.redis.host=localhost
    spring.data.redis.port=6379
    ```
- **Kubernetes run**: you can have redis run in your kubernetes cluster and configure it in the k8s config map: `config/k8s-configmap.yaml`

### Steps to run the Digital Assistant locally

1. Run the project
```bash
$ ./gradlew clean build bootRun
```

2. Create a new digital assistant with the following name and response:
```bash
$ curl -v -H "Content-Type: application/json" -X POST \
    -d '{"name":"local-assistant", "response":"Hello, I am sample-name assistant. What can I do for you!"}' \
    "localhost:8080/assistants"
```

3. Call the digital assistant
```bash
$ curl -v -H "Content-Type: application/json" -X POST \
    -d '{"message":"hello"}' \
    "localhost:8080/assistants/local-assistant/chat"
```

4. (Optional) You can update digital assistant with a new response:
```bash
$ curl -v -X PUT \
    -d 'new response' \
    "localhost:8080/assistants/local-assistant"
```

## Installation and in a Kubernetes cluster

### Pre-requisites
- Kubernetes cluster
- [Kymya Istio module](https://github.com/kyma-project/istio) - follow the instructions to install Istio in your cluster.
- [Kyma Gateway module](https://github.com/kyma-project/api-gateway) - follow the instructions to install Kyma Gateway in your cluster.

### Steps to run the Digital Assistant in a Kubernetes cluster

1. Create a namespace for the Digital Assistant
```bash
$ kubectl create namespace assistant
```

2. Enable Istio sidecar injection for the namespace
```bash
$ kubectl label namespace assistant istio-injection=enabled --overwrite
```

3. Deploy the Digital Assistant
```bash
$ kubectl apply -f config/k8s-deployment.yaml -n assistant
```

4. Expose it to Internet using Kyma Gateway
```bash
$ kubectl apply -f config/kyma-resources.yaml -n assistant
```
**NOTE**: you have to provide your own host in the ApiRule resource according to you DNS configration.

5. Create a new digital assistant with the following name and response:
```bash
$ curl -v -H "Content-Type: application/json" -X POST \
    -d '{"name":"sample-name", "response":"Hello, I am sample-name assistant. What can I do for you?"}' \
    "https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/assistants"
```

6. Call the assistant to get the response:
```bash
$ curl -v -H "Content-Type: application/json" -X POST \
    -d '{"message":"hello"}' \
    "https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/assistants/sample-name/chat"
```

4. (Optional) You can update digital assistant with a new response:
```bash
$ curl -v -X PUT \
    -d 'new response' \
    "https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/assistants/sample-name"
```