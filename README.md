1. client go to `http://localhost:3000/need-auth`
2. redirect the user to a log in page


_Example 1. Unauthenticated User Requests Protected Resource_
```bash
GET / HTTP/1.1
Host: example.com
Cookie: SESSION=91470ce0-3f3c-455b-b7ad-079b02290f7b

HTTP/1.1 302 Found
Location: /login
```
3. The user submits their username and password.
```bash
POST /login HTTP/1.1
Host: example.com
Cookie: SESSION=91470ce0-3f3c-455b-b7ad-079b02290f7b

username=user&password=password&_csrf=35942e65-a172-4cd4-a1d4-d16a51147b3e
```

4. Upon authenticating the user, the user is associated to a new session id to prevent session fixation attacks.
```bash
4. HTTP/1.1 302 Found
Location: /
Set-Cookie: SESSION=4c66e474-3f5a-43ed-8e48-cc1d8cb1d1c8; Path=/; HttpOnly; SameSite=Lax
```

5. Subsequent requests include the session cookie which is **used to authenticate the user for the remainder** of the session.
```bash
5. GET / HTTP/1.1
Host: example.com
Cookie: SESSION=4c66e474-3f5a-43ed-8e48-cc1d8cb1d1c8
```

## SecurityContextRepository

In Spring Security the association of the user to future requests is made 
using `SecurityContextRepository`. 
The default implementation of `SecurityContextRepository` is `DelegatingSecurityContextRepository`  

which delegates to the following:
 
1. `HttpSessionSecurityContextRepository` 
2. `RequestAttributeSecurityContextRepository`


### HttpSessionSecurityContextRepository
`HttpSessionSecurityContextRepository` associates the `SecurityContext` to the 
`HttpSession`. 
Users can replace `HttpSessionSecurityContextRepository` with another
implementation of `SecurityContextRepository` if they wish to associate 
the user with subsequent requests in another way or not at all.

- `NullSecurityContextRepository`  
implementation of `SecurityContextRepository` that does nothing.  
it is not desirable to associate the `SecurityContext` to an `HttpSession` (i.e. when authenticating with OAuth)  
  

- `RequestAttributeSecurityContextRepository`  
saves the `SecurityContext`((which holds user authentication information)) as a request **attribute** to make sure the   
`SecurityContext` is available for a single request that occurs across   
dispatch types that may clear out the `SecurityContext`.
    1. client make a request(is authenticated)
    2. an error occurs( exceptions because database is down, etc)
    3. any `SecurityContext` that was established **is cleared out** and then
       the error dispatch is made.(Depending on the servlet container implementation)
    4. When the error dispatch is made, there is no `SecurityContext` established.
       - This means that the error page **cannot use** the `SecurityContext` 
         for **authorization or displaying the current user unless**
         the `SecurityContext` is persisted somehow.


extracted from [SecurityContextRepository](https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html)