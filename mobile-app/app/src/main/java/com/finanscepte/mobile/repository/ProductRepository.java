package com.finanscepte.mobile.repository;
import com.finanscepte.mobile.api.ApiClient;
import com.finanscepte.mobile.api.ProductApi;
import com.finanscepte.mobile.model.ProductDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    public interface ProductCallback {
        void onSuccess(List<ProductDto> products);
        void onError(String message);
    }

    private final ProductApi productApi;

    public ProductRepository() {
        this.productApi = ApiClient.getInstance().create(ProductApi.class);
    }

    public void fetchProducts(ProductCallback callback) {
        productApi.getProducts().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    return;
                }
                callback.onError("Urunler alinamadi. Kod: " + response.code());
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                callback.onError("Baglanti hatasi: " + t.getMessage());
            }
        });
    }
}
