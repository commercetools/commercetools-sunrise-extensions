# Sunrise Extensions

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

To upload the code ("Function package") to Lambda create a "fat" jar containing all code including library dependencies

```
./gradlew shadowJar
```

You can find the artifact at `build/libs/sunrise-extensions-0.0.1-SNAPSHOT-all.jar `.

Settings for AWS Lambda:

* Runtime: Java 8
* Handler: `com.commercetools.sunrise.extensions.lambda.CartSetCustomerEmailRequestHandler::handleRequest`

Environment Variables to set:

```
SPRING_PROFILES_ACTIVE=production
AUTHENTICATION_KEY=YOUR-FUNCTION-AUTHENTICATION-KEY
CTP_PROJECT_KEY=YOUR-COMMERCETOOLS-PROJECT_KEY
CTP_CLIENT_ID=YOUR-COMMERCETOOLS-CLIENT-ID
CTP_CLIENT_SECRET=YOUR-COMMERCETOOLS-CLIENT-SECRET
CTP_AUTH_URL=https://auth.commercetools.com
CTP_API_URL=https://api.commercetools.com
```

### Heroku

Apart from the code upload, the app deployed to heroku only needs environment variables set:

```
heroku config:set SPRING_PROFILES_ACTIVE=production
heroku config:set CTP_PROJECT_KEY=YOUR-COMMERCETOOLS-PROJECT_KEY
heroku config:set CTP_CLIENT_ID=YOUR-COMMERCETOOLS-CLIENT-ID
heroku config:set CTP_CLIENT_SECRET=YOUR-COMMERCETOOLS-CLIENT-SECRET
heroku config:set CTP_AUTH_URL=https://auth.commercetools.com
heroku config:set CTP_API_URL=https://api.commercetools.com
heroku config:set AUTHENTICATION_KEY=YOUR-FUNCTION-AUTHENTICATION-KEY
```