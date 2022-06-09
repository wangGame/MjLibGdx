package com.kw.gdx.annotation;

import com.kw.gdx.ads.Constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameInfo {
    float width() default Constant.STDWIDTH;
    float height() default Constant.STDHIGHT;
    int batch() default Constant.SPRITEBATCH;
    int viewportType() default Constant.EXTENDVIEWPORT;
}
