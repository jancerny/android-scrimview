package eu.jancerny.widget;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class ScrimView extends View {

    private int gravity = Gravity.BOTTOM;
    private int color = 0x88000000;
    private int numSteps = 8;


    public ScrimView(Context context) {
        super(context);
    }

    public ScrimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScrimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        drawGradient();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        drawGradient();
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
        drawGradient();
    }

    private void init(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ScvScrimView);

        try {
            gravity = a.getInt(R.styleable.ScvScrimView_gravity, gravity);
            color = a.getColor(R.styleable.ScvScrimView_scrimColor, color);
            numSteps = a.getInt(R.styleable.ScvScrimView_numSteps, numSteps);
        } finally {
            a.recycle();
        }

        drawGradient();
    }

    private void drawGradient() {
        setBackground(ScrimUtils.makeCubicGradientScrimDrawable(color, numSteps, gravity));
    }
}
