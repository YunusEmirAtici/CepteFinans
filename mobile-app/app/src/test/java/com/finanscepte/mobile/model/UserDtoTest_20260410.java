package com.finanscepte.mobile.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserDtoTest_20260410 {

    @Test
    void shouldHoldUserFields() {
        UserDto userDto = new UserDto();
        userDto.id = "1";
        userDto.username = "yunus";
        userDto.email = "y@x.com";

        assertEquals("yunus", userDto.username);
    }
}
