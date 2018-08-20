# Reproduction for [jackson-datatype-protobuf #48](https://github.com/HubSpot/jackson-datatype-protobuf/issues/48)

## How to run?

1. Start the app by running `./mvnw spring-boot:run`
2. Issue the following request:

   ```
   curl -X "POST" "http://localhost:8080/hello" \
        -H 'Content-Type: application/json; charset=utf-8' \
        -d $'{
     "firstname": "Jackson",
     "lastname": "Five"
   }'
   ```
   (note that the the fields **must** be lowercased as well)
3. Observe the following output
   ```
   {"responsemessage":"Hello Jackson Five","somerandomnumbervariable":42}%
   ```
   
## How to test the workaround?

A workaround has been proposed by [marianiandre](https://github.com/marianiandre) and can be triggered by running `./mvnw spring-boot:run -Dworkaround=true`. Note that the request now requires properly camelcased field names (e.g. `firstName` and `lastName`)
   
