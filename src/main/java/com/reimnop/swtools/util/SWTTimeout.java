package com.reimnop.swtools.util;

import io.wispforest.owo.ui.base.BaseOwoToast;
import io.wispforest.owo.ui.core.ParentComponent;
import net.minecraft.client.toast.Toast;

@SuppressWarnings("UnstableApiUsage")
public class SWTTimeout<R extends ParentComponent> implements BaseOwoToast.VisibilityPredicate<R> {
    private final long duration;
    private long startTime = -1;

    public SWTTimeout(long duration) {
        this.duration = duration;
    }

    @Override
    public Toast.Visibility test(BaseOwoToast<R> toast, long time) {
        if (startTime < 0) {
            startTime = time;
        }
        return time - startTime < duration ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }
}
