package com.bm.tzj.caledar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.app.App;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 小日历组件
 */
public class CalendarView_x extends LinearLayout  {

    private int size = 35;

    private List<Calendar> days;

    private int s_index;

    private OnSelectedListener listener;

    private Handler handler = new Handler();

    public CalendarView_x(Context context) {
        super(context);
        init();
    }

    public CalendarView_x(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView_x(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Calendar getSelected()
    {
        return days.get(s_index);
    }

    private void init()
    {
        LayoutInflater.from(this.getContext()).inflate(R.layout.v_calendar, this, true);
        final LinearLayout root = (LinearLayout)this.findViewById(R.id.root);
        days = new ArrayList<Calendar>();
        for(int i=0; i<size; i++)
        {
            final LinearLayout v = (LinearLayout)LayoutInflater.from(this.getContext()).inflate(R.layout.v_calendar_item, root, false);
            v.getLayoutParams().width = App.getInstance().getScreenWidth()/7;
            root.addView(v);
            final TextView tv_week = (TextView)v.findViewById(R.id.tv_week);
            final TextView tv_day = (TextView)v.findViewById(R.id.tv_day);
            final Calendar today = Calendar.getInstance();
            today.add(Calendar.DAY_OF_MONTH, i);
            days.add(today);
            int day = today.get(Calendar.DAY_OF_MONTH);
    //        TextView tv_month = (TextView)v.findViewById(R.id.tv_month);
            if(day == 1)
            {
//                tv_month.setVisibility(VISIBLE);
//                int month = today.get(Calendar.MONTH)+1;
//                tv_month.setText(today.get(Calendar.YEAR)+"."+(month<10? "0"+month : ""+month));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                v.getLayoutParams().width = CalendarView_x.this.getWidth()/7;
                                CalendarView_x.this.invalidate();
                            }
                        });
                    }
                }).start();
            }
            else
            {
//                tv_month.setVisibility(GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                v.getLayoutParams().width = CalendarView_x.this.getWidth()/7;
                                CalendarView_x.this.invalidate();
                            }
                        });
                    }
                }).start();
            }
            tv_day.setText(""+day);
            int week = today.get(Calendar.DAY_OF_WEEK);
            switch (week)
            {
                case Calendar.SUNDAY:
                    tv_week.setText("日");
                    break;
                case Calendar.MONDAY:
                    tv_week.setText("一");
                    break;
                case Calendar.TUESDAY:
                    tv_week.setText("二");
                    break;
                case Calendar.WEDNESDAY:
                    tv_week.setText("三");
                    break;
                case Calendar.THURSDAY:
                    tv_week.setText("四");
                    break;
                case Calendar.FRIDAY:
                    tv_week.setText("五");
                    break;
                case Calendar.SATURDAY:
                    tv_week.setText("六");
                    break;
            }

            v.setTag(i);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    LinearLayout sv = (LinearLayout)root.getChildAt(s_index);

                    if(s_index == 0)
                    {
                        ((TextView)sv.findViewById(R.id.tv_day)).setTextColor(0xffA59945);
                        ((TextView)sv.findViewById(R.id.tv_day)).setBackgroundResource(R.drawable.rl_mark1);
                    }
                    else
                    {
                        ((TextView)sv.findViewById(R.id.tv_day)).setTextColor(0xff999999);
                        ((TextView)sv.findViewById(R.id.tv_day)).setBackgroundColor(0);
                    }

                    tv_day.setTextColor(0xffffffff);
                    tv_day.setBackgroundResource(R.drawable.rl_selectedday);
                    int index = (int)v.getTag();
                    s_index = index;

                    if(listener != null)
                    {
                        listener.onSelected(today);
                    }
                }
            });

            if(i==0)
            {
                tv_day.setTextColor(0xffffffff);
                tv_day.setBackgroundResource(R.drawable.rl_selectedday);
            }
            else
            {
                tv_day.setTextColor(0xff999999);
                tv_day.setBackgroundColor(0);
            }
        }
    }

    public void setSelectedIndex(int position)
    {
        ((LinearLayout)this.findViewById(R.id.root)).getChildAt(position).performClick();
    }

    public void setOnSelectedListener(OnSelectedListener l)
    {
        listener = l;
    }

    public static interface OnSelectedListener
    {
        public void onSelected(Calendar date);
    }
}
