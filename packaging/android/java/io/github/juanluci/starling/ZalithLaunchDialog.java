package io.github.juanluci.starling;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public final class ZalithLaunchDialog {
    public static void show(final LauncherActivity act, SharedPreferences p, final Runnable onReady) {
        int accent = ZalithUiKit.accent(p);
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER_HORIZONTAL);
        box.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 20)));
        box.setPadding(ZalithUiKit.dp(act, 24), ZalithUiKit.dp(act, 18), ZalithUiKit.dp(act, 24), ZalithUiKit.dp(act, 18));

        TextView title = new TextView(act);
        title.setText("Launch");
        title.setTextColor(0xFFFFFFFF);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        box.addView(title);

        final TextView step = new TextView(act);
        step.setText("📋  Preparing verifications");
        step.setTextColor(0xFFE5E7EB);
        step.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        step.setPadding(0, ZalithUiKit.dp(act, 14), 0, ZalithUiKit.dp(act, 6));
        box.addView(step);

        final ProgressBar bar = new ProgressBar(act, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgress(25);
        box.addView(bar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 8)));

        final TextView sub = new TextView(act);
        sub.setText("🕒  Initializing Rust Horizon 26.3...");
        sub.setTextColor(0xFF9CA3AF);
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        sub.setPadding(0, ZalithUiKit.dp(act, 10), 0, ZalithUiKit.dp(act, 16));
        box.addView(sub);

        final boolean[] cancelled = {false};
        final AlertDialog dlg = new AlertDialog.Builder(act, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setView(box).setCancelable(false).create();
        if (dlg.getWindow() != null) dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button cancel = ZalithUiKit.pillBtn(act, "Cancel", accent, 14);
        cancel.setOnClickListener(v -> { cancelled[0] = true; dlg.dismiss(); });
        box.addView(cancel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 40)));

        dlg.show();
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() -> { if (!cancelled[0]) { bar.setProgress(65); step.setText("📋  Syncing offline profile"); } }, 450);
        h.postDelayed(() -> { if (!cancelled[0]) { bar.setProgress(100); sub.setText("✓  Starting game engine..."); } }, 900);
        h.postDelayed(() -> { if (!cancelled[0]) { dlg.dismiss(); onReady.run(); } }, 1250);
    }
}
