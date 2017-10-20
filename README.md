# SwipeStack
A simple, customizable and easy to use swipeable view stack for Android.

[![Build Status](https://travis-ci.org/Jawnnypoo/SwipeStack.svg?branch=master)](https://travis-ci.org/Jawnnypoo/SwipeStack) [![](https://jitpack.io/v/Jawnnypoo/SwipeStack.svg)](https://jitpack.io/#Jawnnypoo/SwipeStack)

![Demo screen 1](https://raw.githubusercontent.com/Jawnnypoo/SwipeStack/master/art/screen1.png)
![Demo animation](https://raw.githubusercontent.com/Jawnnypoo/SwipeStack/master/art/demo.gif)
![Demo screen 2](https://raw.githubusercontent.com/Jawnnypoo/SwipeStack/master/art/screen2.png)

### Use it in your layout file ###
1. Use the `link.fls.swipestack.SwipeStack` view in your XML layout file 
2. Set the parent view's `clipChildren` attribute to `false`

*Example:*

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clipChildren="false">

    <link.fls.swipestack.SwipeStack
        android:id="@+id/swipeStack"
        android:layout_width="320dp"
        android:layout_height="240dp"
        android:padding="32dp"/>

</FrameLayout>
```

### Create an adapter ###

Create an adapter which holds the data and creates the views for the stack.

*Example:*

```java
public static class SwipeStackAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private List<String> data;

    public SwipeStackAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CardViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String getItem(int position) {
        return data.get(position);
    }
}
```

### Assign the adapter to the SwipeStack ###

Last, but not least, assign the adapter to the SwipeStack.

*Example:*

```java
SwipeStack swipeStack = (SwipeStack) findViewById(R.id.swipeStack);
swipeStack.setAdapter(new SwipeStackAdapter(mData));
```

That's it!

## Callbacks ##

Currently SwipeStack implements the following callbacks:

- the ` SwipeStackListener ` notifies you when a view was swiped to the left / right or when the stack becomes empty.
- the ` SwipeProgressListener ` notifies you about the progress when the user starts / stops dragging a view around.

## Attributes ##

*All attributes are optional.*

`allowed_swipe_directions` specifies the allowed swipe directions. *Default: both*

`animation_duration` specifies the duration of the animations. *Default: 300ms*

`stack_size` specifies the maximum number of visible views. *Default: 3*

`stack_spacing` specifies the vertical distance between two views. *Default: 12dp*

`stack_rotation` specifies the maximum random ratation (in degrees) of a card on the stack. *Default: 8*

`swipe_rotation` specifies the rotation (in degrees) of the view when it gets swiped left / right. *Default: 15*

`swipe_opacity` specifies the opacity of the view when it gets swiped left / right. *Default: 1.0*

`scale_factor` specifies the scale factor of the views in the stack. *Default: 1.0*

`disable_hw_acceleration` set to `true` disables hardware acceleration. *Default: false*

## Copyright Notice ##
```
Copyright (C) 2016 Frederik Schweiger

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 ```


License
--------

    Copyright 2017 John Carlson
    Copyright 2016 Frederik Schweiger

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
