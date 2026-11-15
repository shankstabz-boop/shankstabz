package io.github.juanluci.starling;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public final class ZalithDownloadsView {
    public static View build(final LauncherActivity act, final SharedPreferences p, final TextView titleView) {
        int accent = ZalithUiKit.accent(p);
        LinearLayout body = new LinearLayout(act);
        body.setOrientation(LinearLayout.HORIZONTAL);

        ScrollView sideSc = new ScrollView(act);
        LinearLayout side = new LinearLayout(act);
        side.setOrientation(LinearLayout.VERTICAL);
        sideSc.addView(side);
        body.addView(sideSc, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 105), ViewGroup.LayoutParams.MATCH_PARENT));

        final LinearLayout listCol = new LinearLayout(act);
        listCol.setOrientation(LinearLayout.VERTICAL);
        listCol.setPadding(ZalithUiKit.dp(act, 12), 0, 0, 0);

        String[] cats = {"Game", "Modpack", "Mod", "Resource Pack", "Saves", "Shader Pack", "Search by ID", "Favorites"};
        for (final String c : cats) {
            TextView tab = new TextView(act);
            tab.setText(c);
            tab.setTextColor(0xFFD1D5DB);
            tab.setGravity(Gravity.CENTER);
            tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            tab.setPadding(ZalithUiKit.dp(act, 6), ZalithUiKit.dp(act, 10), ZalithUiKit.dp(act, 6), ZalithUiKit.dp(act, 10));
            tab.setOnClickListener(v -> {
                titleView.setText("Download - " + c);
                renderCategory(act, c, accent, listCol);
            });
            side.addView(tab);
        }

        ScrollView mainSc = new ScrollView(act);
        mainSc.addView(listCol);
        body.addView(mainSc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        renderCategory(act, "Game", accent, listCol);
        return body;
    }

    private static void renderCategory(LauncherActivity act, String cat, int accent, LinearLayout col) {
        col.removeAllViews();
        if ("Game".equals(cat)) {
            addDlRow(act, col, accent, "26.3", "Horizon Rust Native (Installed)");
            addDlRow(act, col, accent, "26.2", "Vanilla Assets Compatible");
            addDlRow(act, col, accent, "26.1.2", "Legacy Rust Build");
        } else {
            addDlRow(act, col, accent, "Starling Native " + cat, "Compatible with Rust 26.3 (.hmod / .zip)");
            addDlRow(act, col, accent, "Performance Pack", "Low-end mobile optimization");
        }
    }

    private static void addDlRow(final LauncherActivity act, LinearLayout col, int accent, final String title, String sub) {
        LinearLayout card = new LinearLayout(act);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
        card.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = ZalithUiKit.dp(act, 10);

        ImageView ic = new ImageView(act);
        ic.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 34)));
        card.addView(ic, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 34), ZalithUiKit.dp(act, 34)));

        LinearLayout meta = new LinearLayout(act);
        meta.setOrientation(LinearLayout.VERTICAL);
        meta.setPadding(ZalithUiKit.dp(act, 12), 0, 0, 0);
        TextView t1 = new TextView(act);
        t1.setText(title);
        t1.setTextColor(0xFFFFFFFF);
        t1.setTypeface(Typeface.DEFAULT_BOLD);
        meta.addView(t1);
        TextView t2 = new TextView(act);
        t2.setText(sub);
        t2.setTextColor(0xFF9CA3AF);
        t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        meta.addView(t2);
        card.addView(meta, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView dl = new TextView(act);
        dl.setText("⬇");
        dl.setTextColor(accent);
        dl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        card.addView(dl);
        card.setOnClickListener(v -> Toast.makeText(act, "Ready: " + title, Toast.LENGTH_SHORT).show());
        col.addView(card, lp);
    }

    public static View buildMultiplayer(final LauncherActivity act, final SharedPreferences p) {
        LinearLayout body = new LinearLayout(act);
        body.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout left = new LinearLayout(act);
        left.setOrientation(LinearLayout.VERTICAL);
        left.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 16)));
        left.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 16));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        llp.rightMargin = ZalithUiKit.dp(act, 12);

        TextView head = new TextView(act);
        head.setText("User Notice");
        head.setTextColor(0xFFFFFFFF);
        head.setTypeface(Typeface.DEFAULT_BOLD);
        head.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        left.addView(head);

        TextView info = new TextView(act);
        info.setText("\nTerracotta P2P Multiplayer allows direct peer-to-peer LAN & online rooms without a central relay server. Works with offline accounts on Rust 26.3.");
        info.setTextColor(0xFFD1D5DB);
        info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        left.addView(info);
        body.addView(left, llp);

        LinearLayout right = new LinearLayout(act);
        right.setOrientation(LinearLayout.VERTICAL);
        body.addView(right, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        right.addView(toggleRow(act, p, "Enable Multiplayer", "mp_enabled", false));
        right.addView(toggleRow(act, p, "Custom EasyTier Server Nodes", "mp_custom_nodes", false));
        return body;
    }

    private static View toggleRow(LauncherActivity act, SharedPreferences p, String label, String key, boolean def) {
        LinearLayout card = new LinearLayout(act);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
        card.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = ZalithUiKit.dp(act, 10);
        card.setLayoutParams(lp);

        TextView t = new TextView(act);
        t.setText(label);
        t.setTextColor(0xFFFFFFFF);
        card.addView(t, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Switch sw = new Switch(act);
        sw.setChecked(p.getBoolean(key, def));
        sw.setOnCheckedChangeListener((b, c) -> p.edit().putBoolean(key, c).apply());
        card.addView(sw);
        return card;
    }
}
