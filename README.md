# quarkus-serverless-app
### Deploying using OpenShift extension (using OpenShift internal image registry):

Add the following properties to application.properties
```
quarkus.kubernetes.deployment-target=knative
quarkus.openshift.build-strategy=docker
quarkus.kubernetes-client.trust-certs=true
quarkus.container-image.registry=image-registry.openshift-image-registry.svc:5000
quarkus.container-image.group=quarkus-serverless
quarkus.native.container-build=true
quarkus.native.container-runtime=docker
quarkus.openshift.native-dockerfile=Dockerfile.native
```

```
oc create configmap db-env-cm --from-literal=db_user=myuser --from-literal=db_password=mypassword --from-literal=db_url=jdbc:mysql://mysql:3306/quarkus --from-literal=db_kind=mysql
mvn clean package -DskipTests=true -Pnative -Dquarkus.kubernetes.deploy=true
kn service update quarkus-serverless-app --env-from config-map:db-env-cm
```


### ### Deploying using OpenShift extension (using external image registry):
```
mvn clean package -DskipTests=true -Pnative
podman build -t quay.io/balki404/quarkus-serverless-app:3.0 -f Dockerfile.native .
podman push quay.io/balki404/quarkus-serverless-app:3.0
kn service create quarkus-serverless-app-kn --image quay.io/balki404/quarkus-serverless-app:3.0 --env-from config-map:db-env-cm
```

For testing locally, run
```
podman run -it -p 8080:8080 --net=host quay.io/balki404/quarkus-app:2.0
```

For auto scaling, change the spec for the knative service
```
    spec:
      containerConcurrency: 1
```

To run it under load, execute
```
siege --concurrent=3 -r1 -H "accept: application/json" --content-type="application/json" 'http://quarkus-app1-quarkus-serverless.apps.cluster-67e6.67e6.sandbox400.opentlc.com/order/getAll'
```
