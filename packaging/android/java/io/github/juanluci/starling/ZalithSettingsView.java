package io.github.juanluci.starling;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public final class ZalithSettingsView {
    public static View build(final LauncherActivity act, final SharedPreferences p, final TextView titleView) {
        LinearLayout body = new LinearLayout(act);
        body.setOrientation(LinearLayout.HORIZONTAL);

        ScrollView sideSc = new ScrollView(act);
        LinearLayout side = new LinearLayout(act);
        side.setOrientation(LinearLayout.VERTICAL);
        sideSc.addView(side);
        body.addView(sideSc, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 105), ViewGroup.LayoutParams.MATCH_PARENT));

        final LinearLayout content = new LinearLayout(act);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(ZalithUiKit.dp(act, 12), 0, 0, 0);

        String[] tabs = {"Renderer", "Game", "Controls", "Gamepad", "Launcher", "About"};
        for (final String tab : tabs) {
            TextView btn = new TextView(act);
            btn.setText(tab);
            btn.setTextColor(0xFFD1D5DB);
            btn.setGravity(Gravity.CENTER);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            btn.setPadding(ZalithUiKit.dp(act, 6), ZalithUiKit.dp(act, 11), ZalithUiKit.dp(act, 6), ZalithUiKit.dp(act, 11));
            btn.setOnClickListener(v -> {
                titleView.setText("Settings - " + tab);
                renderTab(act, p, tab, content);
            });
            side.addView(btn);
        }

        ScrollView mainSc = new ScrollView(act);
        mainSc.addView(content);
        body.addView(mainSc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        renderTab(act, p, "Renderer", content);
        return body;
    }

    private static void renderTab(LauncherActivity act, SharedPreferences p, String tab, LinearLayout box) {
        box.removeAllViews();
        if ("Renderer".equals(tab)) {
            box.addView(infoCard(act, "Global Renderer", "Selected: Native Rust Vulkan / GLES Engine"));
            box.addView(infoCard(act, "Graphics API", "Selected: Vulkan Hardware Accelerated (26.3)"));
        } else if ("Game".equals(tab)) {
            box.addView(switchCard(act, p, "Global Version Isolation", "Isolates Rust 26.3 worlds and mods", "ver_iso", true));
            box.addView(switchCard(act, p, "Skip Integrity Check", "Launch immediately without re-verifying assets", "skip_check", false));
        } else if ("Controls".equals(tab)) {
            box.addView(switchCard(act, p, "Physical Mouse & W-Key Lock", "Keeps PointerCapture locked while walking with W/A/S/D", LauncherActivity.KEY_LOCK, true));
            box.addView(sliderCard(act, p, "Mouse Sensitivity Scale", LauncherActivity.KEY_DPI, 100, 275, 25));
        } else if ("Gamepad".equals(tab)) {
            box.addView(switchCard(act, p, "Gamepad Control", "Enable native USB/Bluetooth controller input", "pad_on", true));
            box.addView(sliderCard(act, p, "Gamepad Deadzone Scaling", "pad_deadzone", 100, 100, 0));
        } else if ("Launcher".equals(tab)) {
            String[] themes = {"Dynamic", "Embermire", "Velvet Rose", "Mistwave", "Glacier"};
            for (final String th : themes) {
                LinearLayout c = infoCard(act, "Color Theme: " + th, "Tap to apply " + th + " accent color");
                c.setOnClickListener(v -> {
                    p.edit().putString("color_theme", th).apply();
                    Toast.makeText(act, "Theme set to " + th + " (reopen screen to refresh)", Toast.LENGTH_SHORT).show();
                });
                box.addView(c);
            }
        } else {
            box.addView(infoCard(act, "Zalith Launcher (Rust Edition)", "Launcher UI: 2.6_zl • Engine: Starling Horizon 26.3"));
            box.addView(infoCard(act, "Credits", "Inspired by MovTery's ZalithLauncher 2 • Built on Starling Rust"));
        }
    }

    private static LinearLayout infoCard(LauncherActivity act, String title, String sub) {
        LinearLayout c = new LinearLayout(act);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
        c.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = ZalithUiKit.dp(act, 10);
        c.setLayoutParams(lp);

        TextView t1 = new TextView(act);
        t1.setText(title);
        t1.setTextColor(0xFFFFFFFF);
        t1.setTypeface(Typeface.DEFAULT_BOLD);
        c.addView(t1);

        TextView t2 = new TextView(act);
        t2.setText(sub);
        t2.setTextColor(0xFF9CA3AF);
        t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        c.addView(t2);
        return c;
    }

    private static View switchCard(LauncherActivity act, SharedPreferences p, String title, String sub, String key, boolean def) {
        LinearLayout c = infoCard(act, title, sub);
        Switch sw = new Switch(act);
        sw.setChecked(p.getBoolean(key, def));
        sw.setOnCheckedChangeListener((b, ch) -> p.edit().putBoolean(key, ch).apply());
        c.addView(sw);
        return c;
    }

    private static View sliderCard(LauncherActivity act, SharedPreferences p, String title, String key, int def, int max, int min) {
        int cur = p.getInt(key, def);
        final LinearLayout c = infoCard(act, title + ": " + cur + "%", "Adjust sensitivity or deadzone percentage");
        final TextView head = (TextView) c.getChildAt(0);
        SeekBar sb = new SeekBar(act);
        sb.setMax(max);
        sb.setProgress(Math.max(0, cur - min));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int pr, boolean u) {
                int val = pr + min;
                head.setText(title + ": " + val + "%");
                p.edit().putInt(key, val).apply();
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
        c.addView(sb);
        return c;
    }
}
