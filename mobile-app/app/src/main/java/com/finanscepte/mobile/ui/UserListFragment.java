package com.finanscepte.mobile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.finanscepte.mobile.R;
import com.finanscepte.mobile.ui.adapter.UserAdapter;
import com.finanscepte.mobile.viewmodel.UserListViewModel;

public class UserListFragment extends Fragment {

    private UserAdapter userAdapter;
    private UserListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerUsers);
        ProgressBar progressBar = view.findViewById(R.id.progressUsers);
        TextView errorText = view.findViewById(R.id.textUserError);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);

        viewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> userAdapter.submitList(users));
        viewModel.isLoading().observe(getViewLifecycleOwner(),
                loading -> progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE));
        viewModel.getError().observe(getViewLifecycleOwner(),
                message -> errorText.setText(message == null ? "" : message));
        viewModel.loadUsers();
    }
}
