package io.github.juanluci.starling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class ZalithDashboard {
    public static void attach(final LauncherActivity act) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            try { act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); } catch (Exception ignored) {}
        }
        final SharedPreferences p = act.getSharedPreferences(LauncherActivity.PREFS, Context.MODE_PRIVATE);
        if (!p.contains("accounts_list")) p.edit().putString("accounts_list", "Player").putString(LauncherActivity.KEY_USER, "Player").apply();

        LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(ZalithUiKit.BG_DARK);
        root.setFitsSystemWindows(true);
        root.setPadding(ZalithUiKit.dp(act, 24), ZalithUiKit.dp(act, 22), ZalithUiKit.dp(act, 24), ZalithUiKit.dp(act, 16));

        LinearLayout topBar = new LinearLayout(act);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setPadding(0, 0, 0, ZalithUiKit.dp(act, 14));

        final TextView back = new TextView(act);
        back.setText("←  "); back.setTextColor(0xFFFFFFFF); back.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); back.setVisibility(View.GONE);
        topBar.addView(back);

        final TextView title = new TextView(act);
        title.setText("Oxide"); title.setTextColor(0xFFF3F4F6); title.setTypeface(Typeface.DEFAULT_BOLD); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        topBar.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        final FrameLayout host = new FrameLayout(act);
        Runnable goHome = () -> {
            back.setVisibility(View.GONE); title.setText("Oxide"); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            host.removeAllViews();
            View h = buildHome(act, p, back, title, host,
                    () -> open(back, title, host, "Account List", ZalithAccountsView.build(act, p)),
                    () -> open(back, title, host, "Settings - Renderer", ZalithSettingsView.build(act, p, title)));
            host.addView(h); ZalithUiKit.animateIn(h);
        };
        back.setOnClickListener(v -> goHome.run());

        addBtn(act, topBar, "📁", v -> open(back, title, host, "File Manager", ZalithFilesView.build(act, title)));
        addBtn(act, topBar, "👤", v -> open(back, title, host, "Account List", ZalithAccountsView.build(act, p)));
        addBtn(act, topBar, "🌐", v -> open(back, title, host, "Terracotta | Multiplayer", ZalithDownloadsView.buildMultiplayer(act, p)));
        addBtn(act, topBar, "⬇", v -> open(back, title, host, "Download - Game", ZalithDownloadsView.build(act, p, title)));
        addBtn(act, topBar, "⚙", v -> open(back, title, host, "Settings - Renderer", ZalithSettingsView.build(act, p, title)));
        root.addView(topBar);
        root.addView(host, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        act.setContentView(root);
        ZalithUiKit.makeFullscreen(act);
        goHome.run();
    }

    private static void addBtn(LauncherActivity act, LinearLayout bar, String ic, View.OnClickListener l) {
        TextView b = ZalithUiKit.topIcon(act, ic, l);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = ZalithUiKit.dp(act, 8);
        bar.addView(b, lp);
    }

    private static void open(TextView back, TextView title, FrameLayout host, String h, View v) {
        back.setVisibility(View.VISIBLE); title.setText(h); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        host.removeAllViews(); host.addView(v); ZalithUiKit.animateIn(v);
    }

    private static View buildHome(final LauncherActivity act, final SharedPreferences p,
            final TextView back, final TextView title, final FrameLayout host, Runnable onAcc, Runnable onSet) {
        int accent = ZalithUiKit.accent(p);
        LinearLayout body = new LinearLayout(act);
        LinearLayout left = new LinearLayout(act); left.setOrientation(LinearLayout.VERTICAL);
        body.addView(left, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.45f));

        LinearLayout card = new LinearLayout(act); card.setOrientation(LinearLayout.VERTICAL);
        card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 18)));
        card.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16));
        left.addView(card, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 240), ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout row = new LinearLayout(act); row.setGravity(Gravity.CENTER_VERTICAL);
        ImageView ic = new ImageView(act); ic.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 42)));
        row.addView(ic, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 42), ZalithUiKit.dp(act, 42)));

        LinearLayout meta = new LinearLayout(act); meta.setOrientation(LinearLayout.VERTICAL); meta.setPadding(ZalithUiKit.dp(act, 12), 0, 0, 0);
        TextView n = new TextView(act); n.setText("Rust Optimized 26.3"); n.setTextColor(0xFFFFFFFF); n.setTypeface(Typeface.DEFAULT_BOLD); meta.addView(n);
        TextView v = new TextView(act); v.setText("26.3   Horizon Rust"); v.setTextColor(0xFF9CA3AF); v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11); meta.addView(v);
        row.addView(meta); row.setOnClickListener(x -> open(back, title, host, "Version List", ZalithVersionsView.buildList(act, p, title)));
        card.addView(row);

        Button lPlay = ZalithUiKit.pillBtn(act, "▶  Launch", accent, 13);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 38));
        llp.topMargin = ZalithUiKit.dp(act, 14); lPlay.setOnClickListener(x -> ZalithLaunchDialog.show(act, p, () -> act.play()));
        card.addView(lPlay, llp);

        LinearLayout rCard = new LinearLayout(act); rCard.setOrientation(LinearLayout.VERTICAL); rCard.setGravity(Gravity.CENTER_HORIZONTAL);
        rCard.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 22)));
        rCard.setPadding(ZalithUiKit.dp(act, 18), ZalithUiKit.dp(act, 18), ZalithUiKit.dp(act, 18), ZalithUiKit.dp(act, 18));
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        rlp.leftMargin = ZalithUiKit.dp(act, 18); body.addView(rCard, rlp);

        rCard.addView(new View(act), new LinearLayout.LayoutParams(1, 0, 0.35f));
        ImageView head = new ImageView(act); head.setImageBitmap(ZalithUiKit.skinHead(ZalithUiKit.dp(act, 68)));
        ZalithUiKit.addBounce(head); head.setOnClickListener(x -> onAcc.run());
        rCard.addView(head, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 68), ZalithUiKit.dp(act, 68)));

        TextView uName = new TextView(act); uName.setText(p.getString(LauncherActivity.KEY_USER, "Player"));
        uName.setTextColor(0xFFFFFFFF); uName.setTypeface(Typeface.DEFAULT_BOLD); uName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        uName.setPadding(0, ZalithUiKit.dp(act, 8), 0, ZalithUiKit.dp(act, 2)); uName.setOnClickListener(x -> onAcc.run());
        rCard.addView(uName);

        TextView off = new TextView(act); off.setText("Offline"); off.setTextColor(0xFF9CA3AF); off.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        rCard.addView(off); rCard.addView(new View(act), new LinearLayout.LayoutParams(1, 0, 1f));

        LinearLayout vRow = new LinearLayout(act); vRow.setGravity(Gravity.CENTER_VERTICAL); vRow.setPadding(ZalithUiKit.dp(act, 4), 0, ZalithUiKit.dp(act, 4), ZalithUiKit.dp(act, 10));
        ImageView sm = new ImageView(act); sm.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 26)));
        vRow.addView(sm, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 26), ZalithUiKit.dp(act, 26)));

        LinearLayout vc = new LinearLayout(act); vc.setOrientation(LinearLayout.VERTICAL); vc.setPadding(ZalithUiKit.dp(act, 8), 0, 0, 0);
        TextView vt = new TextView(act); vt.setText("Rust Optimized 26.3"); vt.setTextColor(0xFFFFFFFF); vt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); vc.addView(vt);
        TextView vs = new TextView(act); vs.setText("26.3, Rust - 0.8.0"); vs.setTextColor(0xFF9CA3AF); vs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10); vc.addView(vs);
        vRow.addView(vc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView gear = new TextView(act); gear.setText("⚙"); gear.setTextColor(0xFFE5E7EB); gear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        gear.setOnClickListener(x -> onSet.run()); vRow.addView(gear);
        rCard.addView(vRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button mPlay = ZalithUiKit.pillBtn(act, "Launch", accent, 14);
        mPlay.setOnClickListener(x -> ZalithLaunchDialog.show(act, p, () -> act.play()));
        rCard.addView(mPlay, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 42)));
        return body;
    }
}
