package com.javaacademy.onegramchat.chat;

import com.javaacademy.onegramchat.message.Message;
import com.javaacademy.onegramchat.message.MessageType;
import com.javaacademy.onegramchat.user.User;

import java.util.ArrayList;
import java.util.Scanner;

public class OneGramChat {
    private static final String ENTER_USER_NAME = "Введите имя пользователя";
    private static final String ENTER_USER_PASSWORD = "Введите пароль";
    private static final String USER_LOGGED_IN = "Пользователь авторизован";
    private static final String USER_DO_NOT_EXIST_ERROR = "Такого пользователя нет";
    private static final String USER_IS_NOT_LOGGED_IN = "Вы не авторизованы";
    private static final String RECIPIENT_USER = "Введите имя адресата";
    private static final String RECIPIENT_LETTER = "Введите текст письма";
    private static final String INCOMING_PATTERN = "Письмо от %s: %s";
    private static final String OUTGOING_PATTERN = "Письмо к %s: %s";
    private static final Scanner scanner = new Scanner(System.in);

    public ArrayList<User> usersList;
    private User currentUser;

    public OneGramChat() {
        usersList = new ArrayList<>();
    }

    /**
     * Создание пользователя
     */
    public void createUser() {
        String name = askUserName();
        String password = askUserPassword();
        User user = new User(name, password);
        usersList.add(user);
    }

    /**
     * Авторизция пользователя
     */
    public void logInUser() {
        String name = askUserName();
        String password = askUserPassword();
        usersList.stream()
                .filter(person -> person.getName().equals(name) && person.getPassword().equals(password))
                .findAny()
                .ifPresentOrElse(
                        person -> {
                            System.out.println(USER_LOGGED_IN);
                            currentUser = person;
                            },
                        () -> {
                            throw new RuntimeException(USER_DO_NOT_EXIST_ERROR);
                        });
    }

    /**
     * Выход пользователя из системы
     */
    public void logOutUser() {
        currentUser = null;
    }

    /**
     * Считывание ответа на запрос системой имени пользователя
     */
    private String askUserName() {
        System.out.println(ENTER_USER_NAME);
        return scanner.nextLine();
    }

    /**
     * Считывание ответа на запрос системой пароля пользователя
     */
    private String askUserPassword() {
        System.out.println(ENTER_USER_PASSWORD);
        return scanner.nextLine();
    }

    /**
     * Запрос у пользователя имени адресата
     */
    private String askNameUserForLetter() {
        System.out.println(RECIPIENT_USER);
        return scanner.nextLine();
    }

    /**
     * Запрос у пользователя текста сообщения
     */
    private String askTextForLetter() {
        System.out.println(RECIPIENT_LETTER);
        return scanner.nextLine();
    }

    /**
     * Запись сообщения текущему пользователю
     */
    private void writeMessageToCurrentUser(Message message) {
        currentUser
                .getMessages()
                .get(MessageType.OUTGOING)
                .add(message);
    }

    /**
     * Поиск получателя сообщения
     */
    private User findRecipientUser(String name) {
        return usersList
                .stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(USER_DO_NOT_EXIST_ERROR));
    }

    /**
     * Запись сообщения получателю
     */
    private void writeMessageToRecipientUser(String nameUser, Message message) {
        findRecipientUser(nameUser)
                .getMessages()
                .get(MessageType.INCOMING)
                .add(message);
    }

    /**
     * Запись полученных имени адресата и письма в исходящие и исходящие сообщения
     */
    public void writeLetter() {
        if (currentUser == null) {
            throw new RuntimeException(USER_IS_NOT_LOGGED_IN);
        }
        String recipient = askNameUserForLetter();
        String letter = askTextForLetter();
        Message message = new Message(letter, currentUser, findRecipientUser(recipient));
        writeMessageToCurrentUser(message);
        writeMessageToRecipientUser(recipient, message);
    }

    /**
     * Чтение и вывод в консоль всех сообщений пользователя
     */
    public void readLetters() {
        if (currentUser == null) {
            throw new RuntimeException(USER_IS_NOT_LOGGED_IN);
        }
        printIncomingMessage();
        printOutgoingMessage();
    }

    /**
     * Печать в консоль всех входящих сообщений пользователя
     */
    private void printIncomingMessage() {
        currentUser
                .getMessages()
                .get(MessageType.INCOMING)
                .forEach(message -> System.out.println(
                        INCOMING_PATTERN.formatted(message.getSender().getName(), message.getText())));
    }

    /**
     * Печать в консоль всех исходящих сообщений пользователя
     */
    private void printOutgoingMessage() {
        currentUser
                .getMessages()
                .get(MessageType.OUTGOING)
                .forEach(message -> System.out.println(
                        OUTGOING_PATTERN.formatted(message.getSender().getName(), message.getText())));
    }
}