package com.example.da_mientay_admin.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;
import com.example.da_mientay_admin.Callback.ButtonClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipHelper extends ItemTouchHelper.SimpleCallback {

    int btnWidth;
    private  RecyclerView recyclerView;
    private List<MyButton> btnList;
    private GestureDetector gestureDetector; // Nhận diện thu thập sự kiện chạm
    private  int swipePosition = -1;
    private  float swipeThreshold = 0.5f;
    private Map<Integer,List<MyButton>> btnBuffer;
    private Queue<Integer> removeQueue;


    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for(MyButton button:btnList)
                if(button.onClick(e.getX(),e.getY()))
                    break;
                return true;
        }
    };

    public SwipHelper(Context context, RecyclerView recyclerView, int btnWidth)
    {
        super(0,ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.btnList = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context,gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.btnBuffer = new HashMap<>();
        this.btnWidth = btnWidth;

        removeQueue = new LinkedList<Integer>()
        {
            @Override
            public boolean add(Integer integer) {
                if(contains(integer))
                    return  false;
                else
                    return  super.add(integer);
            }
        };
        attachSwipe();
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private  synchronized void recoverSwipedItem()
    {
        while (!removeQueue.isEmpty())
        {
            int pos = removeQueue.poll();
            if(pos >-1)
                recyclerView.getAdapter().notifyItemChanged(pos);
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(swipePosition <0) return false;
            Point point = new Point((int)event.getRawX(),(int)event.getRawY());
            RecyclerView.ViewHolder swipViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);
            View swipedItem = swipViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);


            if(event.getAction() == event.ACTION_DOWN||
                    event.getAction() == event.ACTION_UP ||
                    event.getAction() == event.ACTION_MOVE) {

                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(event);
                } else {
                    removeQueue.add(swipePosition);
                    swipePosition = -1;
                }

            }
            return false;
        }

    };

    public class MyButton
    {
        private  String text;
        private  int imgResId,textSize,color,pos;
        private  RectF clickRegion; //RectF tọa độ 4 góc Rectangle
        private ButtonClickListener listenner;
        private Context context;
        private Resource resource;

        public MyButton(Context context,String text, int textSize, int imgResId, int color, ButtonClickListener listenner ) {
            this.text = text;
            this.imgResId = imgResId;
            this.textSize = textSize;
            this.color = color;
            this.listenner = listenner;
            this.context = context;
        }

        public boolean onClick(float x, float y)
        {
            if(clickRegion !=null && clickRegion.contains(x,y))
            {
                listenner.onClick(pos);
                return  true;

            }
            return false;
        }

        public void onDraw(Canvas c,RectF rectF,int pos)
        {
            Paint p = new Paint();
            p.setColor(color);
            c.drawRect(rectF,p);

            //Text
            p.setColor(Color.WHITE);
            p.setTextSize(textSize);

            Rect r = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text,0,text.length(),r);
            float x=0,y=0;
            if(imgResId == 0 ) // Neu la text
            {
                x = cWidth/2f-r.width()/2f-r.left;
                y = cHeight/2f +r.height()/2f- r.bottom;
                c.drawText(text,rectF.left+x,rectF.top+y,p);
            }

            else // neu co image
            {
                Drawable d = ContextCompat.getDrawable(context,imgResId);
                Bitmap bitmap = drawToBitmap(d);
                c.drawBitmap(bitmap,(rectF.left+rectF.right)/2,(rectF.top+rectF.bottom)/2,p);
            }
            clickRegion =rectF;
            this.pos = pos;
        }


    }

    private Bitmap drawToBitmap(Drawable d) {
        if(d instanceof BitmapDrawable)
            return ((BitmapDrawable)d).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas((bitmap));
        d.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        d.draw(canvas);
        return  bitmap;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    int pos = viewHolder.getAdapterPosition();
    if(swipePosition != pos)
        removeQueue.add(swipePosition);
    swipePosition = pos;
    if(btnBuffer.containsKey(swipePosition))
        btnList = btnBuffer.get(swipePosition);
    else
        btnList.clear();
    btnBuffer.clear();
    swipeThreshold = 0.5f * btnList.size()* btnWidth;
    recoverSwipedItem();
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f*defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return  5.0f*defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float transX = dX;
        View itemView = viewHolder.itemView;
        if(pos<0)
        {
            swipePosition =pos;
            return;
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            if(dX<0)
            {
                List<MyButton> buffer = new ArrayList<>();
                if(!btnBuffer.containsKey(pos))
                {
                    initMyButton(viewHolder,buffer);
                    btnBuffer.put(pos,buffer);
                }
                else
                {
                    buffer = btnBuffer.get(pos);
                }
                transX = dX * buffer.size()* btnWidth/itemView.getWidth();
                drawButton(c,itemView,buffer,pos,transX);
            }
        }
        super.onChildDraw(c,recyclerView,viewHolder,transX,dY,actionState,isCurrentlyActive);
    }

    private void drawButton(Canvas c, View itemView, List<MyButton> buffer, int pos, float transX) {
        float right  = itemView.getRight();
        float dButtonWidth = -1* transX / buffer.size();
        for(MyButton button: buffer)
        {
            float left = right - dButtonWidth;
            button.onDraw(c,new RectF(left,itemView.getTop(),right,itemView.getBottom()),pos);
            right = left;
        }

    }
    public  abstract  void initMyButton(RecyclerView.ViewHolder viewHolder,List<MyButton> buf);
}
