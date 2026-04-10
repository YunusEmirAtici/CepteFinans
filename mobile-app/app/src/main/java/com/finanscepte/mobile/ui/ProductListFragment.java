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
import com.finanscepte.mobile.ui.adapter.ProductAdapter;
import com.finanscepte.mobile.viewmodel.ProductListViewModel;

public class ProductListFragment extends Fragment {

    private ProductAdapter productAdapter;
    private ProductListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerProducts);
        ProgressBar progressBar = view.findViewById(R.id.progressProducts);
        TextView errorText = view.findViewById(R.id.textProductError);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> productAdapter.submitList(products));
        viewModel.isLoading().observe(getViewLifecycleOwner(),
                loading -> progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE));
        viewModel.getError().observe(getViewLifecycleOwner(),
                message -> errorText.setText(message == null ? "" : message));
        viewModel.loadProducts();
    }
}
