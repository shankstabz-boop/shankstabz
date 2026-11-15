package io.github.juanluci.starling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public final class ZalithDashboard {
    public static void attach(final LauncherActivity act) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            try { act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); } catch (Exception ignored) {}
        }
        final SharedPreferences p = act.getSharedPreferences(LauncherActivity.PREFS, Context.MODE_PRIVATE);

        LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setBackgroundColor(0xFF101216);

        // Left Sidebar (Zalith Style)
        LinearLayout side = new LinearLayout(act);
        side.setOrientation(LinearLayout.VERTICAL);
        side.setBackgroundColor(0xFF171A21);
        side.setPadding(dp(act, 16), dp(act, 20), dp(act, 16), dp(act, 20));
        root.addView(side, new LinearLayout.LayoutParams(dp(act, 200), ViewGroup.LayoutParams.MATCH_PARENT));

        TextView title = new TextView(act);
        title.setText("RUST LAUNCHER");
        title.setTextColor(0xFF4ADE80);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        side.addView(title);

        TextView sub = new TextView(act);
        sub.setText("Offline Mode • 26.3");
        sub.setTextColor(0xFF9CA3AF);
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        sub.setPadding(0, dp(act, 4), 0, dp(act, 16));
        side.addView(sub);

        TextView badge = new TextView(act);
        badge.setText("✓ No Microsoft Login\n✓ Native Mouse Lock");
        badge.setTextColor(0xFF6EE7B7);
        badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        side.addView(badge);

        // Right Main Panel
        LinearLayout main = new LinearLayout(act);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(dp(act, 22), dp(act, 18), dp(act, 22), dp(act, 18));
        root.addView(main, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        // Card 1: Offline Account Box
        LinearLayout accCard = card(act);
        TextView accHead = label(act, "OFFLINE PLAYER NAME (CRACKED / NO MICROSOFT)", 12, 0xFF9CA3AF);
        accCard.addView(accHead);

        final EditText userBox = new EditText(act);
        userBox.setText(p.getString(LauncherActivity.KEY_USER, "Player"));
        userBox.setTextColor(0xFFFFFFFF);
        userBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        userBox.setBackground(pill(0xFF101216, dp(act, 8)));
        userBox.setPadding(dp(act, 12), dp(act, 10), dp(act, 12), dp(act, 10));
        accCard.addView(userBox);
        main.addView(accCard);

        // Card 2: Mouse & Keyboard Controls
        LinearLayout ctrlCard = card(act);
        int savedDpi = p.getInt(LauncherActivity.KEY_DPI, 100);
        final TextView dpiText = label(act, "Mouse DPI Sensitivity Scale: " + savedDpi + "%", 13, 0xFFFFFFFF);
        ctrlCard.addView(dpiText);

        SeekBar bar = new SeekBar(act);
        bar.setMax(275);
        bar.setProgress(Math.max(0, savedDpi - 25));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int prog, boolean u) {
                int val = prog + 25;
                dpiText.setText("Mouse DPI Sensitivity Scale: " + val + "%");
                p.edit().putInt(LauncherActivity.KEY_DPI, val).apply();
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
        ctrlCard.addView(bar);

        final CheckBox lockBox = new CheckBox(act);
        lockBox.setText("Lock Pointer Capture while holding W/A/S/D");
        lockBox.setTextColor(0xFFE5E7EB);
        lockBox.setChecked(p.getBoolean(LauncherActivity.KEY_LOCK, true));
        lockBox.setOnClickListener(v -> p.edit().putBoolean(LauncherActivity.KEY_LOCK, lockBox.isChecked()).apply());
        ctrlCard.addView(lockBox);
        main.addView(ctrlCard);

        // Bottom PLAY Bar
        Button playBtn = new Button(act);
        playBtn.setText("PLAY OFFLINE");
        playBtn.setTextColor(0xFF062814);
        playBtn.setTypeface(Typeface.DEFAULT_BOLD);
        playBtn.setBackground(pill(0xFF4ADE80, dp(act, 10)));
        playBtn.setPadding(dp(act, 28), dp(act, 12), dp(act, 28), dp(act, 12));
        playBtn.setOnClickListener(v -> {
            String name = userBox.getText().toString().trim().replaceAll("[^a-zA-Z0-9_]", "");
            if (name.isEmpty()) name = "Player";
            p.edit().putString(LauncherActivity.KEY_USER, name).apply();
            Toast.makeText(act, "Launching as " + name, Toast.LENGTH_SHORT).show();
            act.play();
        });
        main.addView(playBtn);

        act.setContentView(root);
    }

    private static LinearLayout card(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackground(pill(0xFF171A21, dp(c, 12)));
        l.setPadding(dp(c, 14), dp(c, 12), dp(c, 14), dp(c, 12));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(c, 12);
        l.setLayoutParams(lp);
        return l;
    }

    private static TextView label(Context c, String txt, int sp, int col) {
        TextView t = new TextView(c);
        t.setText(txt);
        t.setTextColor(col);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        t.setPadding(0, 0, 0, dp(c, 6));
        return t;
    }

    private static GradientDrawable pill(int col, int r) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(col);
        d.setCornerRadius(r);
        return d;
    }

    private static int dp(Context c, int v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics());
    }
}
