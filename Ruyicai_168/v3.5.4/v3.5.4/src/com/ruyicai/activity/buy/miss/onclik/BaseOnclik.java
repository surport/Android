package com.ruyicai.activity.buy.miss.onclik;

import android.view.View;
import android.widget.LinearLayout;

public abstract class BaseOnclik {
  protected int layoutHeight;
  protected View layoutMain;
  public abstract void setRadioLayout(LinearLayout radioLayout);
  public abstract void setLayoutMian(View radioLayout);
  public abstract void onClik(float x,float y);
}
