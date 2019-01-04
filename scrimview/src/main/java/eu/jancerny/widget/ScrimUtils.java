package eu.jancerny.widget;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.LruCache;
import android.view.Gravity;

public class ScrimUtils {

    private static LruCache<Integer, Drawable> gradientCache = new LruCache<>(10);

    public static float constrain(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }

    public static Drawable makeCubicGradientScrimDrawable(int baseColor, int stopsCount, int gravity) {
        int cacheKeyHash = baseColor;
        cacheKeyHash = 31 * cacheKeyHash + stopsCount;
        cacheKeyHash = 31 * cacheKeyHash + gravity;

        Drawable cachedGradient = gradientCache.get(cacheKeyHash);
        if (cachedGradient != null) {
            return cachedGradient;
        }

        stopsCount = Math.max(stopsCount, 2);

        PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());

        final int[] stopColors = new int[stopsCount];

        int red = Color.red(baseColor);
        int green = Color.green(baseColor);
        int blue = Color.blue(baseColor);
        int alpha = Color.alpha(baseColor);

        for (int i = 0; i < stopsCount; i++) {
            float x = ((float) i) / (stopsCount - 1f);
            float opacity = constrain(0f, 1f, (float) Math.pow((double) x, 3.0));
            stopColors[i] = Color.argb(Math.round(alpha * opacity), red, green, blue);
        }

        final float x0;
        final float x1;
        final float y0;
        final float y1;

        int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        if (horizontalGravity == Gravity.LEFT) {
            x0 = 1f;
            x1 = 0f;
        } else if (horizontalGravity == Gravity.RIGHT) {
            x0 = 0f;
            x1 = 1f;
        } else {
            x0 = 0f;
            x1 = 0f;
        }

        if (verticalGravity == Gravity.TOP) {
            y0 = 1f;
            y1 = 0f;
        } else if (verticalGravity == Gravity.BOTTOM) {
            y0 = 0f;
            y1 = 1f;
        } else {
            y0 = 0f;
            y1 = 0f;
        }

        paintDrawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {

            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(
                        width * x0,
                        height * y0,
                        width * x1,
                        height * y1,
                        stopColors,
                        null,
                        Shader.TileMode.CLAMP
                );
            }
        });
        gradientCache.put(cacheKeyHash, paintDrawable);
        return paintDrawable;
    }
}
