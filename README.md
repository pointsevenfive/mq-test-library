# mq-test-library

## Description

Java library to help with testing MQ message flows

## Usage

The `Client` class reads an `environment.properties` file from the `resources/` directory that needs to contain the host information of the queue manager (QM)

```properties

test.host = localhost
test.channel = SYSTEM.DEF.CLNTCONN
test.qmanager = DEVQM

```
Based on a prefix that is passed into the `Client` class at instantiation it will configure the connection to the QM.

The response message is returned from the `sendAndReceive` method of `Client`, after passing in your message payload along with the request and response queue names.

```java

Client client = new Client("test");
Message message = client.sendAndReceive(requestQueue, replyQueue, testMessage);

```

## Authors

* **Joseph Fletcher** - *initial build* - [Contact email](mailto:joe-fletcher@live.co.uk)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
