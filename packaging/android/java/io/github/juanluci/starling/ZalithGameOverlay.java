package io.github.juanluci.starling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public final class ZalithGameOverlay {
    public static void attach(final Activity act) {
        final SharedPreferences p = act.getSharedPreferences(LauncherActivity.PREFS, Context.MODE_PRIVATE);
        FrameLayout root = new FrameLayout(act);

        final LinearLayout topPill = new LinearLayout(act);
        topPill.setGravity(Gravity.CENTER_VERTICAL);
        topPill.setBackground(ZalithUiKit.roundRect(0xAA1E1F22, ZalithUiKit.dp(act, 14)));
        topPill.setPadding(ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 4), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 4));
        final TextView pillText = new TextView(act);
        pillText.setText("☰  FPS: 60");
        pillText.setTextColor(0xFFFFFFFF);
        pillText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        topPill.addView(pillText);
        FrameLayout.LayoutParams plp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        plp.topMargin = ZalithUiKit.dp(act, 6);
        root.addView(topPill, plp);

        final LinearLayout drawer = new LinearLayout(act);
        drawer.setOrientation(LinearLayout.HORIZONTAL);
        drawer.setBackgroundColor(0x99000000);
        drawer.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 36), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16));
        drawer.setVisibility(View.GONE);

        LinearLayout leftPanel = new LinearLayout(act);
        leftPanel.setOrientation(LinearLayout.VERTICAL);
        leftPanel.setBackground(ZalithUiKit.roundRect(ZalithUiKit.BG_DARK, ZalithUiKit.dp(act, 18)));
        leftPanel.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        llp.rightMargin = ZalithUiKit.dp(act, 14);

        LinearLayout tabBar = new LinearLayout(act);
        final LinearLayout leftBody = new LinearLayout(act);
        leftBody.setOrientation(LinearLayout.VERTICAL);
        String[] icons = {"⊞", "🖱", "🎮", "👆", "🧭"};
        for (int i = 0; i < icons.length; i++) {
            final int idx = i;
            TextView t = new TextView(act);
            t.setText(icons[i]);
            t.setGravity(Gravity.CENTER);
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            t.setPadding(0, ZalithUiKit.dp(act, 6), 0, ZalithUiKit.dp(act, 8));
            t.setOnClickListener(v -> renderLeft(act, p, idx, leftBody));
            tabBar.addView(t, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        }
        leftPanel.addView(tabBar);
        ScrollView lSc = new ScrollView(act);
        lSc.addView(leftBody);
        leftPanel.addView(lSc);
        drawer.addView(leftPanel, llp);

        ScrollView rSc = new ScrollView(act);
        LinearLayout rightPanel = new LinearLayout(act);
        rightPanel.setOrientation(LinearLayout.VERTICAL);
        rightPanel.setBackground(ZalithUiKit.roundRect(ZalithUiKit.BG_DARK, ZalithUiKit.dp(act, 18)));
        rightPanel.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
        rSc.addView(rightPanel);

        TextView gmTitle = new TextView(act);
        gmTitle.setText("Game Menu");
        gmTitle.setTextColor(0xFFFFFFFF);
        gmTitle.setTypeface(Typeface.DEFAULT_BOLD);
        gmTitle.setGravity(Gravity.CENTER);
        gmTitle.setPadding(0, 0, 0, ZalithUiKit.dp(act, 10));
        rightPanel.addView(gmTitle);

        Button forceClose = ZalithUiKit.pillBtn(act, "Force Close", 0xFF35373C, 13);
        forceClose.setTextColor(0xFFF87171);
        forceClose.setOnClickListener(v -> android.os.Process.killProcess(android.os.Process.myPid()));
        rightPanel.addView(forceClose, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 40)));

        rightPanel.addView(rowSwitch(act, "Enable Menu Overlay", true, (b, c) -> topPill.setAlpha(c ? 1f : 0.15f)));
        rightPanel.addView(rowSwitch(act, "FPS Display", true, (b, c) -> pillText.setText(c ? "☰  FPS: 60" : "☰  Menu")));
        rightPanel.addView(rowSwitch(act, "Memory Display", false, (b, c) -> pillText.setText(c ? "☰  FPS: 60 | RAM: OK" : "☰  FPS: 60")));
        drawer.addView(rSc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        root.addView(drawer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        topPill.setOnClickListener(v -> drawer.setVisibility(drawer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        renderLeft(act, p, 1, leftBody);

        act.addContentView(root, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private static void renderLeft(Activity act, SharedPreferences p, int idx, LinearLayout box) {
        box.removeAllViews();
        if (idx == 1) {
            int cur = p.getInt(LauncherActivity.KEY_DPI, 100);
            final TextView lbl = new TextView(act);
            lbl.setText("Captured Mouse Movement Sens: " + cur + "%");
            lbl.setTextColor(0xFFFFFFFF);
            box.addView(lbl);
            SeekBar sb = new SeekBar(act);
            sb.setMax(275);
            sb.setProgress(Math.max(0, cur - 25));
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override public void onProgressChanged(SeekBar s, int pr, boolean u) {
                    int v = pr + 25;
                    lbl.setText("Captured Mouse Movement Sens: " + v + "%");
                    p.edit().putInt(LauncherActivity.KEY_DPI, v).apply();
                }
                @Override public void onStartTrackingTouch(SeekBar s) {}
                @Override public void onStopTrackingTouch(SeekBar s) {}
            });
            box.addView(sb);
        } else if (idx == 4) {
            box.addView(rowSwitch(act, "Gyroscope Control", false, null));
            box.addView(rowSwitch(act, "Invert X-Axis", false, null));
            box.addView(rowSwitch(act, "Invert Y-Axis", false, null));
        } else {
            TextView info = new TextView(act);
            info.setText(idx == 0 ? "Control Layout Opacity: 15%" : idx == 2 ? "Gamepad Deadzone: 100%" : "Hotbar Width: 50% • Height: 10%");
            info.setTextColor(0xFFD1D5DB);
            info.setPadding(0, ZalithUiKit.dp(act, 12), 0, 0);
            box.addView(info);
        }
    }

    private static View rowSwitch(Activity act, String txt, boolean def, CompoundButton.OnCheckedChangeListener l) {
        LinearLayout r = new LinearLayout(act);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 12)));
        r.setPadding(ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 10), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 10));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = ZalithUiKit.dp(act, 8);
        r.setLayoutParams(lp);
        TextView t = new TextView(act);
        t.setText(txt);
        t.setTextColor(0xFFFFFFFF);
        r.addView(t, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        Switch s = new Switch(act);
        s.setChecked(def);
        if (l != null) s.setOnCheckedChangeListener(l);
        r.addView(s);
        return r;
    }
}
