

Sending requests in parallel ie. single packet attack for http2 and last byte synchronization for http1



```java

/update-email

// update unverified email and generates a new verification token then store it in the db for the user
updateEmailAndGenerateVerificationToken(userId, unVerifiedEmail, genVerificationToken());
// generates a verification email template from db for the user
EmailTemplate template = genEmailTemplate(userId, TEMPLATE.UPDATE_EMAIL);
// send the email for the user
sendEmailVerification(userId, unVerifiedEmail, template);
```