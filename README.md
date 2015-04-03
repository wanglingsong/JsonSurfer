# JsonSurfer - Let's surf on Json!
![](https://travis-ci.org/jsurfer/JsonSurfer.svg?branch=master)
## Why JsonSurfer
Jsonsurfer is good at processing **big and complicated json** data with three major features.
* Streaming

    No need to deserialize entire json into memory
    
* JsonPath

    Selectively extract json data by the power of JsonPath

* Stoppable

    JsonSurfer is built on stoppable SAX-like interface that allows the processor to stop itself if necessary.
    
## Getting started

### [What is JsonPath?](http://goessner.net/articles/JsonPath/)

* JsonSurfer supports imcomplete JsonPath feature at current version:

| Operator                  |   Description     | Supported |
| :-----------------------: |:-----------------:| :-------: |
| `$`                       | root              | YES       |
| `@`                       | current node      | Not yet   |
| `*`                       | wildcard          | YES       |
| `..`                      | recursive descent | YES       |
| `.<name>`                 | child             | YES       |
| `['<name>' (, '<name>')]` | child/children    | YES       |
| `[<number> (, <number>)]` | index/indices     | YES       |
| `[start:end]`             | array slice       | Yes       |
| `[?(<expression>)]`       | expression        | Not yet   |

* JsonSurfer relies on third party library for parsing json and has dedicated maven projects built for them. e.g json-simple, gson or jackson
* Pluggable json parser and model provider
```java
        // use json-simple parser (Json-Simple dependency is included by default)
        // transform json into json-simple model i.e.org.json.simple.JSONObject or org.json.simple.JSONArray
        JsonSurfer surfer = new JsonSurfer(new JsonSimpleParser(), new JsonSimpleProvider());
        // or 
        JsonSurfer surfer = JsonSurfer.simple();
```
```java
        // use gson parser (You need to explicitly declare Gson dependency in you pom)
        // transform json into gson model i.e.com.google.gson.JsonElement
        JsonSurfer surfer = new JsonSurfer(new GsonParser(), new GsonProvider());
        // or 
        JsonSurfer surfer = JsonSurfer.gson();
```
* JsonPath binding. More details in the code examples section.
```java
        // Find all properties within builders objects
        builder.bind("$.builders.*.properties", printListener).skipOverlappedPath();
```
* Stop parsing on the fly. Refer to [Stoppable parsing](#stoppable-parsing)

### Code Examples

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
        JsonPathListener print = new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                System.out.println(value);
            }
        };
        JsonSurfer surfer = JsonSurfer.gson();
        SurfingContext.Builder builder = BuilderFactory.context();
        builder.bind("$.store.book[*].author", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
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
        builder.bind("$..author", print);
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
        builder.bind("$.store.*", print);
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
        builder.bind("$.store..price", print);
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
        builder.bind("$..book[2]", print);
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
        builder.bind("$..book[0,1]", print);
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
        builder.bind("$..book[0,1]", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext parsingContext) {
                parsingContext.stopParsing();
                System.out.println(value);
            }
        });
```
Output
```
{"category":"reference","author":"Nigel Rees","title":"Sayings of the Century","price":8.95}
```

