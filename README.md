# Moxy
Moxy is Android library based on MVP pattern.

Main idea of using Moxy:
![schemmatic using](https://habrastorage.org/files/ac7/e3c/6f5/ac7e3c6f5eec4f498ab50e597606faa5.gif)
What's happened here:

1. At _View_ happened action ![blue square](https://habrastorage.org/files/88e/47f/0d5/88e47f0d5767494c9dc56879b2281d28.png), that is passed to _Presenter_
2. _Presenter_ sends command ![red circle](https://habrastorage.org/files/b0c/d57/199/b0cd57199d4f4bcea465aefb21061461.png) in _ViewState_
3. _Presenter_ starts async request ![green square](https://habrastorage.org/files/998/8b1/57f/9988b157f9b544fd89b4af4aea061f87.png) to _Model_
4. _ViewState_ adds a command ![red circle](https://habrastorage.org/files/b0c/d57/199/b0cd57199d4f4bcea465aefb21061461.png) in commands queue, and then passes it in _View_
5. _View_ brings itself into a state specified in the command ![red circle](https://habrastorage.org/files/b0c/d57/199/b0cd57199d4f4bcea465aefb21061461.png)</li>
6. _Presenter_ receives result of request ![green square](https://habrastorage.org/files/998/8b1/57f/9988b157f9b544fd89b4af4aea061f87.png) from _Model_
7. _Presenter_ sends two commands ![green circle](https://habrastorage.org/files/9bd/23f/e0c/9bd23fe0c88c4d8f8b4a498474a6ad09.png) and ![blue circle](https://habrastorage.org/files/70c/231/d6b/70c231d6bf6b432ba83d5ecf2e97aafd.png) in _ViewState_
8. _ViewState_ saves commands ![green circle](https://habrastorage.org/files/9bd/23f/e0c/9bd23fe0c88c4d8f8b4a498474a6ad09.png) and ![blue circle](https://habrastorage.org/files/70c/231/d6b/70c231d6bf6b432ba83d5ecf2e97aafd.png) in commads queue and send them in _View_
9. _View_ brings itself into a state specified in the commands ![green circle](https://habrastorage.org/files/9bd/23f/e0c/9bd23fe0c88c4d8f8b4a498474a6ad09.png) and ![blue circle](https://habrastorage.org/files/70c/231/d6b/70c231d6bf6b432ba83d5ecf2e97aafd.png)</li>
10. New/recreated _View_ attach to existing _Presenter_
11. _ViewState_ sends queue of saved commands to new/recreate _View_
12. New/recreated _View_ brings itself into a state specified in the commands ![red circle](https://habrastorage.org/files/b0c/d57/199/b0cd57199d4f4bcea465aefb21061461.png), ![green circle](https://habrastorage.org/files/9bd/23f/e0c/9bd23fe0c88c4d8f8b4a498474a6ad09.png) and ![blue circle](https://habrastorage.org/files/70c/231/d6b/70c231d6bf6b432ba83d5ecf2e97aafd.png)

## Capabilities

Moxy has a few killer features on other ways:
- _Presenter_ stay alive when _Activity_ recreated(it simplify work with multithreading)
- Automatically restore all what user see when _Activity_ recreated(including dynamic content is added)
- Capability to changes of many _Views_ from one _Presenter_

## Sample
You can find the sample project [here](https://github.com/Arello-Mobile/MoxySample)

## References
[_RU_] [Moxy — реализация MVP под Android с щепоткой магии](https://habrahabr.ru/post/276189/)

## Integration

Maven integration:
```
<dependencies>
  <dependency>
    <groupId>com.arello-mobile</groupId>
    <artifactId>moxy</artifactId>
    <version>0.5.4</version>
  </dependency>
  <dependency>
    <groupId>com.arello-mobile</groupId>
    <artifactId>moxy-android</artifactId>
    <version>0.5.4</version>
  </dependency>
  <dependency>
    <groupId>com.arello-mobile</groupId>
    <artifactId>moxy-compiler</artifactId>
    <version>0.5.4</version>
    <optional>true</optional>
  </dependency>
</dependencies>
```

Gradle integration:
```
dependencies {
  ...
  compile 'com.arello-mobile:moxy:0.5.4'
  compile 'com.arello-mobile:moxy-android:0.5.4'
  provided 'com.arello-mobile:moxy-compiler:0.5.4'
}
```
## Android studio templates

In order to avoid boilerplate code creating for binding activity,fragments and its presentation part, we propose to use Android Studio templates for Moxy. 

Templates located in [/moxy-templates](https://github.com/Arello-Mobile/Moxy/tree/master/moxy-templates)



## License
```
The MIT License (MIT)

Copyright (c) 2016 Arello Mobile

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
