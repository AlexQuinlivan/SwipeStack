/*
 * Copyright (C) 2016 Frederik Schweiger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package link.fls.swipestack;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

class SwipeHelper implements View.OnTouchListener {

    private final SwipeStack mSwipeStack;
    private View mObservedView;

    private boolean mListenForTouchEvents;
    private float mInitialX;
    private float mInitialY;
    private float x1;
    private float y1;

    private float mRotateDegrees = SwipeStack.DEFAULT_SWIPE_ROTATION;
    private float mOpacityEnd = SwipeStack.DEFAULT_SWIPE_OPACITY;
    private int mAnimationDuration = SwipeStack.DEFAULT_ANIMATION_DURATION;

    SwipeHelper(SwipeStack swipeStack) {
        mSwipeStack = swipeStack;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mListenForTouchEvents || !mSwipeStack.isEnabled()) {
                    return false;
                }
                x1 = event.getX();
                y1 = event.getY();
                v.getParent().requestDisallowInterceptTouchEvent(true);
                mSwipeStack.onSwipeStart();

                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - x1;
                float dy = event.getY() - y1;

                float newX = mObservedView.getX() + dx;
                float newY = mObservedView.getY() + dy;

                mObservedView.setX(newX);
                if (mSwipeStack.isAllowVerticalMovement()) {
                    mObservedView.setY(newY);
                }

                float dragDistanceX = newX - mInitialX;
                float swipeProgress = Math.min(Math.max(
                        dragDistanceX / mSwipeStack.getWidth(), -1), 1);

                mSwipeStack.onSwipeProgress(swipeProgress);

                if (mRotateDegrees > 0) {
                    float rotation = mRotateDegrees * swipeProgress;
                    mObservedView.setRotation(rotation);
                }

                if (mOpacityEnd < 1f) {
                    float alpha = 1 - Math.min(Math.abs(swipeProgress * 2), 1);
                    mObservedView.setAlpha(alpha);
                }

                return true;

            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                mSwipeStack.onSwipeEnd();
                checkViewPosition();

                return true;

        }

        return false;
    }

    private void checkViewPosition() {
        if (!mSwipeStack.isEnabled()) {
            resetViewPosition();
            return;
        }

        float viewCenterHorizontal = mObservedView.getX() + (mObservedView.getWidth() / 2);
        float parentFirstThird = mSwipeStack.getWidth() / 3f;
        float parentLastThird = parentFirstThird * 2;

        if (viewCenterHorizontal < parentFirstThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_RIGHT) {
            swipeViewToLeft(mAnimationDuration, true);
        } else if (viewCenterHorizontal > parentLastThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_LEFT) {
            swipeViewToRight(mAnimationDuration, true);
        } else {
            resetViewPosition();
        }
    }

    private void resetViewPosition() {
        mObservedView.animate()
                .x(mInitialX)
                .y(mInitialY)
                .rotation(0)
                .alpha(1)
                .setDuration(mAnimationDuration)
                .setInterpolator(new OvershootInterpolator(1.4f))
                .setListener(null);
    }

    private void swipeViewToLeft(int duration, final boolean notifyListener) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(-mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(-mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToLeft(notifyListener);
                    }
                });
    }

    private void swipeViewToRight(int duration, final boolean notifyListener) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToRight(notifyListener);
                    }
                });
    }

    void registerObservedView(View view, float initialX, float initialY) {
        if (view == null) return;
        mObservedView = view;
        mObservedView.setOnTouchListener(this);
        mInitialX = initialX;
        mInitialY = initialY;
        mListenForTouchEvents = true;
    }

    void unregisterObservedView() {
        if (mObservedView != null) {
            mObservedView.setOnTouchListener(null);
        }
        mObservedView = null;
        mListenForTouchEvents = false;
    }

    void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    void setRotation(float rotation) {
        mRotateDegrees = rotation;
    }

    void setOpacityEnd(float alpha) {
        mOpacityEnd = alpha;
    }

    void swipeViewToLeft(boolean notifyListener) {
        swipeViewToLeft(mAnimationDuration, notifyListener);
    }

    void swipeViewToRight(boolean notifyListener) {
        swipeViewToRight(mAnimationDuration, notifyListener);
    }
}
