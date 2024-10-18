package com.javaacademy.onegramchat.message;

import com.javaacademy.onegramchat.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Message {
    @NonNull
    String text;
    @NonNull
    User sender;
    @NonNull
    User recipient;
}
