package com.finanscepte.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.finanscepte.mobile.model.UserDto;
import com.finanscepte.mobile.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class UserListViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<List<UserDto>> users = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>("");

    public LiveData<List<UserDto>> getUsers() {
        return users;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadUsers() {
        loading.setValue(true);
        error.setValue("");
        userRepository.fetchUsers(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(List<UserDto> userList) {
                users.setValue(userList);
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                users.setValue(new ArrayList<>());
                error.setValue(message);
                loading.setValue(false);
            }
        });
    }
}
