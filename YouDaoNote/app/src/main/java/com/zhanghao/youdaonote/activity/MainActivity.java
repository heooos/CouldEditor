package com.zhanghao.youdaonote.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.fragment.NoteFragment;
import com.zhanghao.youdaonote.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private long exitTime;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private LinearLayout noteTab;
    private LinearLayout settingTab;

    private ImageButton noteImg;
    private ImageButton settingImg;

    private Fragment noteFragment;
    private Fragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        init();
        initEvent();
        selectTab(0);
    }

    private void init(){

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        noteTab = (LinearLayout) findViewById(R.id.note_tab);
        settingTab = (LinearLayout) findViewById(R.id.setting_tab);

        noteImg = (ImageButton) findViewById(R.id.note_imgBtn);
        settingImg = (ImageButton) findViewById(R.id.setting_imgBtn);

        fragmentList = new ArrayList<>();
        noteFragment = new NoteFragment();
        settingFragment = new SettingFragment();
        fragmentList.add(noteFragment);
        fragmentList.add(settingFragment);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = viewPager.getCurrentItem();
                selected(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initEvent(){

        noteTab.setOnClickListener(this);
        settingTab.setOnClickListener(this);
    }

    private void selected(int i){

        resetImgs();
        switch (i){
            case 0:
                noteImg.setImageResource(R.drawable.note_selected);
                break;
            case 1:
                settingImg.setImageResource(R.drawable.setting_selected);
                break;
        }
    }

    private void selectTab(int i){
        selected(i);
        viewPager.setCurrentItem(i);
    }

    //设置图标全灭
    private void resetImgs() {
        noteImg.setImageResource(R.drawable.note_normal);
        settingImg.setImageResource(R.drawable.setting_normal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id){
            case R.id.action_settingOne:
                Toast.makeText(this,"action_settingOne",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settingTwo:
                Toast.makeText(this,"action_settingTwo",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settingThree:
                Toast.makeText(this,"action_settingThree",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (System.currentTimeMillis() - exitTime > 2000){
                    Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.note_tab:
                selectTab(0);
                break;
            case R.id.setting_tab:
                selectTab(1);
                break;
        }
    }
}
