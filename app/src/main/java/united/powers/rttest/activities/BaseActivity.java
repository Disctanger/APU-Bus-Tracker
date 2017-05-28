package united.powers.rttest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import united.powers.rttest.activities.fragments.CpanelNavigation;
import united.powers.rttest.R;


public class BaseActivity extends SlidingFragmentActivity {

    public int mTitleRes;

    protected ListFragment mFrag;
    public static final String TITLERES = "titleres";

    public BaseActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    public BaseActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setTitle(mTitleRes);

        Bundle bundle = new Bundle();
        bundle.putInt(TITLERES, mTitleRes);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new CpanelNavigation();
            mFrag.setArguments(bundle);
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindScrollScale(1);




    }

}
