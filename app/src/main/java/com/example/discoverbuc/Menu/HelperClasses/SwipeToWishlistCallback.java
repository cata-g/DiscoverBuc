package com.example.discoverbuc.Menu.HelperClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverbuc.R;

public class SwipeToWishlistCallback extends ItemTouchHelper.Callback {

    Context context;
    private Paint clearPaint;
    private ColorDrawable background;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;

    protected SwipeToWishlistCallback(Context context){
        this.context = context;
        background = new ColorDrawable();
        backgroundColor = Color.parseColor("#fab73d");
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(this.context, R.drawable.icon_wishlist);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX * 0.8f, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;

        boolean isCanceled = dX == 0 && !isCurrentlyActive;

        if(isCanceled ){
            clearCanvas(c, (float) itemView.getLeft(), (float) itemView.getTop(), itemView.getRight() + dX, (float) itemView.getBottom());
            super.onChildDraw(c,recyclerView, viewHolder, dX * 0.8f, dY, actionState, isCurrentlyActive);
            return;
        }

        background.setColor(backgroundColor);
        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight() + (int) dX, itemView.getBottom());
        background.draw(c);


    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

}
