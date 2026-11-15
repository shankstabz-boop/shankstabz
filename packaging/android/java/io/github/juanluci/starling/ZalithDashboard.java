package io.github.juanluci.starling;

import android.content.*;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

public final class ZalithDashboard {
    public static void attach(final LauncherActivity act) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) { try { act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); } catch (Exception ignored) {} }
        final SharedPreferences p = act.getSharedPreferences(LauncherActivity.PREFS, Context.MODE_PRIVATE);
        if (!p.contains("accounts_list")) p.edit().putString("accounts_list", "Player").putString(LauncherActivity.KEY_USER, "Player").apply();

        LinearLayout root = new LinearLayout(act); root.setOrientation(LinearLayout.VERTICAL); root.setBackground(ZalithUiKit.bgGradient()); root.setFitsSystemWindows(true);
        root.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 12));

        LinearLayout topBar = new LinearLayout(act); topBar.setGravity(Gravity.CENTER_VERTICAL); topBar.setPadding(0, 0, 0, ZalithUiKit.dp(act, 10));
        final TextView back = new TextView(act); back.setText("← "); back.setTextColor(0xFFFFFFFF); back.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); back.setVisibility(View.GONE);
        topBar.addView(back);

        final ImageView brandIcon = new ImageView(act); brandIcon.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 22)));
        LinearLayout.LayoutParams bilp = ZalithUiKit.lp(ZalithUiKit.dp(act, 22), ZalithUiKit.dp(act, 22), 0f); bilp.rightMargin = ZalithUiKit.dp(act, 8);
        topBar.addView(brandIcon, bilp);

        final TextView title = new TextView(act); title.setText("Oxide"); title.setTextColor(0xFFF3F4F6); title.setTypeface(Typeface.DEFAULT_BOLD); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        topBar.addView(title, ZalithUiKit.lp(0, -2, 1f));

        final FrameLayout host = new FrameLayout(act);
        Runnable goHome = () -> {
            back.setVisibility(View.GONE); brandIcon.setVisibility(View.VISIBLE); title.setText("Oxide"); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); host.removeAllViews();
            View h = buildHome(act, p, back, brandIcon, title, host, () -> open(back, brandIcon, title, host, "Account List", ZalithAccountsView.build(act, p)), () -> open(back, brandIcon, title, host, "Settings - Renderer", ZalithSettingsView.build(act, p, title)));
            host.addView(h); ZalithUiKit.animateIn(h);
        };
        back.setOnClickListener(v -> goHome.run());

        addBtn(act, topBar, 0, v -> open(back, brandIcon, title, host, "File Manager", ZalithFilesView.build(act, title)));
        addBtn(act, topBar, 1, v -> open(back, brandIcon, title, host, "Account List", ZalithAccountsView.build(act, p)));
        addBtn(act, topBar, 2, v -> open(back, brandIcon, title, host, "Terracotta | Multiplayer", ZalithDownloadsView.buildMultiplayer(act, p)));
        addBtn(act, topBar, 3, v -> open(back, brandIcon, title, host, "Download - Game", ZalithDownloadsView.build(act, p, title)));
        addBtn(act, topBar, 4, v -> open(back, brandIcon, title, host, "Settings - Renderer", ZalithSettingsView.build(act, p, title)));
        root.addView(topBar); root.addView(host, ZalithUiKit.lp(-1, 0, 1f));

        act.setContentView(root); ZalithUiKit.makeFullscreen(act); goHome.run();
    }

    private static void addBtn(LauncherActivity act, LinearLayout bar, int type, View.OnClickListener l) {
        ImageView b = ZalithUiKit.navIcon(act, type, l); LinearLayout.LayoutParams lp = ZalithUiKit.lp(-2, -2, 0f); lp.leftMargin = ZalithUiKit.dp(act, 6); bar.addView(b, lp);
    }

    private static void open(TextView back, ImageView brand, TextView title, FrameLayout host, String h, View v) {
        back.setVisibility(View.VISIBLE); brand.setVisibility(View.GONE); title.setText(h); title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); host.removeAllViews(); host.addView(v); ZalithUiKit.animateIn(v);
    }

    private static View buildHome(final LauncherActivity act, final SharedPreferences p, final TextView back, final ImageView brand, final TextView title, final FrameLayout host, Runnable onAcc, Runnable onSet) {
        int accent = ZalithUiKit.accent(p);
        LinearLayout body = new LinearLayout(act);
        LinearLayout left = new LinearLayout(act); left.setOrientation(LinearLayout.VERTICAL); body.addView(left, ZalithUiKit.lp(0, -1, 1.55f));

        LinearLayout card = new LinearLayout(act); card.setOrientation(LinearLayout.VERTICAL); card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
        card.setPadding(ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 12));
        left.addView(card, ZalithUiKit.lp(ZalithUiKit.dp(act, 195), -2, 0f));

        LinearLayout row = new LinearLayout(act); row.setGravity(Gravity.CENTER_VERTICAL);
        ImageView ic = new ImageView(act); ic.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 32))); row.addView(ic, ZalithUiKit.lp(ZalithUiKit.dp(act, 32), ZalithUiKit.dp(act, 32), 0f));
        LinearLayout meta = new LinearLayout(act); meta.setOrientation(LinearLayout.VERTICAL); meta.setPadding(ZalithUiKit.dp(act, 9), 0, 0, 0);
        TextView n = new TextView(act); n.setText("Rust Optimized 26.3"); n.setTextColor(0xFFFFFFFF); n.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); n.setTypeface(Typeface.DEFAULT_BOLD); meta.addView(n);
        TextView v = new TextView(act); v.setText("26.3  •  Horizon Rust"); v.setTextColor(0xFF9CA3AF); v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10); meta.addView(v);
        row.addView(meta); row.setOnClickListener(x -> open(back, brand, title, host, "Version List", ZalithVersionsView.buildList(act, p, title))); card.addView(row);

        Button lPlay = ZalithUiKit.pillBtn(act, "▶  Launch", accent, 11); LinearLayout.LayoutParams llp = ZalithUiKit.lp(-1, ZalithUiKit.dp(act, 30), 0f); llp.topMargin = ZalithUiKit.dp(act, 10);
        lPlay.setOnClickListener(x -> ZalithLaunchDialog.show(act, p, () -> act.play())); card.addView(lPlay, llp);

        LinearLayout rCard = new LinearLayout(act); rCard.setOrientation(LinearLayout.VERTICAL); rCard.setGravity(Gravity.CENTER_HORIZONTAL);
        rCard.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 16))); rCard.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
        LinearLayout.LayoutParams rlp = ZalithUiKit.lp(0, -1, 1f); rlp.leftMargin = ZalithUiKit.dp(act, 14); body.addView(rCard, rlp);

        rCard.addView(new View(act), ZalithUiKit.lp(1, 0, 0.3f));
        ImageView head = new ImageView(act); head.setImageBitmap(ZalithUiKit.skinHead(ZalithUiKit.dp(act, 48))); ZalithUiKit.addBounce(head); head.setOnClickListener(x -> onAcc.run());
        rCard.addView(head, ZalithUiKit.lp(ZalithUiKit.dp(act, 48), ZalithUiKit.dp(act, 48), 0f));

        TextView uName = new TextView(act); uName.setText(p.getString(LauncherActivity.KEY_USER, "Player")); uName.setTextColor(0xFFFFFFFF); uName.setTypeface(Typeface.DEFAULT_BOLD); uName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        uName.setPadding(0, ZalithUiKit.dp(act, 6), 0, ZalithUiKit.dp(act, 1)); uName.setOnClickListener(x -> onAcc.run()); rCard.addView(uName);
        TextView off = new TextView(act); off.setText("Offline"); off.setTextColor(0xFF9CA3AF); off.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10); rCard.addView(off);
        rCard.addView(new View(act), ZalithUiKit.lp(1, 0, 1f));

        LinearLayout vRow = new LinearLayout(act); vRow.setGravity(Gravity.CENTER_VERTICAL); vRow.setPadding(ZalithUiKit.dp(act, 2), 0, ZalithUiKit.dp(act, 2), ZalithUiKit.dp(act, 8));
        ImageView sm = new ImageView(act); sm.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 22))); vRow.addView(sm, ZalithUiKit.lp(ZalithUiKit.dp(act, 22), ZalithUiKit.dp(act, 22), 0f));
        LinearLayout vc = new LinearLayout(act); vc.setOrientation(LinearLayout.VERTICAL); vc.setPadding(ZalithUiKit.dp(act, 7), 0, 0, 0);
        TextView vt = new TextView(act); vt.setText("Rust Optimized 26.3"); vt.setTextColor(0xFFFFFFFF); vt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11); vc.addView(vt);
        TextView vs = new TextView(act); vs.setText("26.3, Rust - 0.8.0"); vs.setTextColor(0xFF9CA3AF); vs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9); vc.addView(vs);
        vRow.addView(vc, ZalithUiKit.lp(0, -2, 1f));

        ImageView gear = new ImageView(act); gear.setImageBitmap(ZalithUiKit.drawGlyph(ZalithUiKit.dp(act, 18), 4, 0xFFD97736)); ZalithUiKit.addBounce(gear); gear.setOnClickListener(x -> onSet.run()); vRow.addView(gear);
        rCard.addView(vRow, ZalithUiKit.lp(-1, -2, 0f));

        Button mPlay = ZalithUiKit.pillBtn(act, "Launch", accent, 12); mPlay.setOnClickListener(x -> ZalithLaunchDialog.show(act, p, () -> act.play()));
        rCard.addView(mPlay, ZalithUiKit.lp(-1, ZalithUiKit.dp(act, 34), 0f));
        return body;
    }
            }
