package io.github.juanluci.starling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class LauncherActivity extends Activity {
    static final String AFTER = "io.github.juanluci.starling.AFTER";
    static final String PREFS = "zalith_rust_prefs";
    static final String KEY_USER = "offline_username";
    static final String KEY_DPI = "mouse_dpi_scale";
    static final String KEY_LOCK = "lock_keyboard_focus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final int after = intent == null ? 0 : intent.getIntExtra(AFTER, 0);
        Uri file = handedFile(intent);

        if (after != 0) {
            showRestarting();
            new Thread(() -> {
                for (int i = 0; i < 100 && new File("/proc/" + after).exists(); i++) {
                    try { Thread.sleep(50); } catch (InterruptedException e) { break; }
                }
                runOnUiThread(this::play);
            }, "starling-restart").start();
        } else if (file != null) {
            final AlertDialog copying = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                    .setMessage("Importing file...").setCancelable(false).show();
            receive(this, file, () -> { copying.dismiss(); play(); });
        } else if (StartupRecovery.interrupted(this)) {
            startActivity(new Intent(this, DiagnosticsActivity.class).putExtra(StartupRecovery.RECOVERY, true));
            finish();
        } else {
            ZalithDashboard.attach(this);
        }
    }

    void play() {
        SharedPreferences p = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Intent game = new Intent(this, GameActivity.class);
        game.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        game.putExtra(KEY_USER, p.getString(KEY_USER, "Player"));
        game.putExtra(KEY_DPI, p.getInt(KEY_DPI, 100));
        game.putExtra(KEY_LOCK, p.getBoolean(KEY_LOCK, true));
        startActivity(game);
        finish();
        overridePendingTransition(0, 0);
    }

    @SuppressWarnings("deprecation")
    private void showRestarting() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            try { setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); } catch (Exception ignored) {}
        }
        TextView t = new TextView(this);
        t.setText("Opening world...");
        t.setTextColor(0xFFFFFFFF);
        t.setGravity(Gravity.CENTER);
        t.setBackgroundColor(0xFF000000);
        setContentView(t);
    }

    @SuppressWarnings("deprecation")
    private static Uri handedFile(Intent i) {
        if (i == null) return null;
        if (Intent.ACTION_VIEW.equals(i.getAction())) return i.getData();
        if (Intent.ACTION_SEND.equals(i.getAction())) return i.getParcelableExtra(Intent.EXTRA_STREAM);
        return null;
    }

    static void receive(final Activity a, final Uri f, final Runnable done) {
        final File inbox = new File(a.getFilesDir(), "import-inbox");
        final String name = displayName(a, f);
        new Thread(() -> {
            inbox.mkdirs();
            File part = new File(inbox, name + ".part");
            try (InputStream in = a.getContentResolver().openInputStream(f); OutputStream out = new FileOutputStream(part)) {
                byte[] buf = new byte[65536];
                for (int n; (n = in.read(buf)) > 0; ) out.write(buf, 0, n);
            } catch (Exception e) { part.delete(); }
            if (part.exists() && !part.renameTo(new File(inbox, name))) part.delete();
            if (done != null) a.runOnUiThread(done);
        }).start();
    }

    private static String displayName(Context c, Uri f) {
        String n = null;
        try (Cursor cur = c.getContentResolver().query(f, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cur != null && cur.moveToFirst()) n = cur.getString(0);
        } catch (Exception ignored) {}
        if (n == null) n = f.getLastPathSegment();
        if (n == null || n.isEmpty()) n = "imported";
        n = n.replaceAll("[/\\\\:*?\"<>|]", "_");
        return n.endsWith(".part") ? n + "_" : n;
    }
}
