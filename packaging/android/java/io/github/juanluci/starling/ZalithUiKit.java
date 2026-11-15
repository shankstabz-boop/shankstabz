package io.github.juanluci.starling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

public final class ZalithUiKit {
    static final int BG_DARK = 0xFF141619, CARD_GRAY = 0xFF202328, CARD_LIGHT = 0xFF2A2E35;

    @SuppressWarnings("deprecation")
    static void makeFullscreen(Activity act) {
        act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController c = act.getWindow().getInsetsController();
            if (c != null) { c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars()); c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE); }
        } else act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    static GradientDrawable bgGradient() { return new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xFF1C2026, 0xFF121316}); }

    static int accent(SharedPreferences p) {
        String t = p.getString("color_theme", "Dynamic");
        if ("Embermire".equals(t)) return 0xFFF97316;
        if ("Velvet Rose".equals(t)) return 0xFFF43F5E;
        if ("Mistwave".equals(t)) return 0xFF38BDF8;
        if ("Glacier".equals(t)) return 0xFFA5F3FC;
        return 0xFF67D5E8;
    }

    static void animateIn(View v) {
        AnimationSet s = new AnimationSet(true); s.setInterpolator(new DecelerateInterpolator(1.6f)); s.setDuration(220);
        s.addAnimation(new AlphaAnimation(0f, 1f)); s.addAnimation(new TranslateAnimation(0f, 0f, 18f, 0f)); v.startAnimation(s);
    }

    static void addBounce(final View v) {
        v.setOnTouchListener((view, ev) -> {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) view.animate().scaleX(0.92f).scaleY(0.92f).setDuration(80).start();
            else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) view.animate().scaleX(1f).scaleY(1f).setDuration(110).start();
            return false;
        });
    }

    static ImageView navIcon(Context c, int type, View.OnClickListener l) {
        ImageView iv = new ImageView(c); iv.setImageBitmap(drawGlyph(dp(c, 18), type, 0xFF93C5FD));
        iv.setBackground(roundRect(CARD_GRAY, dp(c, 9))); int pad = dp(c, 7); iv.setPadding(pad, pad, pad, pad);
        addBounce(iv); iv.setOnClickListener(l); return iv;
    }

    static Bitmap drawGlyph(int sz, int type, int col) {
        Bitmap b = Bitmap.createBitmap(sz, sz, Bitmap.Config.ARGB_8888); Canvas c = new Canvas(b);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG); p.setColor(col); p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(sz * 0.11f); p.setStrokeCap(Paint.Cap.ROUND); p.setStrokeJoin(Paint.Join.ROUND);
        float m = sz * 0.18f, e = sz * 0.82f, mid = sz * 0.5f;
        if (type == 0) { Path f = new Path(); f.moveTo(m, sz * 0.28f); f.lineTo(sz * 0.44f, sz * 0.28f); f.lineTo(sz * 0.52f, sz * 0.36f); f.lineTo(e, sz * 0.36f); f.lineTo(e, sz * 0.78f); f.lineTo(m, sz * 0.78f); f.close(); c.drawPath(f, p); }
        else if (type == 1) { c.drawCircle(mid, sz * 0.34f, sz * 0.15f, p); c.drawArc(sz * 0.22f, sz * 0.54f, sz * 0.78f, sz * 0.94f, 180, 180, false, p); }
        else if (type == 2) { c.drawCircle(mid, mid, sz * 0.32f, p); c.drawLine(m, mid, e, mid, p); c.drawLine(mid, m, mid, e, p); }
        else if (type == 3) { c.drawLine(mid, m, mid, sz * 0.65f, p); c.drawLine(sz * 0.34f, sz * 0.50f, mid, sz * 0.65f, p); c.drawLine(sz * 0.66f, sz * 0.50f, mid, sz * 0.65f, p); c.drawLine(m, e, e, e, p); }
        else { c.drawCircle(mid, mid, sz * 0.16f, p); c.drawCircle(mid, mid, sz * 0.32f, p); }
        return b;
    }

    static Button pillBtn(Context c, String txt, int bg, int sp) {
        Button b = new Button(c); b.setText(txt); b.setAllCaps(false); b.setTextColor(0xFF071E24);
        b.setTypeface(Typeface.DEFAULT_BOLD); b.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        b.setBackground(roundRect(bg, dp(c, 18))); b.setMinHeight(0); b.setMinimumHeight(0); addBounce(b); return b;
    }

    static Bitmap skinHead(int sz) {
        Bitmap b = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888); Canvas c = new Canvas(b); Paint p = new Paint();
        p.setColor(0xFFE5E7EB); c.drawRect(0, 0, 8, 8, p); p.setColor(0xFF9CA3AF); c.drawRect(0, 0, 8, 2, p); c.drawRect(0, 2, 1, 6, p); c.drawRect(7, 2, 8, 6, p);
        p.setColor(0xFFFDE6D8); c.drawRect(1, 3, 7, 8, p); p.setColor(0xFF1E293B); c.drawRect(2, 4, 3, 6, p); c.drawRect(5, 4, 6, 6, p);
        return Bitmap.createScaledBitmap(b, sz, sz, false);
    }

    static Bitmap rustIcon(int sz) {
        Bitmap b = Bitmap.createBitmap(sz, sz, Bitmap.Config.ARGB_8888); Canvas c = new Canvas(b); Paint p = new Paint(Paint.ANTI_ALIAS_FLAG); float mid = sz * 0.5f;
        p.setColor(0xFF282C34); c.drawRoundRect(0, 0, sz, sz, sz * 0.24f, sz * 0.24f, p);
        Path hex = new Path(); hex.moveTo(mid, sz * 0.14f); hex.lineTo(sz * 0.82f, sz * 0.32f); hex.lineTo(sz * 0.82f, sz * 0.68f); hex.lineTo(mid, sz * 0.86f); hex.lineTo(sz * 0.18f, sz * 0.68f); hex.lineTo(sz * 0.18f, sz * 0.32f); hex.close();
        p.setColor(0xFFD97736); c.drawPath(hex, p); p.setColor(0xFF1C2026); c.drawCircle(mid, mid, sz * 0.21f, p); p.setColor(0xFF67D5E8); c.drawCircle(mid, mid, sz * 0.11f, p);
        return b;
    }

    static GradientDrawable roundRect(int col, int r) { GradientDrawable d = new GradientDrawable(); d.setColor(col); d.setCornerRadius(r); d.setStroke(1, 0x26FFFFFF); return d; }
    static LinearLayout.LayoutParams lp(int w, int h, float wt) { return new LinearLayout.LayoutParams(w, h, wt); }
    static int dp(Context c, int v) { return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics()); }
}
