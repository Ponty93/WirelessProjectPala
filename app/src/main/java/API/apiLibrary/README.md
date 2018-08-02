# DavidWebb

Lightweight Java HTTP-Client for calling JSON REST-Services (especially made for Android).

## Problem

If you have to call a RESTful Webservice from Java, especially if you are on Android, you have some options:

 * Use `DefaultHttpClient` or `AndroidHttpClient`. It is already deployed on Android and it's easy to use.
   But wait a moment -
   [Google doesn't recommend using it](http://android-developers.blogspot.de/2011/09/androids-http-clients.html),
   only on very old Android versions.
 * Use `HttpURLConnection`. This is what Google recommends for newer Android versions (>= Gingerbread).
   It is part of JDK, but it's cumbersome to use (if not to say a nightmare).
 * Add `Unirest`, `Restlet` or some other "all-you-can-eat", universal, multi-part, File-upload and all-cases
   supporting library which adds some hundred KB of jars to your APK.

## Solved

**DavidWebb** is a paper-thin wrapper around
[HttpURLConnection](http://docs.oracle.com/javase/7/docs/api/java/net/HttpURLConnection.html).
It supports most HTTP communication cases when you talk to REST services and your data is JSON. It is very
lightweight (~18 KB jar) and super-easy to use.

## Features ##

  * Supports GET, POST, PUT, DELETE
  * add HTTP headers (per request, per client or globally)
  * convert params to `x-www-form-urlencoded` body **or** URI search params
  * fluent API
  * org.json support (JSONObject, JSONArray) as payload in both directions (up/down)
  * wraps all Exceptions in a WebbException (a RuntimeException)
  * automatically sets many boiler-plate HTTP headers (like 'Accept', 'Content-Type', 'Content-Length')
  * GZip-compression for uploads (POST/PUT)
  * Un-compress gzip/deflate downloads
  * supports HTTPS and enables relaxing SSL-handshake (self-signed certificates, hostname verification)
  * pass-through to "real" connection for special cases
  * option to retry the request in case of special errors (503, 504, 'connection reset by peer')
  * multi-valued parameters (since 1.3.0)
  * use streams as input and output (automatically closing underlying HttpURLConnection when stream
    is closed, since 1.3.0)

## Non-Features ##

Following features are not supported and there are no plans to realize them:

  * Cookie management (read `Set-Cookie` header from response and set `Cookie` header for request).
    As DavidWebb is just a thin wrapper over HttpURLConnection, you can use `CookieManager`.
  * Comfortable Basic Authentication (it's not hard to implement it above of DavidWebb, see below)
  * Multi-Part upload
  * Mixing URL-parameters and x-www-form-urlencoded POST bodies.<br>
    Workaround: build the URI with the help of `WebbUtils.queryString` and use `Request.param()` for
    the form-fields.

Where not stated, the workaround is to use another library or implement it above of DavidWebb.
If you think your implementation might be useful for others and it's not blowing up the size of
the JAR, please create a pull request. (Adding heavy dependencies is not an option.)

# Usage Examples

Below you can see some examples of how to use DavidWebb. And here you can find the
[API Documentation](http://hgoebl.github.io/DavidWebb/apidocs/).

**This is some code from a SyncAdapter of an Android App:**

```java
// create the client (one-time, can be used from different threads)
Webb webb = Webb.create();
webb.setBaseUri(SyncPreferences.REST_ENDPOINT);
webb.setDefaultHeader(Webb.HDR_USER_AGENT, Const.UA);

// later we authenticate
Response<JSONObject> response = webb
        .post("/session")
        .param("authentication", createAuthentication(syncPreferences))
        .param("deviceId", syncPreferences.getDeviceId())
        .ensureSuccess()
        .asJsonObject();

JSONObject apiResult = response.getBody();

AccessToken accessToken = new AccessToken();
accessToken.token = apiResult.getString("token");
accessToken.validUntil = apiResult.getLong("validUntil");

webb.setDefaultHeader(HDR_ACCESS_TOKEN, accessToken.token);

JSONObject sync = webb.post("/startSync")
        .param("lastSync", syncPreferences.getLastSync())
        .ensureSuccess()
        .asJsonObject()
        .getBody();

// ... etc. etc.

// releaseAccessToken
webb.delete("/session").asVoid();
accessToken = null;
```

**Using Google Directions API:**

```java
Webb webb = Webb.create();
JSONObject result = webb
        .get("http://maps.googleapis.com/maps/api/directions/json")
        .param("origin", new GeoPoint(47.8227, 12.096933))
        .param("destination", new GeoPoint(47.8633, 12.215533))
        .param("mode", "walking")
        .param("sensor", "true")
        .ensureSuccess()
        .asJsonObject()
        .getBody();

JSONArray routes = result.getJSONArray("routes");
```

**Deal with "connection reset by peer" or other recoverable errors**

Android (at least >= GINGERBREAD) automatically sets the "Connection" header to "keep-alive".
This sometimes causes errors, because the server might already have closed the connection without
the mobile device knowing it. It would be cumbersome to cope with this and other situation where
a retry solved all problems. Since version 1.2.0 it is more comfortable for you:

```java
Webb webb = Webb.create();
JSONObject result = webb
        .get("https://example.com/api/request")
        .retry(1, false) // at most one retry, don't do exponential backoff
        .asJsonObject()
        .getBody();
```

In many cases you will need to change the behaviour of how and when to retry a request.
For this, you can register your own `RetryManager`, see `webb.setRetryManager()`.

**You have to do Basic Authentication?**

This authorization method uses a Base64 encoded string. Unfortunately Java SE doesn't provide a
Base64 encoder. Because DavidWebb wants to be light and Android already provides a Base64 support class,
it's left to you to insert a few lines of code. As you can see, it's not hard work.
Use one of the methods to set a header and set Authorization header by yourself:

```java
byte[] credentials = (username + ":" + password).getBytes("UTF-8");
String auth = "Basic " + Base64.encodeToString(credentials, 0);
Webb webb = Webb.create();
webb.setDefaultHeader(Webb.HDR_AUTHORIZATION, auth);
```

**More Samples**

If you want to see more examples, just have a look at the JUnit TestCase (src/test/java/...).

## Special Case Android < Froyo

You should add this if you build for legacy Android devices:

```java
if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
    System.setProperty("http.keepAlive", "false");
}
```

## In Case Of Problems

I've encountered some problems which could be solved by disabling **keep-alive** of HTTP connections.
The solution above did not always work on recent versions of Android. To be sure to do without
keep-alive, you can set an HTTP header like this:

```
// disable 'keep-alive' for all requests created by this instance of Webb
webb.setDefaultHeader("Connection", "close");

// disable only for this request
Request<String> request = webb.post("/some-resource").header("Connection", "close");
```
# Maven Coordinates

```xml
<dependency>
    <groupId>com.goebl</groupId>
    <artifactId>david-webb</artifactId>
    <version>1.3.0</version>
</dependency>
```

Gradle

    'com.goebl:david-webb:1.3.0'

Not using Maven/Gradle? - Then you can download the plain JAR from following links directly:

 * [SNAPSHOT Versions](https://oss.sonatype.org/content/groups/staging/com/goebl/david-webb/)
 * [RELEASE Versions](http://repo.maven.apache.org/maven2/com/goebl/david-webb/)

# Background

## Not for you?

If **DavidWebb** is too lightweight and you're missing features, you can have a look at:

  * [OkHttp](http://square.github.io/okhttp/) An HTTP & SPDY client for Android and Java applications
  * [Volley](https://developer.android.com/training/volley/index.html) is an HTTP library that makes networking for
    Android apps easier and most importantly, faster.
  * [basic-http-client](https://code.google.com/p/basic-http-client/)
    Basic HTTP client w/ Android AsyncTask wrapper
  * [RESTDroid](https://github.com/PCreations/RESTDroid)
    Resource oriented REST client for Android
  * [RoboSpice](https://github.com/octo-online/robospice)
    RoboSpice is a modular android library that makes writing asynchronous network requests easy!
  * [android-rest-client](https://github.com/darko1002001/android-rest-client)
    A simple rest API client library
  * [unirest](http://unirest.io/)
    Lightweight HTTP Request Library
  * [Restlet Framework](http://restlet.org/)
    The leading web API framework for Java
  * [DataDroid](http://datadroid.foxykeep.com/) - an Android library for Data Management
  * [google-http-java-client](https://github.com/google/google-http-java-client) Google HTTP Client Library for Java
  * [More Alternatives (on RoboSpice)](https://github.com/octo-online/robospice#alternatives-to-robospice-)
  * (tell me if I missed your award-winning REST-client library!)

## The Name!?

David **Webb** is the real name of **Jason** Bourne. So **JSON** and **Web**, did you get it?
OK, might be silly, but Bourne 1-3 are my favorite films and so at least I can remember the name.

From Wikipedia:

> [Jason Bourne](http://en.wikipedia.org/wiki/Jason_Bourne) is a fictional character and the protagonist
of a series of novels by Robert Ludlum and subsequent film adaptations

# License

MIT License, see LICENSE file

# Testing

The Unit-Tests do not mock any network-libraries, but depend on a small Express-application running.

Run following commands before you start the JUnit-tests or skip the tests in Maven build with command line
option `-DskipTests`

```
cd src/test/api-test-server
npm install
node .
```

## Android Tests

Before running the Android tests, build with maven, deploy the Android app and be sure to
set the timezone of your emulator to the same as your PC and synchronize date/time,
otherwise some tests will fail:

There is a script (`src/test/android/run-tests-emulator.sh`) which also builds the test-app with gradle.

## Coverage

The test coverage is measured only with the JUnit-tests for JavaSE. As mocking is not easy
to accomplish under Android/Dalvik, coverage is not measured for this runtime environment.

  * Class: 100%
  * Instructions: 90%
  * Branches: 79%
  * see [JaCoCo Coverage Report](http://hgoebl.github.io/DavidWebb/jacoco/)

# TODO

## Features (planned)
  * support char-sets other than UTF-8
  * unprefixJson <http://haacked.com/archive/2008/11/20/anatomy-of-a-subtle-json-vulnerability.aspx>
  * progress callback during POST requests where size is known in advance
  * Support for PATCH requests (only when OkHttp is used as a replacement for HttpURLConnection)

## Features (only ideas)
  * provide base classes (or only examples) for using DavidWebb together with `AsyncTask`
  * decorator/interceptor beforeSend - provide hooks to manipulate request before send
  * decorator/interceptor afterReceive - provide hooks to manipulate raw response after receiving it

Create an issue if you want to have one of those ideas implemented.

## Documentation
  * Create User-Guide

## Testing
  * Higher coverage
  * Test against httpbin.org

# Contributors

 * [essobedo](https://github.com/essobedo)
