package io.github.juanluci.starling;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ZalithAccountsView {
    public static View build(final LauncherActivity act, final SharedPreferences p) {
        int accent = ZalithUiKit.accent(p);
        LinearLayout body = new LinearLayout(act);
        body.setOrientation(LinearLayout.HORIZONTAL);

        // Left Skin Preview + Add Account Button
        LinearLayout left = new LinearLayout(act);
        left.setOrientation(LinearLayout.VERTICAL);
        left.setGravity(Gravity.CENTER);
        body.addView(left, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.7f));

        ImageView avatar = new ImageView(act);
        avatar.setImageBitmap(ZalithUiKit.skinHead(ZalithUiKit.dp(act, 96)));
        left.addView(avatar, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 96), ZalithUiKit.dp(act, 96)));

        final LinearLayout listCol = new LinearLayout(act);
        listCol.setOrientation(LinearLayout.VERTICAL);

        Button addBtn = ZalithUiKit.pillBtn(act, "Add Account", accent, 14);
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 170), ZalithUiKit.dp(act, 42));
        blp.topMargin = ZalithUiKit.dp(act, 24);
        addBtn.setOnClickListener(v -> {
            EditText in = new EditText(act);
            in.setHint("Offline Username");
            in.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            new AlertDialog.Builder(act, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                    .setTitle("Add Offline Account")
                    .setView(in)
                    .setPositiveButton("Add", (d, w) -> {
                        String n = in.getText().toString().trim().replaceAll("[^a-zA-Z0-9_]", "");
                        if (n.isEmpty()) return;
                        List<String> list = getAccounts(p);
                        if (!list.contains(n)) list.add(n);
                        saveAccounts(p, list, n);
                        renderList(act, p, listCol);
                    })
                    .setNegativeButton("Cancel", null).show();
        });
        left.addView(addBtn, blp);

        ScrollView sc = new ScrollView(act);
        sc.addView(listCol);
        body.addView(sc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.3f));

        renderList(act, p, listCol);
        return body;
    }

    private static void renderList(LauncherActivity act, SharedPreferences p, LinearLayout col) {
        col.removeAllViews();
        String active = p.getString(LauncherActivity.KEY_USER, "qSHANKS");
        int accent = ZalithUiKit.accent(p);
        final List<String> accs = getAccounts(p);

        for (final String name : accs) {
            boolean sel = name.equals(active);
            LinearLayout card = new LinearLayout(act);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
            card.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = ZalithUiKit.dp(act, 10);

            TextView radio = new TextView(act);
            radio.setText(sel ? "◉  " : "○  ");
            radio.setTextColor(sel ? accent : 0xFF9CA3AF);
            radio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            card.addView(radio);

            ImageView head = new ImageView(act);
            head.setImageBitmap(ZalithUiKit.skinHead(ZalithUiKit.dp(act, 36)));
            card.addView(head, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 36), ZalithUiKit.dp(act, 36)));

            LinearLayout meta = new LinearLayout(act);
            meta.setOrientation(LinearLayout.VERTICAL);
            meta.setPadding(ZalithUiKit.dp(act, 12), 0, 0, 0);
            TextView t1 = new TextView(act);
            t1.setText(name);
            t1.setTextColor(0xFFFFFFFF);
            t1.setTypeface(Typeface.DEFAULT_BOLD);
            meta.addView(t1);
            TextView t2 = new TextView(act);
            t2.setText("Offline");
            t2.setTextColor(0xFF9CA3AF);
            t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            meta.addView(t2);
            card.addView(meta, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView del = new TextView(act);
            del.setText("🗑");
            del.setTextColor(0xFF9CA3AF);
            del.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            del.setOnClickListener(v -> {
                if (accs.size() > 1) {
                    accs.remove(name);
                    saveAccounts(p, accs, accs.get(0));
                    renderList(act, p, col);
                }
            });
            card.addView(del);

            card.setOnClickListener(v -> {
                p.edit().putString(LauncherActivity.KEY_USER, name).apply();
                renderList(act, p, col);
            });
            col.addView(card, lp);
        }
    }

    private static List<String> getAccounts(SharedPreferences p) {
        String raw = p.getString("accounts_list", "Shankstabz,qSHANKS");
        return new ArrayList<>(Arrays.asList(raw.split(",")));
    }

    private static void saveAccounts(SharedPreferences p, List<String> list, String active) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i));
        }
        p.edit().putString("accounts_list", sb.toString()).putString(LauncherActivity.KEY_USER, active).apply();
    }
}
