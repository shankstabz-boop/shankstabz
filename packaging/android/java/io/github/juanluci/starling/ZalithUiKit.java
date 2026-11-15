package io.github.juanluci.starling;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class ZalithUiKit {
    static final int BG_DARK = 0xFF1E1F22;
    static final int CARD_GRAY = 0xFF2B2D31;
    static final int CARD_LIGHT = 0xFF35373C;

    static int accent(SharedPreferences p) {
        String t = p.getString("color_theme", "Dynamic");
        if ("Embermire".equals(t)) return 0xFFF97316;
        if ("Velvet Rose".equals(t)) return 0xFFF43F5E;
        if ("Mistwave".equals(t)) return 0xFF38BDF8;
        if ("Glacier".equals(t)) return 0xFFA5F3FC;
        return 0xFF67D5E8;
    }

    static TextView topIcon(Context c, String g, View.OnClickListener l) {
        TextView t = new TextView(c);
        t.setText(g);
        t.setTextColor(0xFFE5E7EB);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        t.setPadding(dp(c, 10), dp(c, 4), dp(c, 10), dp(c, 4));
        t.setOnClickListener(l);
        return t;
    }

    static Button pillBtn(Context c, String txt, int bg, int sp) {
        Button b = new Button(c);
        b.setText(txt);
        b.setAllCaps(false);
        b.setTextColor(0xFF0A252B);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        b.setBackground(roundRect(bg, dp(c, 22)));
        return b;
    }

    static Bitmap skinHead(int sz) {
        Bitmap b = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        p.setColor(0xFFE5E7EB); c.drawRect(0, 0, 8, 8, p);
        p.setColor(0xFF9CA3AF); c.drawRect(0, 0, 8, 2, p); c.drawRect(0, 2, 1, 6, p); c.drawRect(7, 2, 8, 6, p);
        p.setColor(0xFFFDE6D8); c.drawRect(1, 3, 7, 8, p);
        p.setColor(0xFF1E293B); c.drawRect(2, 4, 3, 6, p); c.drawRect(5, 4, 6, 6, p);
        return Bitmap.createScaledBitmap(b, sz, sz, false);
    }

    static Bitmap rustIcon(int sz) {
        Bitmap b = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        p.setColor(0xFFB45309); c.drawRect(0, 0, 8, 8, p);
        p.setColor(0xFFF59E0B); c.drawRect(1, 1, 7, 7, p);
        p.setColor(0xFF78350F); c.drawRect(2, 2, 6, 6, p);
        return Bitmap.createScaledBitmap(b, sz, sz, false);
    }

    static GradientDrawable roundRect(int col, int r) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(col);
        d.setCornerRadius(r);
        return d;
    }

    static int dp(Context c, int v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics());
    }
                                       }
