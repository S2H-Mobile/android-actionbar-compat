# android-actionbar-compat
This compatibility library is based on the AOSP [ActionBarCompat sample project](https://android.googlesource.com/platform/development/+/f84558cf16844b6c96e0744544ff2094bc4dbf50/samples/ActionBarCompat).

It makes the ActionBar API available to Froyo+ devices. You can define the state of the home icon as a navigation item programmatically.

## Setup
- Import the project folder as a library into the Eclipse ADT workspace.
- Add the library to an existing Android application: 

1. Right-click on your application in Eclipse -> _Properties_
2. Select _Android_ -> _Library_
3. Click on _Add ..._
4. Select the library and click _Ok_

## Usage
- Extend your activity from one of the provided base activities.
- Implement the necessary methods in your activity.

## Example


```java
package com.example.actionbar;

import de.s2hmobile.compat.ActionBarActivity;

import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

  @Override
  public boolean isHomeStateful() {
    return true;
  }

  @Override
  public int setHomeResId() {
    return R.drawable.home;
  }
}
```
