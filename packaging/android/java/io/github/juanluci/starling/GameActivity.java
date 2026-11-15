package io.github.juanluci.starling;

import android.os.Bundle;

/** Native game component; StarlingActivity keeps the existing Java/JNI bridge API. */
public final class GameActivity extends StarlingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZalithGameOverlay.attach(this);
    }
}
