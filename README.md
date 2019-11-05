[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)   [ ![Download](https://api.bintray.com/packages/edwardstock/android/hawk/images/download.svg) ](https://bintray.com/edwardstock/android/hawk/)

<img align='right' src='https://github.com/edwardstock/hawk/blob/master/art/hawk-logo.png' width='128' height='128'/>

### Hawk 3.0.0
Secure, simple key-value storage for android

**This is improved and refactored version of outdated original library https://github.com/orhanobut/hawk**

#### Important Note
This version has no backward compatibility with Hawk 1+ versions. If you still want to use old versions, [check here](https://github.com/orhanobut/hawk/tree/hawk1)

### Download
Add this to your build.gradle:

```groovy
repositories {
  maven {
    url "https://dl.bintray.com/edwardstock/android"
  }
}
```

```groovy
implementation "com.edwardstock.android:hawk:3.0.0"
```

### Initialize default database
```java
Hawk.init(context).build();
```

### Initialize another instance of db
```java
Hawk.init("my-local-cache", context).build();

// and then, to use "my-local-cache" db, use need to use 
Hawk.db("my-local-cache").put("key", "value");

// Boilerplate code "Hawk.db("longest-string-ever") could hidden with DI Dagger2
@Provides
@AppScope
@Named("some-db")
public HawkDB provideMyLongestDB() {
    return Hawk.db("longest-string-ever");
}

//and then injected
@Inject @Named("some-db")
HawkDB someDb;

// see more documentation of dagger2
// here https://dagger.dev/
```

### Usage
Save any type (Any object, primitives, lists, sets, maps ...)
```java
Hawk.db().put(key, T);
```
Get the original value with the original type
```java
T value = Hawk.db().get(key);
```
Delete any entry
```java
Hawk.db().delete(key);
```
Check if any key exists
```java
Hawk.db().contains(key);
```
Check total entry count
```java
Hawk.db().count();
```
Get crazy and delete everything
```java
Hawk.db().deleteAll();
```

And of course, new generation feature (version **3.0.0**) - batch operation
```java
StorageBatch hawkBatch = new StorageBatch();
hawkBatch
    .put("key1", "value1")
    .put("key2", "value2")
    .delete("key0");

Hawk.db().batch(hawkBatch);
```

### How does Hawk work?

<img src='https://github.com/edwardstock/hawk/blob/master/art/how-hawk-works.png'/>

### More options
- Everything is pluggable, therefore you can change any layer with your custom implementation.
- NoEncryption implementation is provided out of box If you want to disable crypto.
```java
Hawk.init(context)
  .setEncryption(new NoEncryption())
  .setLogInterceptor(new MyLogInterceptor())
  .setConverter(new MyConverter())
  .setParser(new MyParser())
  .setStorage(new MyStorage())
  .build();
```

### License
<pre>
Copyright 2019 Eduard Maximovich

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>


