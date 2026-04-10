package com.finanscepte.mobile.api;

import com.finanscepte.mobile.model.UserDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("/api/users")
    Call<List<UserDto>> getUsers();
}
