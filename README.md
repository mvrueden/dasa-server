# DASA - New Horizons

This is an example implementation of a very simple rest service.

## Build 

**Run with tests**

```
mvn clean package
```

**Run without tests**

```
mvn clean package -DskipTests
```

## Run tests

```
mvn test
```

## API Endpoints
### List devices

**Request**

```
curl -X GET http://localhost:8080/devices
```

**Response**

```
[{"id":1,"energy":1.0,"status":"NO_WARNING","type":"Satellite"}]
```

### Claim session on existing device


**Request**

```
curl -X POST -H 'Content-Type: application/json' -d "{\typ\": \"Satellite\""}" http://localhost:8080/devices/claim
```

**Response**

```
{"sessionId":"7abee89a-43ae-43ac-8f1d-e1d813389701","deviceId":1}
```


In case the requested device is already claimed, a LOCKED http response is returned
```
{"status":"LOCKED","timestamp":[2020,9,16,14,33,21,708083000],"field":"device","message":"The requested device is already in use.","debugMessage":null}* Closing connection 0
```

It accepts a filter object to filter which device should be claimed.
Possible options are:

**deviceId** Id of the device. 
By default a device with id 1 is registered

**typ** The type of the device. 
See [Type](blob/master/src/main/java/com/example/test/model/Type.java) for supported types. 

**energy** The current energy level of the device. 
For now it must match exactly, meaning a requested value of 0.5 will only return those, but not anything with an energy level higher than the provided threshold.

**status** The status of the device. 
See [Status](blob/master/src/main/java/com/example/test/model/Status.java) for supported status.

If you provide more than one value, all filter criteria must match.

### Free already claimed session

**Request**
```
curl -X POST http://localhost:8080/devices/free/<sessionId>
```

**Response**

For now, always returns HTTP Status OK 200, even if the provided sessionId is unknown.


## Docker

### Build everything:

```
mvn clean package
```

Optionally without tests
```
mvn clean package -DskipTests
```

### Build the Docker image

```
docker build . -t dasa/server:bleeding --build-arg JAR_FILE=dasa-0.0.1-SNAPSHOT.jar
```

### Run the container

```
docker run --rm -d -p 8080:8080 dasa/server:bleeding
```
