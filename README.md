Swap
====

An EditText with the ability to swap the keyboard with custom fragments

![](screenshot.png){:height="36px" width="36px"}

Where to Download
-----------------
```groovy
dependencies {
  compile 'com.xlythe:swap:0.0.1'
}
```

Getting Started
---------------
```java
public class MainActivity extends AppCompatActivity {
    SwapEditText mEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (SwapEditText) findViewById(R.id.edit_text);
    }

    @Override
    public void onBackPressed() {
        if (mEditText.getFragmentVisibility()) {
            mEditText.hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}
```

Getting Started
---------------
```xml
<com.xlythe.swap.SwapEditText
    android:id="@+id/edit_text"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
        
<FrameLayout
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
