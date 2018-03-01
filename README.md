## Sunrise Extensions

[![Build Status](https://travis-ci.org/commercetools/commercetools-sunrise-extensions.svg?branch=master)](https://travis-ci.org/commercetools/commercetools-sunrise-extensions)

A proof of concept to add [API Extensions](https://docs.commercetools.com/http-api-projects-api-extensions.html) to the [Sunrise Java Shop](https://github.com/commercetools/commercetools-sunrise-java).

## Extensions

### Cart Customer Email

When creating a cart with the `customerId` field set but not the `customerEmail` field then the extension fetches the customer and adds the email of this customer to this cart.

Endpoint: POST /carts/set-customer-email

## Developer Hints

When working with IntelliJ IDEA you need to enable Annotation Processing in the Preferences.

## Deployment

### AWS Lambda

To upload the code ("Function package") to Lambda create a "fat" jar containing all code with library dependencies

```
./gradlew shadowJar to create fat jar
```

You can find the artifact at `build/libs/sunrise-extensions-0.0.1-SNAPSHOT-all.jar `.

Setting for AWS Lambda:

* Runtime: Java 8
* Handler: com.commercetools.sunrise.extensions.lambda.CartSetCustomerEmailRequestHandler::handleRequest`

### Heroku