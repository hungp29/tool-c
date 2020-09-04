# tool-c
Tool Checkin

Because I often forget to checkin / checkout at the company. Each time I felt very annoying when I have to contact HR and Leader to confirm that I go to work and leave company on time.
So I wrote this tool to make it easier to checkin / checkout. It is still limited and not very good, if I have time or any new ideas I will add later

Build

./gradlew clean buildJar

Create Crypto file

- Open EncryptSubApp.java
- Add values to encrypt to String data[] = {""}; // Define value to encrypt here.
- Define properties crypto.* in application.properties file
- Run EncryptSubApp.java
- Get file crypto in folder config crypto.secret-file
- Get values encrypted in console
