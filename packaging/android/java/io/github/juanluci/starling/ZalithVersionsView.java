package io.github.juanluci.starling;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public final class ZalithVersionsView {
    public static View buildList(final LauncherActivity act, final SharedPreferences p, final TextView titleView) {
        int accent = ZalithUiKit.accent(p);
        final LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.HORIZONTAL);

        // Left Panel (Default Game Folder + Cyan Buttons)
        LinearLayout left = new LinearLayout(act);
        left.setOrientation(LinearLayout.VERTICAL);
        left.setPadding(0, 0, ZalithUiKit.dp(act, 14), 0);
        root.addView(left, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 195), ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout folderCard = new LinearLayout(act);
        folderCard.setOrientation(LinearLayout.VERTICAL);
        folderCard.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
        folderCard.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
        TextView f1 = new TextView(act);
        f1.setText("Default Game Folder");
        f1.setTextColor(0xFFFFFFFF);
        f1.setTypeface(Typeface.DEFAULT_BOLD);
        folderCard.addView(f1);
        TextView f2 = new TextView(act);
        f2.setText("/storage/emulated/0/Starling");
        f2.setTextColor(0xFF9CA3AF);
        f2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        folderCard.addView(f2);
        left.addView(folderCard);

        Button addF = ZalithUiKit.pillBtn(act, "Add Game Folder", accent, 13);
        LinearLayout.LayoutParams b1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 40));
        b1.topMargin = ZalithUiKit.dp(act, 14);
        addF.setOnClickListener(v -> Toast.makeText(act, "Using /Starling shared folder", Toast.LENGTH_SHORT).show());
        left.addView(addF, b1);

        Button cleanF = ZalithUiKit.pillBtn(act, "Clean up game files", accent, 13);
        LinearLayout.LayoutParams b2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZalithUiKit.dp(act, 40));
        b2.topMargin = ZalithUiKit.dp(act, 10);
        cleanF.setOnClickListener(v -> Toast.makeText(act, "Cache cleaned", Toast.LENGTH_SHORT).show());
        left.addView(cleanF, b2);

        // Right Version Cards
        ScrollView sc = new ScrollView(act);
        LinearLayout list = new LinearLayout(act);
        list.setOrientation(LinearLayout.VERTICAL);
        sc.addView(list);
        root.addView(sc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        String[][] versions = {
                {"Rust Optimized 26.3", "26.3   Horizon Rust - 0.8.0"},
                {"Vanilla Rust 26.3", "26.3   Clean Native Engine"}
        };
        for (final String[] ver : versions) {
            LinearLayout card = new LinearLayout(act);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
            card.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = ZalithUiKit.dp(act, 10);

            TextView rad = new TextView(act);
            rad.setText("◉  ");
            rad.setTextColor(accent);
            rad.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            card.addView(rad);

            ImageView ic = new ImageView(act);
            ic.setImageBitmap(ZalithUiKit.rustIcon(ZalithUiKit.dp(act, 36)));
            card.addView(ic, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 36), ZalithUiKit.dp(act, 36)));

            LinearLayout meta = new LinearLayout(act);
            meta.setOrientation(LinearLayout.VERTICAL);
            meta.setPadding(ZalithUiKit.dp(act, 12), 0, 0, 0);
            TextView t1 = new TextView(act);
            t1.setText(ver);
            t1.setTextColor(0xFFFFFFFF);
            t1.setTypeface(Typeface.DEFAULT_BOLD);
            meta.addView(t1);
            TextView t2 = new TextView(act);
            t2.setText(ver);
            t2.setTextColor(0xFF9CA3AF);
            t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            meta.addView(t2);
            card.addView(meta, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView gear = new TextView(act);
            gear.setText("⚙");
            gear.setTextColor(0xFFE5E7EB);
            gear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            gear.setOnClickListener(v -> {
                root.removeAllViews();
                titleView.setText("Manage Versions - Overview");
                root.addView(buildManage(act, ver, titleView), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            });
            card.addView(gear);
            list.addView(card, lp);
        }
        return root;
    }

    public static View buildManage(final LauncherActivity act, final String verName, final TextView titleView) {
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

        String[] tabs = {"Overview", "Configuration", "Update Loader", "Mods", "Saves", "Resource Packs", "Shader Packs", "Screenshots", "Servers"};
        for (final String t : tabs) {
            TextView b = new TextView(act);
            b.setText(t);
            b.setTextColor(0xFFD1D5DB);
            b.setGravity(Gravity.CENTER);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            b.setPadding(ZalithUiKit.dp(act, 6), ZalithUiKit.dp(act, 10), ZalithUiKit.dp(act, 6), ZalithUiKit.dp(act, 10));
            b.setOnClickListener(v -> {
                titleView.setText("Manage Versions - " + t);
                renderManageTab(act, verName, t, content);
            });
            side.addView(b);
        }

        ScrollView sc = new ScrollView(act);
        sc.addView(content);
        body.addView(sc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        renderManageTab(act, verName, "Overview", content);
        return body;
    }

    private static void renderManageTab(LauncherActivity act, String ver, String tab, LinearLayout box) {
        box.removeAllViews();
        LinearLayout card = new LinearLayout(act);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
        card.setPadding(ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 16), ZalithUiKit.dp(act, 14));

        TextView h = new TextView(act);
        h.setText(ver + " • " + tab);
        h.setTextColor(0xFFFFFFFF);
        h.setTypeface(Typeface.DEFAULT_BOLD);
        card.addView(h);

        TextView s = new TextView(act);
        s.setText("Mods".equals(tab) ? "Native Rust (.hmod) mods enabled for 26.3" : "Quick Actions: Open " + tab + " Folder");
        s.setTextColor(0xFF9CA3AF);
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        card.addView(s);

        if ("Mods".equals(tab)) {
            CheckBox cb = new CheckBox(act);
            cb.setText("Sodium / Horizon Native Optimizer (.hmod)");
            cb.setTextColor(0xFFFFFFFF);
            cb.setChecked(true);
            card.addView(cb);
        }
        box.addView(card);
    }
                         }
