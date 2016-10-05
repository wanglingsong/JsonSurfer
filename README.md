# JsonSurfer - Let's surf on Json!

[![Join the chat at https://gitter.im/jsurfer/JsonSurfer](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/jsurfer/JsonSurfer?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

![](https://travis-ci.org/jsurfer/JsonSurfer.svg?branch=master) [![Coverage Status](https://coveralls.io/repos/jsurfer/JsonSurfer/badge.svg)](https://coveralls.io/r/jsurfer/JsonSurfer) [![Code Advisor On Demand Status](https://badges.ondemand.coverity.com/streams/1nud1532tp6in87qf8mvq5a2a0)](https://ondemand.coverity.com/streams/1nud1532tp6in87qf8mvq5a2a0/jobs)

## Why JsonSurfer

Jsonsurfer is dedicated in processing **big and complicated json** data with three major features.
* Streaming

    No need to deserialize entire json into memory
    
* JsonPath

    Selectively extract json data by the power of JsonPath

* Stoppable

    JsonSurfer is built on stoppable SAX-like interface that allows the processor to stop itself if necessary.
    
## Getting started

### [What is JsonPath?](http://goessner.net/articles/JsonPath/)

* JsonSurfer supports incomplete JsonPath feature at current version:

| Operator                  |   Description     | Supported |
| :-----------------------: |:-----------------:| :-------: |
| `$`                       | root              | YES       |
| `@`                       | current node      | Not yet   |
| `*`                       | wildcard          | YES       |
| `..`                      | recursive descent | YES       |
| `.<name>`                 | child             | YES       |
| `['<name>' (, '<name>')]` | child/children    | YES       |
| `[<number> (, <number>)]` | index/indices     | YES       |
| `[start:end]`             | array slice       | YES       |
| `[?(<expression>)]`       | expression        | Not yet   |

* JsonSurfer is available in cetral maven repository.

```xml
<dependency>
    <groupId>com.github.jsurfer</groupId>
    <artifactId>jsurfer-core</artifactId>
    <version>1.2.8</version>
</dependency>
```
* Optional dependencies

JsonSurfer has driver for Gson, Jackson and JsonSimple but does not transitively include Gson and Jackson library. Please declare them in POM as needed.

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.7</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.8.2</version>
</dependency>
```

### Surfing API:

#### "Surfing" in Json DOM tree collecting matched value in the listeners
```java
        JsonSurfer surfer = new JsonSurfer(GsonParser.INSTANCE, JavaCollectionProvider.INSTANCE);
        surfer.configBuilder()
                .bind("$.store.book[*]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) throws Exception {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```

#### Repeated surfing with same binding
```java
        JsonSurfer surfer = new JsonSurfer(GsonParser.INSTANCE, JavaCollectionProvider.INSTANCE);
        SurfingConfiguration config = surfer.configBuilder()
                .bind("$.store.book[*]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) throws Exception {
                        System.out.println(value);
                    }
                })
                .build();        
        surfer.surf(sample1, config);
        surfer.surf(sample2, config);
```

#### Collect the first matched value
```java
        JsonSurfer jsonSurfer = JsonSurfer.gson();
        Object singleResult = jsonSurfer.collectOne(sample, "$.store.book[0]");
```
#### Colllect every matched value
```java
        JsonSurfer jsonSurfer = JsonSurfer.gson();
        Collection<Object> multipleResults = jsonSurfer.collectAll(sample, "$.store.book[*]");
```

### Resolver API:
* As of 1.2.6, JsonSurfer provides another way of processing json. You can directly resolve value with JsonPath from a well-built DOM like HashMap or even POJO:
```java
        Book book = new Book();
        book.setAuthor("Leo");
        book.setCategory("Fiction");
        book.setPrice(100.0d);
        book.setTitle("JsonSurfer is great!");
        System.out.print(compile("$.author").resolve(book, new PoJoResolver()));
```
which prints "Leo".
```java
        List<String> list = Arrays.asList("foo", "bar");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        System.out.println(compile("$.list[1]").resolve(map, JavaCollectionProvider.INSTANCE));
```
which prints "bar".

### Other API:

* JsonSurfer provides flexible plug-in interface, you can choose your library for parsing and modeling. For example:
```java
        // use json-simple parser (Json-Simple dependency is included by default)
        // transform json into json-simple model i.e.org.json.simple.JSONObject or org.json.simple.JSONArray
        JsonSurfer surfer = new JsonSurfer(JsonSimpleParser.INSTANCE, JsonSimpleProvider.INSTANCE);
        // or JsonSurfer surfer = JsonSurfer.simple();
```
```java
        // use gson parser (You need to explicitly declare Gson dependency in you pom)
        // transform json into gson model i.e.com.google.gson.JsonElement
        JsonSurfer surfer = new JsonSurfer(GsonParser.INSTANCE, GsonProvider.INSTANCE);
        // or JsonSurfer surfer = JsonSurfer.gson();
```
* Stop parsing on the fly. Refer to [Stoppable parsing](#stoppable-parsing)

### More code Examples

Sample Json:
```javascript
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```

#### Find the authors of all books: 
```javascript
$.store.book[*].author
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$.store.book[*].author", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
"Nigel Rees"
"Evelyn Waugh"
"Herman Melville"
"J. R. R. Tolkien"
```
#### All authors
```javascript
$..author
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$..author", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
"Nigel Rees"
"Evelyn Waugh"
"Herman Melville"
"J. R. R. Tolkien"
```
#### All things in store
```javascript
$.store.*
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$.store.*", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
[{"category":"reference","author":"Nigel Rees","title":"Sayings of the Century","price":8.95},{"category":"fiction","author":"Evelyn Waugh","title":"Sword of Honour","price":12.99},{"category":"fiction","author":"Herman Melville","title":"Moby Dick","isbn":"0-553-21311-3","price":8.99},{"category":"fiction","author":"J. R. R. Tolkien","title":"The Lord of the Rings","isbn":"0-395-19395-8","price":22.99}]
{"color":"red","price":19.95}
```
#### The price of everything in the store
```javascript
$.store..price
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$.store..price", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
8.95
12.99
8.99
22.99
19.95
```
#### The thrid book
```javascript
$..book[2]
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$..book[2]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
{"category":"fiction","author":"Herman Melville","title":"Moby Dick","isbn":"0-553-21311-3","price":8.99}
```
#### The first two books
```javascript
$..book[0,1]
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$..book[0,1]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
{"category":"reference","author":"Nigel Rees","title":"Sayings of the Century","price":8.95}
{"category":"fiction","author":"Evelyn Waugh","title":"Sword of Honour","price":12.99}
```
#### Stoppable parsing
The parsing is stopped when the first book found and printed.
```javascript
$..book[0,1]
```
```java
        JsonSurfer surfer = JsonSurfer.gson();
        surfer.configBuilder()
                .bind("$..book[0,1]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {                        
                        System.out.println(value);
                        context.stopParsing();
                    }
                })
                .buildAndSurf(sample);
```
Output
```
{"category":"reference","author":"Nigel Rees","title":"Sayings of the Century","price":8.95}
```
### Benchmark

* JsonSurfer is fast !!! The benchmark is powered by [JMH](http://openjdk.java.net/projects/code-tools/jmh/)

```
Benchmark                                            Mode  Cnt       Score      Error  Units
BenchmarkCollectSingleValue.benchmarkGson           thrpt   10  176764.763 ± 1104.124  ops/s
BenchmarkCollectSingleValue.benchmarkGsonSurfer     thrpt   10  740780.577 ± 5342.493  ops/s
BenchmarkCollectSingleValue.benchmarkJackson        thrpt   10  190154.310 ±  929.849  ops/s
BenchmarkCollectSingleValue.benchmarkJacksonSurfer  thrpt   10  521919.374 ± 2685.533  ops/s
BenchmarkCollectSingleValue.benchmarkSimpleSurfer   thrpt   10  219732.696 ± 3476.744  ops/s
```
