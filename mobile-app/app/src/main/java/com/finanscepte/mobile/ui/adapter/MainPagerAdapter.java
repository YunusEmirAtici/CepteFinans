package com.finanscepte.mobile.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.finanscepte.mobile.ui.ProductListFragment;
import com.finanscepte.mobile.ui.UserListFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new UserListFragment();
        }
        return new ProductListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
