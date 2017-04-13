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

* Supported JsonPath operator in JsonSurfer:

| Operator                  |   Description     |
| :-----------------------: |:-----------------:|
| `$`                       | root              |
| `@`                       | current node      |
| `*`                       | wildcard          |
| `..`                      | recursive descent |
| `.<name>`                 | child             |
| `['<name>' (, '<name>')]` | child/children    |
| `[<number> (, <number>)]` | index/indices     |
| `[start:end]`             | array slice       |
| `[?(<expression>)]`       | filter expression |

* JsonSurfer is available in cetral maven repository.

JsonSurfer has drivers for most of popular json libraries including: Gson, Jackson, FastJson and JsonSimple. Choose one and add to your POM.

```xml

<dependency>
    <groupId>com.github.jsurfer</groupId>
    <artifactId>jsurfer-gson</artifactId>
    <version>1.3.1</version>
</dependency>

<dependency>
    <groupId>com.github.jsurfer</groupId>
    <artifactId>jsurfer-jackson</artifactId>
    <version>1.3.1</version>
</dependency>

<dependency>
    <groupId>com.github.jsurfer</groupId>
    <artifactId>jsurfer-fastjson</artifactId>
    <version>1.3.1</version>
</dependency>

<dependency>
    <groupId>com.github.jsurfer</groupId>
    <artifactId>jsurfer-jsonsimple</artifactId>
    <version>1.3.1</version>
</dependency>

```

### Usage:

#### Create your JsonSurfer:

* JsonSurfer has flexible constructor. You can create yourself or pick a prebuilt one according the json library you used:
1. Gson
```java
        // use gson parser and use gson provider use to deserialize json into gson model i.e.com.google.gson.JsonElement
        JsonSurfer surfer = new JsonSurfer(GsonParser.INSTANCE, GsonProvider.INSTANCE);
```
or
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
```
2. Jackson
```java
        JsonSurfer surfer = new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);
```
or
```java
        JsonSurfer surfer = JsonSurferJackson.INSTANCE;
```
3. JsonSimple
```java
        // use json-simple parser and json-simple provider to deserialize json into json-simple model i.e.org.json.simple.JSONObject or org.json.simple.JSONArray
        JsonSurfer surfer = new JsonSurfer(JsonSimpleParser.INSTANCE, JsonSimpleProvider.INSTANCE);
```
or
```java
        JsonSurfer surfer = JsonSurferJsonSimple.INSTANCE;
```
4. Fastjson
```java
        JsonSurfer surfer = new JsonSurfer(FastJsonParser.INSTANCE, FastJsonProvider.INSTANCE);
```
or
```java
        JsonSurfer surfer = JsonSurferFastJson.INSTANCE;
```
#### "Surfing" in Json and collecting matched value in the listeners
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
        surfer.configBuilder()
                .bind("$.store.book[*]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) throws Exception {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```

#### Reuse listener binding
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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

#### Collect the first matched value and stop immediately
```java
        JsonSurfer jsonSurfer = JsonSurferGson.INSTANCE;
        Object singleResult = jsonSurfer.collectOne(sample, "$.store.book[0]");
```
#### Colllect every matched value in a collection
```java
        JsonSurfer jsonSurfer = JsonSurferGson.INSTANCE;
        Collection<Object> multipleResults = jsonSurfer.collectAll(sample, "$.store.book[*]");
```
#### Stop parsing on the fly
* Refer to [Stoppable parsing](#stoppable-parsing)
#### Filters
* Filter operators

| Operator                  |   Description     |
| :-----------------------: |:-----------------:|
| ==                        | equal             |
| <                         | less than         |
| >                         | greater than      |

You can use logical operators '&&' and '||' to create more complex filter expression. For example:
```
$.store.book[?(@.price < 10 || @.category && @.isbn && @.price>10)]
```

#### Resolver API:
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
#### Stop parsing on the fly
* Refer to [Stoppable parsing](#stoppable-parsing)

### Examples

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

| JsonPath                  |   Result     |
| :-----------------------: |:-----------------:|
| ```$.store.book[*].author``` | [Find the authors of all books](#find-the-authors-of-all-books)  |
| ```$..author```              | [All authors](#all-authors)                    |
| ```$.store.*```              | [All things in store](#all-things-in-store)                   |
| ```$.store..price``` | [The price of everything in the store](#the-price-of-everything-in-the-store)  |
| ```$..book[2]```              | [The thrid book](#the-thrid-book)                   |
| ```$..book[0,1]```              | [The first two books](#the-first-two-books)                 |
| ```$.store.book[?(@.price==8.95)]``` | [Filter all books whose price equals to 8.95](#filter-all-books-whose-price-equals-to-8.95)  |
| ```$.store.book[?(@.category=='fiction')]```              | [Filter all books which belong to fiction category](#filter-all-books-which-belong-to-fiction-category)                   |

#### Find the authors of all books: 
```javascript
$.store.book[*].author
```
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
#### Filter all books whose price equals to 8.95
```javascript
$.store.book[?(@.price==8.95)]
```
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
        surfer.configBuilder()
                .bind("$.store.book[?(@.price==8.95)]", new JsonPathListener() {
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
```
#### Filter all books which belong to fiction category
```javascript
$.store.book[?(@.category=='fiction')]
```
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
        surfer.configBuilder()
                .bind("$.store.book[?(@.category=='fiction')]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                })
                .buildAndSurf(sample);
```
Output
```
{"category":"fiction","author":"Evelyn Waugh","title":"Sword of Honour","price":12.99}
{"category":"fiction","author":"Herman Melville","title":"Moby Dick","isbn":"0-553-21311-3","price":8.99}
{"category":"fiction","author":"J. R. R. Tolkien","title":"The Lord of the Rings","isbn":"0-395-19395-8","price":22.99}
```
#### Stoppable parsing
The parsing is stopped when the first book found and printed.
```javascript
$..book[0,1]
```
```java
        JsonSurfer surfer = JsonSurferGson.INSTANCE;
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
Benchmark                                                       Mode  Cnt       Score       Error  Units
BenchmarkCollectSingleValue.benchmarkFastjson                  thrpt   10  139772.275      8854.369  ops/s
BenchmarkCollectSingleValue.benchmarkFastjsonWithJsonSurfer    thrpt   10  699176.961      23396.619  ops/s
BenchmarkCollectSingleValue.benchmarkGson                      thrpt   10  139394.358      6019.764  ops/s
BenchmarkCollectSingleValue.benchmarkGsonWithJsonSurfer        thrpt   10  632155.657      15484.499  ops/s
BenchmarkCollectSingleValue.benchmarkJackson                   thrpt   10  160545.079      7006.525  ops/s
BenchmarkCollectSingleValue.benchmarkJacksonWithJsonSurfer     thrpt   10  451870.586      13132.576  ops/s
BenchmarkCollectSingleValue.benchmarkJsonSimpleWithJsonSurfer  thrpt   10  155094.948      4457.502  ops/s
```
