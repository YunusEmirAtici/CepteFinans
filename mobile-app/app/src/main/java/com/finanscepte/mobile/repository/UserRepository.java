package com.finanscepte.mobile.repository;
import com.finanscepte.mobile.api.ApiClient;
import com.finanscepte.mobile.api.UserApi;
import com.finanscepte.mobile.model.UserDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    public interface UserCallback {
        void onSuccess(List<UserDto> users);
        void onError(String message);
    }

    private final UserApi userApi;

    public UserRepository() {
        this.userApi = ApiClient.getInstance().create(UserApi.class);
    }

    public void fetchUsers(UserCallback callback) {
        userApi.getUsers().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    return;
                }
                callback.onError("Kullanicilar alinamadi. Kod: " + response.code());
            }

            @Override
            public void onFailure(Call<List<UserDto>> call, Throwable t) {
                callback.onError("Baglanti hatasi: " + t.getMessage());
            }
        });
    }
}
