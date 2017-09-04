# How to use 
To test signature you need to have merchant on Test envirotment ( ex merchant on EXT like cookie shop )
Then generate transaction on behalf on this Merchant example in Postman:
POST /v2/transactions HTTP/1.1
```

Host: dev.payconiq.com
Content-Type: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJraWQiOiJwYXljb25pcSIsImFsZyI6IkVTMjU2In0.eyJzdWIiOiI1NjlmODI4OWMyNWM0MTAzYTM1OTdlYzgiLCJqdGkiOiIyOTdmNzhlMS02ZjVjLTRiZGItYjQ5Yi1kNjNhN2E4Yzg4YjMiLCJpYXQiOjE0OTYyMDc5NTUsImV4cCI6MTUyNzc0Mzk1NSwibmJmIjoxNDk2MjA3OTU1LCJ0b2tlblR5cGUiOiJUT0tFTiIsInN0eXBlIjoiTSJ9.PJUQJGJWbUNYPNbQz0O4DcqIn80z6LpkSr9ozzzPAHwJYbtB9I-YvFNhKvXbnaq7SNjL7PjuKUORKClwse-Ndw
Cache-Control: no-cache
Postman-Token: 6efa7892-8ca7-43c6-d8f4-3596242efbf7

{
    "amount": "1",
    "currency": "EUR",
    "callbackUrl": "http://mockbin.org/bin/75b69e7a-58a1-432f-82de-79f0df42f63a"
}

```

as callbackURL you can specify your own URL or use online generator. *Important* Needs to be accessible through the internet

On the URL you wil get all necessary information. 

Inside VerifySignature.java file add data which you get from callbackURL. 

# Run
mvn clean install
java -jar target/verify-signature-0.0.1.jar 