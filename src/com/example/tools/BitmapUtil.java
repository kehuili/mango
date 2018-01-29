package com.example.tools;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;

public class BitmapUtil  
{  
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap,String s)  
    {  
        if (null == srcBitmap)  
        {  
            return null;  
        }  
          
        // The gap between the reflection bitmap and original bitmap.   
        final int REFLECTION_GAP = 4;  
          
        int srcWidth  = srcBitmap.getWidth();  
        int srcHeight = srcBitmap.getHeight();  
        int reflectionWidth  = srcBitmap.getWidth();  
        int reflectionHeight = srcBitmap.getHeight() / 2;  
          
        if (0 == srcWidth || srcHeight == 0)  
        {  
            return null;  
        }  
          
        // The matrix  
        Matrix matrix = new Matrix();  
        matrix.preScale(1, -1);  
          
        try  
        {  
            // The reflection bitmap, width is same with original's, height is half of original's.  
            Bitmap reflectionBitmap = Bitmap.createBitmap(  
                    srcBitmap,  
                    0,   
                    srcHeight / 2,  
                    srcWidth,   
                    srcHeight / 2,  
                    matrix,  
                    false);  
              
            if (null == reflectionBitmap)  
            {  
                return null;  
            }  
              
            // Create the bitmap which contains original and reflection bitmap.  
            Bitmap bitmapWithReflection = Bitmap.createBitmap(  
                    reflectionWidth,  
                    srcHeight + reflectionHeight + REFLECTION_GAP,   
                    Config.ARGB_8888);  
              
            if (null == bitmapWithReflection)  
            {  
                return null;  
            }  
              
            // Prepare the canvas to draw stuff.  
            Canvas canvas = new Canvas(bitmapWithReflection);  
            Paint paint = new Paint();
            Paint p = new Paint();
            // Draw the original bitmap.  
            canvas.drawBitmap(srcBitmap, 0, 0, null);  
            
            // Draw the reflection bitmap.  
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);  
            
            p.setColor(Color.BLACK);
            p.setAlpha(0x33);
            Rect r = new Rect(0, srcHeight/4*3, srcWidth, srcHeight);
            canvas.drawRect(r, p);
              
            Paint textP =  new Paint();
            textP.setColor(Color.WHITE);
            textP.setTextSize(20);
            FontMetricsInt fontMetrics = textP.getFontMetricsInt();  
             
            int baseline = r.top + (r.bottom - r.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
            // 下面这行实现水平居中，drawText对应改为传入targetRect.centerX()  
            textP.setTextAlign(Paint.Align.CENTER);  
            canvas.drawText(s, r.centerX(), baseline, textP); 
            
            paint.setAntiAlias(true); 
            LinearGradient shader = new LinearGradient(  
                    0,   
                    srcHeight,   
                    0,   
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,   
                    0x70FFFFFF,   
                    0x00FFFFFF,  
                    TileMode.MIRROR);  
            paint.setShader(shader);  
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));  
              
            // Draw the linear shader.  
            canvas.drawRect(  
                    0,   
                    srcHeight,   
                    srcWidth,   
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,   
                    paint);  
              
            return bitmapWithReflection;  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
          
        return null;  
    }  
}  
