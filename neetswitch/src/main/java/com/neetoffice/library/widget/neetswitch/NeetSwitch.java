package com.neetoffice.library.widget.neetswitch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Deo on 2015/8/17.
 */
public class NeetSwitch extends View {
    private static final long DURATION = 500;
    private static final int RECT = 1;
    private static final int CIRCLE = 2;
    private float elevation = 6;
    private OnClickListener l;
    private boolean canmove;
    private float startX;
    private int shape = CIRCLE;
    private Paint paint;
    private Paint textPaint;
    private float open = 1.0f;
    private boolean mediaDesign = false;
    private float radius;
    private int onColor = Color.parseColor("#0288ce");
    private int offColor = Color.parseColor("#888888");
    private int buttonColor = Color.WHITE;
    private int lineColor = Color.TRANSPARENT;
    private float lineWidth = 0;
    private boolean touchable = true;
    private CharSequence offtext = "";
    private CharSequence ontext = "";
    private int textSize = 15;
    private int textColor = Color.BLACK;
    private OnCheckedChangeListener onCheckedChangeListener;
    private final Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(NeetSwitch.this, open > 0);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public NeetSwitch(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public NeetSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public NeetSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NeetSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("NewApi")
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NeetSwitch, defStyleAttr, defStyleRes);
            onColor = a.getColor(R.styleable.NeetSwitch_neet_switch_onColor, onColor);
            offColor = a.getColor(R.styleable.NeetSwitch_neet_switch_offColor, offColor);
            offtext = a.getText(R.styleable.NeetSwitch_neet_switch_offtext);
            ontext = a.getText(R.styleable.NeetSwitch_neet_switch_ontext);
            textSize = a.getDimensionPixelSize(R.styleable.NeetSwitch_neet_switch_textsize, textSize);
            textColor = a.getColor(R.styleable.NeetSwitch_neet_switch_textcolor, textColor);
            buttonColor = a.getColor(R.styleable.NeetSwitch_neet_switch_buttonColor, buttonColor);
            lineColor = a.getColor(R.styleable.NeetSwitch_neet_switch_lineColor, lineColor);
            lineWidth = a.getDimension(R.styleable.NeetSwitch_neet_switch_lineWidth, lineWidth);
            shape = a.getInt(R.styleable.NeetSwitch_neet_switch_shape, shape);
            touchable = a.getBoolean(R.styleable.NeetSwitch_neet_switch_touchable, touchable);
            open = a.getBoolean(R.styleable.NeetSwitch_neet_switch_isOpen, open != 1.0f) ? 1.0f : 0.0f;
            mediaDesign = a.getBoolean(R.styleable.NeetSwitch_neet_switch_mediaDesign, mediaDesign);
            elevation = a.getDimensionPixelSize(R.styleable.NeetSwitch_neet_switch_elevation, (int) elevation);
        }
        paint = new Paint();
        paint.setStrokeWidth(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = getElevation() > 0 ? getElevation() : elevation;
        }
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    protected int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = measureDimension(120, widthMeasureSpec);
        final int height = measureDimension(60, heightMeasureSpec);
        radius = width / 4f < height / 2f ? width / 4f : height / 2f;
        startX = width - radius;
        setMeasuredDimension(width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        final int alpha = (int) (Color.alpha(offColor) + open * (Color.alpha(onColor) - Color.alpha(offColor)));
        final int red = (int) (Color.red(offColor) + open * (Color.red(onColor) - Color.red(offColor)));
        final int green = (int) (Color.green(offColor) + open * (Color.green(onColor) - Color.green(offColor)));
        final int blue = (int) (Color.blue(offColor) + open * (Color.blue(onColor) - Color.blue(offColor)));
        final int barColor = Color.argb(alpha, red, green, blue);
        paint.setColor(barColor);
        final float axis = getAxis();
        Rect tr = new Rect();
        if (offtext != null && open > 0.5) {
            textPaint.getTextBounds(offtext.toString(), 0, offtext.length(), tr);
        } else if (ontext != null) {
            textPaint.getTextBounds(ontext.toString(), 0, ontext.length(), tr);
        }
        if (mediaDesign) {
            final float left = radius;
            final float top = height / 2f - radius / 2f;
            final float right = radius + axis;
            final float bottom = height / 2f + radius / 2f;
            RectF rectF = new RectF(left, top, right, bottom);
            drewbar(canvas, rectF, shape == RECT ? 0 : radius / 2f, barColor);
        } else {
            final float left = 0;
            final float top = height / 2f - radius;
            final float right = width;
            final float bottom = height / 2f + radius;
            RectF rectF = new RectF(left, top, right, bottom);
            drewbar(canvas, rectF, shape == RECT ? 0 : radius, barColor);
        }

        final float r = radius - lineWidth - elevation;
        float w;
        float h = r;
        if (mediaDesign || (tr.width() - r) < r) {
            w = r;
        } else {
            w = tr.width();
        }
        final float x = lineWidth + elevation + w + (axis - (w - r) * 2) * open;
        final float y = height / 2f;
        if (mediaDesign) {
            if (shape == CIRCLE) {
                final RadialGradient radialGradient = new RadialGradient(x + elevation, y + elevation,
                        r + elevation * 2, new int[] { Color.GRAY, Color.TRANSPARENT }, new float[] { 0, 1 },
                        Shader.TileMode.REPEAT);
                paint.setShader(radialGradient);
                canvas.drawCircle(x + elevation, y + elevation, r, paint);
            } else {
                RectF rectF = new RectF(x - r + elevation, y - r + elevation, x + r + elevation, y + r + elevation);
                paint.setColor(Color.GRAY);
                paint.setAlpha(100);
                canvas.drawRect(rectF, paint);
                paint.setAlpha(255);
            }
        }
        paint.setShader(null);
        paint.setColor(buttonColor);
        RectF rectF = new RectF(x - w, y - h, x + w, y + h);
        canvas.drawRoundRect(rectF, shape == RECT ? 0 : radius, shape == RECT ? 0 : radius, paint);

        if (!mediaDesign && offtext != null && open > 0.5) {
            canvas.drawText(offtext.toString(), x - tr.width() / 2, y + textSize / 4, textPaint);
        } else if (!mediaDesign && ontext != null) {
            canvas.drawText(ontext.toString(), x - tr.width() / 2, y + textSize / 4, textPaint);
        }
    }

    private void drewbar(Canvas canvas, RectF bar, float radius, int barColor) {
        bar = new RectF(bar.left + lineWidth, bar.top + lineWidth, bar.right - lineWidth, bar.bottom - lineWidth);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Paint.Style.STROKE);
        if (radius > 0) {
            canvas.drawRoundRect(bar, radius, radius, paint);
        } else {
            canvas.drawRect(bar, paint);
        }
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(barColor);
        if (radius > 0) {
            canvas.drawRoundRect(bar, radius, radius, paint);
        } else {
            canvas.drawRect(bar, paint);
        }
    }

    private float getAxis() {
        final float width = getMeasuredWidth();
        final float pl = getPaddingLeft();
        final float pr = getPaddingRight();
        return width - pl - pr - radius * 2;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.l = l;
        super.setOnClickListener(l);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setChecked(boolean checked) {
        runAnimator(open, checked ? 1.0f : 0.0f, DURATION);
    }

    public void setChecked(boolean checked, long duration) {
        runAnimator(open, checked ? 1.0f : 0.0f, duration);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!touchable || l != null) {
            return super.onTouchEvent(event);
        }
        final float height = getMeasuredHeight();
        final float axis = getAxis();
        final float pl = getPaddingLeft();
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            final float l = (pl + radius + axis * open) - (radius - elevation * (mediaDesign ? 2 : 1)) / 2f;
            final float t = (height / 2f) - (radius - elevation * (mediaDesign ? 2 : 1)) / 2f;
            final float r = (pl + radius + axis * open) + (radius - elevation * (mediaDesign ? 2 : 1)) / 2f;
            final float b = (height / 2f) + (radius - elevation * (mediaDesign ? 2 : 1)) / 2f;
            final RectF rectF = new RectF(l, t, r, b);
            canmove = rectF.contains(event.getX(), event.getY());
            return canmove;
        } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
            if (!canmove) {
                return false;
            }
            float w = event.getX() - startX;
            if (w > 0) {
                open = 1f;
            } else {
                open = 1f - Math.abs(w) / getAxis();
                if (open < 0) {
                    open = 0;
                }
            }
            invalidate();
            return true;
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            runAnimator(open, open > 0.5f ? 1f : 0f, DURATION);
        }
        return super.onTouchEvent(event);
    }

    private void setOpen(float open) {
        this.open = open;
        invalidate();
    }

    public void toggle() {
        runAnimator(open, open > 0 ? 0f : 1f, DURATION);
    }

    public void toggle(long duration) {
        runAnimator(open, open > 0 ? 0f : 1f, duration);
    }

    private void runAnimator(float from, float to, long duration) {
        if (duration > 0) {
            ObjectAnimator a = ObjectAnimator.ofFloat(this, "open", from, to)
                    .setDuration((long) (duration * Math.abs(to - from)));
            AnimatorSet set = new AnimatorSet();
            set.addListener(listener);
            set.play(a);
            set.start();
        } else {
            setOpen(to);
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(NeetSwitch.this, open > 0);
            }
        }
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(NeetSwitch neetSwitch, boolean isChecked);
    }
}
