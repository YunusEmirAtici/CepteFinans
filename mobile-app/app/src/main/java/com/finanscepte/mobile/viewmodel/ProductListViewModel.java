package com.finanscepte.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.finanscepte.mobile.model.ProductDto;
import com.finanscepte.mobile.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;

public class ProductListViewModel extends ViewModel {

    private final ProductRepository productRepository = new ProductRepository();
    private final MutableLiveData<List<ProductDto>> products = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>("");

    public LiveData<List<ProductDto>> getProducts() {
        return products;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadProducts() {
        loading.setValue(true);
        error.setValue("");
        productRepository.fetchProducts(new ProductRepository.ProductCallback() {
            @Override
            public void onSuccess(List<ProductDto> productList) {
                products.setValue(productList);
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                products.setValue(new ArrayList<>());
                error.setValue(message);
                loading.setValue(false);
            }
        });
    }
}
