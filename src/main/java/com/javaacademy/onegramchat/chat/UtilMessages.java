package com.javaacademy.onegramchat.chat;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UtilMessages {
    String ENTER_USER_NAME = "Введите имя пользователя";
    String ENTER_USER_PASSWORD = "Введите пароль";
    String USER_LOGGED_IN = " Пользователь авторизован";
    String USER_CREATE = " Пользователь создан";
    String USER_LOG_OUT = "Пользователь %s вышел из OneGramChat\n";
    String USER_DO_NOT_EXIST_ERROR = "Такого пользователя нет";
    String USER_IS_NOT_LOGGED_IN = "Вы не авторизованы";
    String ERROR_WORD = "Ошибка! ";
    String USER_FIELD_NULL = "Введено пустое имя или пароль";
    String RECIPIENT_USER = "Введите имя адресата";
    String RECIPIENT_LETTER = "Введите текст письма";
    String USER_SENT_EMAIL = "Вы отправили письмо адресату %s\n";
    String INCOMING_PATTERN = "Письмо от %s: %s\n";
    String OUTGOING_PATTERN = "Письмо к %s: %s\n";
    String USER_HAS_NO_INCOMING_EMAIL = "У вас нет входящих писем";
    String USER_HAS_NO_OUTGOING_EMAIL = "У вас нет исходящих писем";
    String CHAT_GREETING =
            "Вас приветствует OneGramChat!\nВведите команду что вы хотите сделать:";
    String CHAT_GREETING_LOG_IN_USER =
            "Вы вошли в OneGramChat как %s!\nВведите команду что вы хотите сделать:\n";
    String CHAT_EXIT_MESSAGE = "До встречи в OneGramChat!\nПрограмма закончила работу";
    String DELIMITER = "==========================";
    String AVAILABLE_COMMANDS = "[новый] [войти] [написать] [прочитать] [выйти] [exit]";
    String WRONG_COMMAND = "Команда не поддерживается или введена пустая строка";
}
