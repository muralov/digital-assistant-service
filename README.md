# Digital Assistant

Alllows to create a digittal assistant with a name and a response. The assistant can be called with a message and it will respond with the predefined response.

## Running the Digital Assistant locally
### Pre-requisites
- JDK 21

### Common steps
1. Clone the repository
```bash
git clone git@github.com:muralov/digital-assistant-service.git
```
2. Change directory to the project
```bash
cd digital-assistant-service
```
3. (Optional) You can use Redis storage to store digital assistants data. For that, you have to install Redis without authentication. This prevents loss of data after application restart. You can use either of the following options:
- **Local run**: you can have redis run locally and configure it in `application.properties`
    ```properties
    spring.data.redis.host=localhost
    spring.data.redis.port=6379
    ```
- **Kubernetes run**: you can deploy [Redis](https://redis.io/docs/latest/operate/kubernetes/deployment/helm/) in a K8s cluster and you have to configure it in the app with k8s config map in `config/k8s-resources.yaml`.

> **NOTE**: By default, it uses in-memory if Redis config is unavailable.

### Steps to run the Digital Assistant locally

1. Run the project
```bash
./gradlew clean build bootRun
```

2. Create a new digital assistant with the following name and response:
```bash
curl -v -H "Content-Type: application/json" -X PUT \
    -d '{"response":"Hello, I am dummy assistant. What can I do for you!"}' \
    "localhost:8080/assistants/sample-name"
```

3. Call the digital assistant
```bash
curl -v -H "Content-Type: application/json" -X POST \
    -d '{"message":"hello"}' \
    "localhost:8080/assistants/sample-name/chat"
```

## Installation in a Kubernetes cluster
Currently, it is already deployed a K8s cluster (in Google Cloud) and exposed to the internet. You can call with curl instructions below. The application host is `https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/`.

### Pre-requisites for Installation
- Kubernetes cluster
- [Kymya Istio module](https://github.com/kyma-project/istio) - follow the instructions to install Istio in your cluster.
- [Kyma Gateway module](https://github.com/kyma-project/api-gateway) - follow the instructions to install Kyma Gateway in your cluster.

### Steps to run the Digital Assistant

1. Create a namespace for the Digital Assistant
```bash
kubectl create namespace assistant
```

2. Enable Istio sidecar injection for the namespace
```bash
kubectl label namespace assistant istio-injection=enabled --overwrite
```

3. Deploy the Digital Assistant
```bash
kubectl apply -f config/k8s-resources.yaml -n assistant
```

4. Expose it to the Internet using Kyma API Rule
```bash
kubectl apply -f config/kyma-apirule.yaml -n assistant
```
>**NOTE**: you have to provide your own host in the ApiRule resource according to you DNS configration.

5. Create a new digital assistant with the following name and response:
```bash
curl -v -H "Content-Type: application/json" -X PUT \
    -d '{"response":"Hello, I am dummy assistant. What can I do for you!"}' \
    "https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/assistants/sample-name"
```

6. Call the assistant to get the response:
```bash
curl -v -H "Content-Type: application/json" -X POST \
    -d '{"message":"hello"}' \
    "https://digital-assistant.mur-kc.kymatunas.shoot.canary.k8s-hana.ondemand.com/assistants/sample-name/chat"
```