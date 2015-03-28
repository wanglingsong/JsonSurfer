# JsonSurfer - Let's surf on Json!
## Why JsonSurfer
* Streaming

    No need to deserialize entire json into memory
    
* JsonPath

    Selectively extract json data by the power of JsonPath

* Stoppable

    JsonSurfer is built on [json-simple](https://code.google.com/p/json-simple/)'s Stoppable SAX-like interface that allows the processor to stop itself if necessary.
    
## Getting started

### [What is JsonPath?](http://goessner.net/articles/JsonPath/)

* JsonSurfer is limited at current version:

| Operator                  | Supported |
| :-----------------------: | :-------: |
| `$`                       | YES       |
| `@`                       | Not yet   |
| `*`                       | YES       |
| `..`                      | YES       |
| `.<name>`                 | YES       |
| `['<name>' (, '<name>')]` | YES       |
| `[<number> (, <number>)]` | YES       |
| `[start:end]`             | Not yet   |
| `[?(<expression>)]`       | Not yet   |

* JsonSurfer relies on third party library for parsing json. e.g json-simple, gson or jackson
* Pluggable json model provider
```java
        // use json-simple parser
        JsonSimpleSurfer loader = new JsonSimpleSurfer();
        // transform json into json-simple model i.e.org.json.simple.JSONObject or org.json.simple.JSONArray
        Builder builder = context().withJsonProvider(new JsonSimpleProvider()); 
```
```java
        // use gson parser
        GsonSurfer loader = new GsonSurfer();
        // transform json into gson model i.e.com.google.gson.JsonElement
        Builder builder = context().withJsonProvider(new GsonProvider());
```
* **GsonSurfer** is recommended!
* JsonSurfer offer a Java DSL for building JsonPath. More details in the following code examples.
```java
        // equivalent to $.builders.*.properties
        builder.bind(root().child("builders").anyChild().child("properties"), printListener).skipOverlappedPath();
```
* Stop parsing on the fly
```java
TODO
```
### Code Examples

* Sample Json:
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

1. Find the authors of all books: 
```javascript
$.store.book[*].author
```
```java
        HandlerBuilder builder = handler();
        builder.bind(root().child("store").child("book").anyIndex().child("author"), new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                System.out.println(value);
            }
        });
        GsonSurfer surfer = new GsonSurfer();
        surfer.surf(new FileReader(new File("sample.json")), builder.build());
```
