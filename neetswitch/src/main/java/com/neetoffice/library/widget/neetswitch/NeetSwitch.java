package com.neetoffice.library.widget.neetswitch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Deo-chainmeans on 2015/8/17.
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
    private float open = 1.0f;
    private boolean mediaDesign = false;
    private float radius;
    private int onColor;
    private int offColor;
    private int buttonColor;
    private boolean touchable = true;
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

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NeetSwitch, defStyleAttr, defStyleRes);
            onColor = a.getColor(R.styleable.NeetSwitch_neet_switch_onColor, Color.parseColor("#0288ce"));
            offColor = a.getColor(R.styleable.NeetSwitch_neet_switch_offColor, Color.parseColor("#888888"));
            buttonColor = a.getColor(R.styleable.NeetSwitch_neet_switch_buttonColor, Color.WHITE);
            shape = a.getInt(R.styleable.NeetSwitch_neet_switch_shape, CIRCLE);
            touchable = a.getBoolean(R.styleable.NeetSwitch_neet_switch_touchable, touchable);
            open = a.getBoolean(R.styleable.NeetSwitch_neet_switch_isOpen, false) ? 1.0f : 0.0f;
            mediaDesign = a.getBoolean(R.styleable.NeetSwitch_neet_switch_mediaDesign, mediaDesign);
            elevation = a.getDimensionPixelSize(R.styleable.NeetSwitch_neet_switch_elevation, 6);
        }
        paint = new Paint();
        paint.setStrokeWidth(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = getElevation() > 0 ? getElevation() : elevation;
        } else {
            elevation = 6;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);
        radius = width / 6f < height / 2f ? width / 6f : height / 2f;
        final float pt = getPaddingTop();
        final float pb = getPaddingBottom();
        final float pl = getPaddingLeft();
        final float pr = getPaddingRight();
        if (height < radius + pt + pb) {
            height = radius + pt + pb;
        }
        startX = width - pr - radius;
        setMeasuredDimension(width, (int) height);
    }

    @Override
    public void draw(Canvas canvas) {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        final float pl = getPaddingLeft();
        final float pr = getPaddingRight();
        final int alpha = (int) (Color.alpha(offColor) + open * (Color.alpha(onColor) - Color.alpha(offColor)));
        final int red = (int) (Color.red(offColor) + open * (Color.red(onColor) - Color.red(offColor)));
        final int green = (int) (Color.green(offColor) + open * (Color.green(onColor) - Color.green(offColor)));
        final int blue = (int) (Color.blue(offColor) + open * (Color.blue(onColor) - Color.blue(offColor)));
        final int barColor = Color.argb(alpha, red, green, blue);
        paint.setColor(barColor);
        final float axis = getAxis();
        if (shape == RECT) {
            if (mediaDesign) {
                RectF rectF = new RectF(pl + radius, height / 2f - radius / 2f, pl + radius + axis, height / 2f + radius / 2f);
                canvas.drawRect(rectF, paint);
            } else {
                RectF rectF = new RectF(pl, height / 2f - radius, width - pr, height / 2f + radius);
                canvas.drawRect(rectF, paint);
            }
            final float x = pl + radius + axis * open;
            final float y = height / 2f;
            if (mediaDesign) {
                RectF rectF = new RectF(x - radius + 3*elevation, y - radius + 3*elevation, x + radius - elevation, y + radius - elevation);
                paint.setColor(Color.GRAY);
                paint.setAlpha(100);
                canvas.drawRect(rectF, paint);
                paint.setAlpha(255);
            }
            paint.setShader(null);
            paint.setColor(buttonColor);
            RectF rectF = new RectF(x - radius + elevation * (mediaDesign ? 2 : 1), y - radius + elevation * (mediaDesign ? 2 : 1), x + radius - elevation * (mediaDesign ? 2 : 1), y + radius - elevation * (mediaDesign ? 2 : 1));
            canvas.drawRect(rectF, paint);
        } else {
            if (mediaDesign) {
                RectF rectF = new RectF(pl + radius, height / 2f - radius / 2f, pl + radius + axis, height / 2f + radius / 2f);
                canvas.drawRoundRect(rectF, radius / 2f, radius / 2f, paint);
            } else {
                RectF rectF = new RectF(pl, height / 2f - radius, width - pr, height / 2f + radius);
                canvas.drawRoundRect(rectF, radius, radius, paint);
            }
            if (mediaDesign) {
                final RadialGradient radialGradient = new RadialGradient(pl + radius + axis * open, height / 2f + elevation, radius, new int[]{Color.GRAY, Color.TRANSPARENT}, new float[]{0, 1}, Shader.TileMode.REPEAT);
                paint.setShader(radialGradient);
                canvas.drawCircle(pl + radius + axis * open + elevation, height / 2f + elevation, radius - elevation, paint);
            }
            paint.setShader(null);
            paint.setColor(buttonColor);
            canvas.drawCircle(pl + radius + axis * open, height / 2f, radius - elevation * (mediaDesign ? 2 : 1), paint);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!touchable&&l != null) {
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
            runAnimator(open, open > 0.5f ? 1f : 0f);
        }
        return super.onTouchEvent(event);
    }

    private void setOpen(float open) {
        this.open = open;
        invalidate();
    }

    public void toggle() {
        runAnimator(open, open > 0 ? 0f : 1f);
    }

    private void runAnimator(float from, float to) {
        ObjectAnimator a = ObjectAnimator.ofFloat(this, "open", from, to).setDuration((long) (DURATION * Math.abs(to - from)));
        AnimatorSet set = new AnimatorSet();
        set.addListener(listener);
        set.play(a);
        set.start();
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(NeetSwitch neetSwitch, boolean isChecked);
    }
}
