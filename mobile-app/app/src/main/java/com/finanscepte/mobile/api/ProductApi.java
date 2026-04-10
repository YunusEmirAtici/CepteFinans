package com.finanscepte.mobile.api;

import com.finanscepte.mobile.model.ProductDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("/api/products")
    Call<List<ProductDto>> getProducts();
}
