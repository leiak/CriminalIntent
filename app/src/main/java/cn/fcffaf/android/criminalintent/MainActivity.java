package cn.fcffaf.android.criminalintent;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    private static final String TAG = "mainActivity";

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
