package ttc.project.stoku.fragment;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;


import java.util.Locale;

import ttc.project.stoku.R;
import ttc.project.stoku.activity.SettingActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.lang_key))){
            ListPreference pref = (ListPreference) findPreference(s);
            String val = pref.getValue();
            if(val.equals(getString(R.string.val_ID))){
                pref.setSummary(getString(R.string.lang_ID));
            } else if(val.equals(getString(R.string.val_EN))){
                pref.setSummary(getString(R.string.lang_EN));
            }

            Locale locale = new Locale(pref.getValue());
            Locale.setDefault(locale);
            ((SettingActivity)getActivity()).setLanguageSettingActivity();
//            Configuration config = new Configuration();
//            config.locale = locale;
//            getActivity().getBaseContext().getResources().updateConfiguration(config, null);
//            getActivity().recreate();
//            finish();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_setting, container, false);
//        return view;
//    }


}
