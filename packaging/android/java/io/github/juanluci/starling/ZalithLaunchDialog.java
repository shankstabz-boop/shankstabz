package io.github.juanluci.starling;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.*;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ZalithLaunchDialog {
    public static void show(final LauncherActivity act, final SharedPreferences p, final Runnable onReady) {
        int accent = ZalithUiKit.accent(p);
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL); box.setGravity(Gravity.CENTER_HORIZONTAL);
        box.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 16)));
        box.setPadding(ZalithUiKit.dp(act, 20), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 20), ZalithUiKit.dp(act, 14));

        TextView title = new TextView(act); title.setText("Launch"); title.setTextColor(0xFFFFFFFF);
        title.setTypeface(Typeface.DEFAULT_BOLD); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); box.addView(title);

        final TextView step = new TextView(act); step.setText("Preparing verifications..."); step.setTextColor(0xFFE5E7EB);
        step.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); step.setPadding(0, ZalithUiKit.dp(act, 10), 0, ZalithUiKit.dp(act, 6)); box.addView(step);

        final ProgressBar bar = new ProgressBar(act, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100); bar.setProgress(30); box.addView(bar, ZalithUiKit.lp(-1, ZalithUiKit.dp(act, 6), 0f));

        final TextView sub = new TextView(act); sub.setText("Syncing offline account to Oxide..."); sub.setTextColor(0xFF9CA3AF);
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11); sub.setPadding(0, ZalithUiKit.dp(act, 8), 0, ZalithUiKit.dp(act, 12)); box.addView(sub);

        final AtomicBoolean cancelled = new AtomicBoolean(false);
        final AlertDialog dlg = new AlertDialog.Builder(act, android.R.style.Theme_DeviceDefault_Dialog_Alert).setView(box).setCancelable(false).create();
        if (dlg.getWindow() != null) dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button cancel = ZalithUiKit.pillBtn(act, "Cancel", accent, 12);
        cancel.setOnClickListener(v -> { cancelled.set(true); dlg.dismiss(); });
        box.addView(cancel, ZalithUiKit.lp(-1, ZalithUiKit.dp(act, 34), 0f));

        dlg.show();
        writeStarlingAccounts(act, p);

        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() -> { if (!cancelled.get()) { bar.setProgress(75); step.setText("Profile ready: " + p.getString(LauncherActivity.KEY_USER, "Player")); } }, 400);
        h.postDelayed(() -> { if (!cancelled.get()) { bar.setProgress(100); sub.setText("Starting Horizon Rust 26.3..."); } }, 800);
        h.postDelayed(() -> { if (!cancelled.get()) { dlg.dismiss(); onReady.run(); } }, 1100);
    }

    private static void writeStarlingAccounts(LauncherActivity act, SharedPreferences p) {
        try {
            String active = p.getString(LauncherActivity.KEY_USER, "Player").replace("\"", "");
            StringBuilder arr = new StringBuilder("[");
            for (String raw : p.getString("accounts_list", active).split(",")) {
                String n = raw.trim().replace("\"", "");
                if (!n.isEmpty()) { if (arr.length() > 1) arr.append(","); arr.append("\"").append(n).append("\""); }
            }
            arr.append("]");
            String json = "{\"accounts\":[],\"selected\":null,\"offline\":" + arr + ",\"offline_selected\":\"" + active + "\"}\n";
            try (FileOutputStream fos = new FileOutputStream(new File(act.getFilesDir(), "accounts.json"))) { fos.write(json.getBytes(StandardCharsets.UTF_8)); }
        } catch (Exception ignored) {}
    }
}
