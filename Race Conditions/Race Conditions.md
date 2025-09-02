

#### Limit Overrun - Applying Coupon

- We can apply coupon to your cart with `/cart/coupon`  and then coupon can't be reused unless the remove coupon endpoint is called `/cart/coupon/remove`

```javascript
csrf=UBHXQ1w9NN8qqsqJYalwsnFC2xS4j9Qt&coupon=PROMO20
```


Backend Code Pseudo Code ??

```java
@PostMapping("/cart/coupon")
void applyCoupon(RequestDto req) {
    Counpon coupon = getCoupon(req.couponId())
    if(coupon.isUsed()){
	    throw Exception("Coupon Already used")
    }
	makeCouponUsed(req.couponId());
	
	applyCoupon(req.userId(), coupon);
}

@PostMapping("/cart/coupon/remove")
void removeCouponUsage() {
    Counpon coupon = getCoupon(req.coupon())
    if(!coupon.isUsed()){
	    throw Exception("Coupon never used")
    }
	makeCouponUnUsed(coupon);
	
	revokeCoupon(req.userId(), coupon);
}
```


What if multiple requests hit the coupon endpoint at the same time, before another thread updating the coupon to be used -> TOCTOU  .



![[Pasted image 20250902201727.png]]

![[Pasted image 20250902201745.png]]



### Rate Limit Bypass

UnSafe Code for Login Bypass

```java
@PostMapping("/login")
Response login(RequestDto req) {
    User user = getUser(req.userId());

    // 1. Check if the account is rate limited
    if (isRateLimited(req.username())) {
        return Response.error("Too many attempts. Try later");
    }

    // 2. Validate credentials
    if (!passwordMatches(user, req.password())) {
        // Increase failed login counter
        incrementFailedAttempts(req.username());
        return Response.error("Invalid credentials");
    }

    // 3. Reset failed attempts on success
    resetFailedAttempts(req.username());
    return Response.success("Login successful");
}
```


Safe Version

```java
@PostMapping("/login")
Response login(RequestDto req) {
    User user = getUser(req.username());

    // Check password first
    if (!passwordMatches(user, req.password())) {
        // Atomically: increment failed attempts AND check if exceeded
        if (!incrementAndCheckRateLimit(req.username())) {
            return Response.error("Too many attempts. Try later");
        }
        return Response.error("Invalid credentials");
    }

    // On success, reset the counter
    resetFailedAttempts(req.username());
    return Response.success("Login successful");
}
```



### Multi Endpoint Bypass


- We are able to add an item to the cart in the route `/cart`  with the parameters `productId=2&quantity=1&redir=CART`   < 300 ms
- We are able to checkout in `/cart/checkout` < 500 ms

What if we can add an item, just before the checkout and able to buy it ??

Possible backend Pseudo code


```java
@PostMapping('/cart')
void addToCart(RequestDto request){
	addTheItemToCart(request.userId, request.productId, request.quantity request.cartId);
}

@PostMapping('/checkout')
void checkout(RequestDto request){
	cartDetails = getCartDetails();
	redirToPaymentPage(userId, cardId)
}

void callbackAfterPayment(PaymentCallbackDto p) {
	if(p.isSuccess){
		storeTransaction(p);
		deliverToUser(userId, cartId)
	}
}
```