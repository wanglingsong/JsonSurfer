# JsonSurfer - Let's surf on Json!
## Why JsonSurfer
How to read data from json in Java? 

Two approaches:

* Deserialize entire json into memory. i.e. Map or Java POJO

    But what if the json is large? For example, 100 MB or larger.

* Parse json using streaming.

    Streaming can save you from memory-hogging.

    But what if the json object is complicated (i.e. desired value nested deeply)?

    Luckily, most of popular json library like Gson and Jackson provide powerful streaming API, however, they are not convenient enough in this case, um.. at least for me.

## Getting started
[JsonPath](http://goessner.net/articles/JsonPath/)

## Code Example

