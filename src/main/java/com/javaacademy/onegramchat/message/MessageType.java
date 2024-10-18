package com.javaacademy.onegramchat.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MessageType {
    INCOMING("Входящие"),
    OUTGOING("Исходящие");

    String type;
}
