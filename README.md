# quarkus-app
### Deploying using OpenShift extension:
=========================================
#### Add the following properties to application.properties
```
quarkus.openshift.route.expose=true
quarkus.kubernetes.deployment-target=knative
quarkus.kubernetes-client.trust-certs=true
quarkus.s2i.base-jvm-image=registry.access.redhat.com/ubi8/openjdk-11
quarkus.container-image.registry=image-registry.openshift-image-registry.svc:5000
quarkus.container-image.group=quarkus-app #name of the OCP project
quarkus.openshift.env.configmaps=db-env-cm

```oc create configmap db-env-cm --from-literal=db_user=myuser --from-literal=db_password=mypassword --from-literal=db_url=jdbc:mysql://mysql:3306/quarkus --from-literal=db_kind=mysql```

```

Run ```mvn clean package -DskipTests=true -Pnative -Dquarkus.kubernetes.deploy=true```
Run ```kn service update quarkus-serverless-app --env-from config-map:db-env-cm```


### Deploying the native executable:
======================================
Run ```mvn clean package -DskipTests=true -Pnative```

#### Create an container image:
Run ```podman build -t quay.io/balki404/quarkus-serverless-app:3.0 -f Dockerfile.native .```
Run ```podman push quay.io/balki404/quarkus-serverless-app:3.0```
Run ```kn service create quarkus-serverless-app-kn --image quay.io/balki404/quarkus-serverless-app:3.0 --env-from config-map:db-env-cm```

#### Testing locally
podman run -it -p 8080:8080 --net=host quay.io/balki404/quarkus-app:2.0

#### For auto scaling
Change the spec for the knative service
```
    spec:
      containerConcurrency: 1
```
#### Run it under load
siege --concurrent=3 -r1 -H "accept: application/json" --content-type="application/json" 'http://quarkus-app1-quarkus-serverless.apps.cluster-67e6.67e6.sandbox400.opentlc.com/order/getAll'