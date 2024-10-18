package com.javaacademy.onegramchat.user;

import com.javaacademy.onegramchat.message.Message;
import com.javaacademy.onegramchat.message.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class User {
    @NonNull
    String name;
    @NonNull
    String password;
    private HashMap<MessageType, ArrayList<Message>> messages;
}
