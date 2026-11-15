package io.github.juanluci.starling;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ZalithFilesView {
    public static View build(final LauncherActivity act, final TextView titleView) {
        final File rootDir = act.getExternalFilesDir(null) != null ? act.getExternalFilesDir(null) : act.getFilesDir();
        final File[] current = { rootDir };

        LinearLayout body = new LinearLayout(act);
        body.setOrientation(LinearLayout.HORIZONTAL);

        // Left Sidebar (Back, Forward, + Create New, Parent)
        LinearLayout side = new LinearLayout(act);
        side.setOrientation(LinearLayout.VERTICAL);
        side.setGravity(Gravity.CENTER_HORIZONTAL);
        side.setPadding(0, ZalithUiKit.dp(act, 8), ZalithUiKit.dp(act, 14), 0);
        body.addView(side, new LinearLayout.LayoutParams(ZalithUiKit.dp(act, 110), ViewGroup.LayoutParams.MATCH_PARENT));

        final LinearLayout grid = new LinearLayout(act);
        grid.setOrientation(LinearLayout.VERTICAL);

        side.addView(sideBtn(act, "▲\nBack", v -> {
            if (current[0].getParentFile() != null && !current[0].equals(rootDir)) {
                current[0] = current[0].getParentFile();
                renderDir(act, current[0], titleView, grid);
            }
        }));
        side.addView(sideBtn(act, "+\nCreate New", v -> {
            EditText in = new EditText(act);
            in.setHint("New folder name");
            new AlertDialog.Builder(act, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                    .setTitle("Create Folder")
                    .setView(in)
                    .setPositiveButton("Create", (d, w) -> {
                        String n = in.getText().toString().trim();
                        if (!n.isEmpty()) {
                            new File(current[0], n).mkdirs();
                            renderDir(act, current[0], titleView, grid);
                        }
                    })
                    .setNegativeButton("Cancel", null).show();
        }));
        side.addView(sideBtn(act, "←\nParent", v -> {
            if (current[0].getParentFile() != null) {
                current[0] = current[0].getParentFile();
                renderDir(act, current[0], titleView, grid);
            }
        }));

        ScrollView sc = new ScrollView(act);
        sc.addView(grid);
        body.addView(sc, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        renderDir(act, current[0], titleView, grid);
        return body;
    }

    private static void renderDir(LauncherActivity act, File dir, TextView titleView, LinearLayout grid) {
        grid.removeAllViews();
        File[] files = dir.listFiles();
        int fCount = 0, dCount = 0;
        if (files != null) {
            for (File f : files) { if (f.isDirectory()) dCount++; else fCount++; }
        }
        titleView.setText(dir.getAbsolutePath() + "\nFolders: " + dCount + " Files: " + fCount);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

        if (files == null || files.length == 0) {
            TextView empty = new TextView(act);
            empty.setText("Empty directory (tap + Create New)");
            empty.setTextColor(0xFF9CA3AF);
            grid.addView(empty);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        LinearLayout row = null;
        for (int i = 0; i < files.length; i++) {
            if (i % 2 == 0) {
                row = new LinearLayout(act);
                row.setOrientation(LinearLayout.HORIZONTAL);
                grid.addView(row);
            }
            final File f = files[i];
            LinearLayout card = new LinearLayout(act);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setBackground(ZalithUiKit.roundRect(ZalithUiKit.CARD_GRAY, ZalithUiKit.dp(act, 14)));
            card.setPadding(ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12), ZalithUiKit.dp(act, 14), ZalithUiKit.dp(act, 12));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            lp.setMargins(ZalithUiKit.dp(act, 4), ZalithUiKit.dp(act, 4), ZalithUiKit.dp(act, 4), ZalithUiKit.dp(act, 4));

            TextView ic = new TextView(act);
            ic.setText(f.isDirectory() ? "📁" : "📄");
            ic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            card.addView(ic);

            LinearLayout meta = new LinearLayout(act);
            meta.setOrientation(LinearLayout.VERTICAL);
            meta.setPadding(ZalithUiKit.dp(act, 10), 0, 0, 0);
            TextView nm = new TextView(act);
            nm.setText(f.getName());
            nm.setTextColor(0xFFFFFFFF);
            nm.setTypeface(Typeface.DEFAULT_BOLD);
            meta.addView(nm);
            TextView dt = new TextView(act);
            dt.setText((f.isDirectory() ? "" : f.length() + " B ") + sdf.format(new Date(f.lastModified())));
            dt.setTextColor(0xFF9CA3AF);
            dt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            meta.addView(dt);
            card.addView(meta);

            if (f.isDirectory()) {
                card.setOnClickListener(v -> renderDir(act, f, titleView, grid));
            }
            row.addView(card, lp);
        }
    }

    private static TextView sideBtn(LauncherActivity act, String txt, View.OnClickListener l) {
        TextView t = new TextView(act);
        t.setText(txt);
        t.setTextColor(0xFFD1D5DB);
        t.setGravity(Gravity.CENTER);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        t.setPadding(0, ZalithUiKit.dp(act, 10), 0, ZalithUiKit.dp(act, 10));
        t.setOnClickListener(l);
        return t;
    }
}
