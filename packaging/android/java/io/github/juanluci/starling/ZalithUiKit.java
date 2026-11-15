package io.github.juanluci.starling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public final class ZalithUiKit {
    static final int BG_DARK = 0xFF17181B;
    static final int CARD_GRAY = 0xFF232529;
    static final int CARD_LIGHT = 0xFF2E3136;

    @SuppressWarnings("deprecation")
    static void makeFullscreen(Activity act) {
        act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController c = act.getWindow().getInsetsController();
            if (c != null) {
                c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            act.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    static int accent(SharedPreferences p) {
        String t = p.getString("color_theme", "Dynamic");
        if ("Embermire".equals(t)) return 0xFFF97316;
        if ("Velvet Rose".equals(t)) return 0xFFF43F5E;
        if ("Mistwave".equals(t)) return 0xFF38BDF8;
        if ("Glacier".equals(t)) return 0xFFA5F3FC;
        return 0xFF67D5E8;
    }

    static void animateIn(View v) {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator(1.6f));
        set.setDuration(240);
        set.addAnimation(new AlphaAnimation(0f, 1f));
        set.addAnimation(new TranslateAnimation(0f, 0f, 22f, 0f));
        v.startAnimation(set);
    }

    static void addBounce(final View v) {
        v.setOnTouchListener((view, ev) -> {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(90).start();
            else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) view.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
            return false;
        });
    }

    static TextView topIcon(Context c, String g, View.OnClickListener l) {
        TextView t = new TextView(c);
        t.setText(g); t.setTextColor(0xFFE5E7EB); t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        t.setBackground(roundRect(CARD_GRAY, dp(c, 10)));
        t.setPadding(dp(c, 11), dp(c, 6), dp(c, 11), dp(c, 6));
        addBounce(t); t.setOnClickListener(l);
        return t;
    }

    static Button pillBtn(Context c, String txt, int bg, int sp) {
        Button b = new Button(c);
        b.setText(txt); b.setAllCaps(false); b.setTextColor(0xFF0A252B);
        b.setTypeface(Typeface.DEFAULT_BOLD); b.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        b.setBackground(roundRect(bg, dp(c, 22)));
        addBounce(b);
        return b;
    }

    static Bitmap skinHead(int sz) {
        Bitmap b = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b); Paint p = new Paint();
        p.setColor(0xFFE5E7EB); c.drawRect(0, 0, 8, 8, p);
        p.setColor(0xFF9CA3AF); c.drawRect(0, 0, 8, 2, p); c.drawRect(0, 2, 1, 6, p); c.drawRect(7, 2, 8, 6, p);
        p.setColor(0xFFFDE6D8); c.drawRect(1, 3, 7, 8, p);
        p.setColor(0xFF1E293B); c.drawRect(2, 4, 3, 6, p); c.drawRect(5, 4, 6, 6, p);
        return Bitmap.createScaledBitmap(b, sz, sz, false);
    }

    static Bitmap rustIcon(int sz) {
        Bitmap b = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b); Paint p = new Paint();
        p.setColor(0xFFB45309); c.drawRect(0, 0, 8, 8, p);
        p.setColor(0xFFF59E0B); c.drawRect(1, 1, 7, 7, p);
        p.setColor(0xFF78350F); c.drawRect(2, 2, 6, 6, p);
        return Bitmap.createScaledBitmap(b, sz, sz, false);
    }

    static GradientDrawable roundRect(int col, int r) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(col); d.setCornerRadius(r); d.setStroke(2, 0x22FFFFFF);
        return d;
    }

    static int dp(Context c, int v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics());
    }
}
