package com.xiecc.seeWeather.modules.ui.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseApplication;
import com.xiecc.seeWeather.common.ACache;
import com.xiecc.seeWeather.common.FileSizeUtil;
import com.xiecc.seeWeather.modules.domain.Setting;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private static String TAG = SettingFragment.class.getSimpleName();
    //private SettingActivity mActivity;
    private Setting mSetting;
    private Preference mChangeIcons;
    private Preference mClearCache;

    private ACache mACache;

    //public SettingFragment() {}
    //@Override public void onAttach(Context context) {
    //    super.onAttach(context);
    //    mActivity = (SettingActivity) context;
    //}


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSetting = Setting.getInstance();
        mACache = ACache.get(getActivity());

        mChangeIcons = findPreference(Setting.CHANGE_ICONS);
        mClearCache = findPreference(Setting.CLEAR_CACHE);

        mChangeIcons.setSummary(getResources().getStringArray(R.array.icons)[mSetting.getInt(Setting.CHANGE_ICONS, 0)]);
        mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(BaseApplication.cacheDir));

        mChangeIcons.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
    }


    @Override public boolean onPreferenceClick(Preference preference) {
        if (preference == mChangeIcons) {
            showDialog();
        }
        else {
            mACache.clear();
            mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(BaseApplication.cacheDir));
            Snackbar.make(getView(), "缓存已清除", Snackbar.LENGTH_SHORT).show();
        }
        return false;
    }


    private void showDialog() {
        new AlertDialog.Builder(getActivity()).setTitle("更换图标")
                                              .setSingleChoiceItems(getResources().getStringArray(R.array.icons),
                                                      mSetting.getInt(Setting.CHANGE_ICONS, 0),
                                                      new DialogInterface.OnClickListener() {
                                                          @Override
                                                          public void onClick(DialogInterface dialog, int which) {
                                                              if (which != mSetting.getInt(Setting.CHANGE_ICONS, 0)) {
                                                                  mSetting.putInt(Setting.CHANGE_ICONS, which);
                                                              }
                                                              dialog.dismiss();
                                                              mChangeIcons.setSummary(getResources().getStringArray(
                                                                      R.array.icons)[mSetting.getInt(
                                                                      Setting.CHANGE_ICONS, 0)]);
                                                              Snackbar.make(getView(), "切换成功,重启应用生效",
                                                                      Snackbar.LENGTH_SHORT).show();
                                                          }
                                                      })
                                              .show();
    }
}
