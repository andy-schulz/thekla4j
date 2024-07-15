---
title: Http Cookies
parent: Http
grand_parent: Web
layout: default
has_children: false
nav_order: 1540
---

# Working with Cookies
Find a detailed a description about cookies at the [MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies?retiredLocale=de)
and how to [set them](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie#httponly)

## Cookie record

| parameter   | type          | description                                                                                                                                                               |
|:------------|:--------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name        | String        | name of the cookie                                                                                                                                                        |
| value       | String        | value of the cookie                                                                                                                                                       |
| domain      | String        | [Define where cookies are sent](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies?retiredLocale=de#define_where_cookies_are_sent)                                 |
| path        | String        |                                                                                                                                                                           |
| expires     | LocalDateTime |                                                                                                                                                                           |
| sameSite    | String        | [Controlling third-party cookies with SameSite](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies?retiredLocale=de#controlling_third-party_cookies_with_samesite) |
| maxAge      | String        |                                                                                                                                                                           |
| httpOnly    | boolean       |                                                                                                                                                                           |
| secure      | boolean       |                                                                                                                                                                           |
| partitioned | boolean       |                                                                                                                                                                           |



test