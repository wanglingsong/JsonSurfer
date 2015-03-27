# JsonSurfer - Let's surf on Json!
## Why JsonSurfer
How to read data from json in Java? 

Two approaches:

* Deserialize entire json into memory. i.e. Map or Java POJO

    But what if the json is large? For example, 100 MB or larger.

* Parse json using streaming.

    Streaming can save you from memory-hogging.

    But what if the json object is complicated (i.e. desired value nested deeply)?

    Luckily, most of popular json library like Gson and Jackson provide powerful streaming API, however, they are still not convenient enough in this case, um.. at least for me.

## Getting started
[JsonPath](http://goessner.net/articles/JsonPath/)

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

## Code Example

$.store.book[*].author:

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
