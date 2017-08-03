# android-actionbar-compat
This compatibility library is based on the AOSP ActionBarCompat sample project.

It makes the ActionBar API available to Froyo+ devices. You can define the state of the home icon as a navigation item programmatically.

## Setup
Import the project folder as a library into Eclipse ADT.

## Usage
- Add this library to the build path of your Android project.
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
