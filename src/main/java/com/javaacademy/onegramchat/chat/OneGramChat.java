package com.javaacademy.onegramchat.chat;

import com.javaacademy.onegramchat.message.Message;
import com.javaacademy.onegramchat.message.MessageType;
import com.javaacademy.onegramchat.user.User;

import java.util.ArrayList;
import java.util.Scanner;

public class OneGramChat {
    private static final String ENTER_USER_NAME = "Введите имя пользователя";
    private static final String ENTER_USER_PASSWORD = "Введите пароль";
    private static final String USER_LOGGED_IN = " Пользователь авторизован";
    private static final String USER_CREATE = " Пользователь создан";
    private static final String USER_LOG_OUT = "Пользователь %s вышел из OneGramChat\n";
    private static final String USER_DO_NOT_EXIST_ERROR = "Такого пользователя нет";
    private static final String USER_IS_NOT_LOGGED_IN = "Вы не авторизованы";
    private static final String RECIPIENT_USER = "Введите имя адресата";
    private static final String RECIPIENT_LETTER = "Введите текст письма";
    private static final String USER_SENT_EMAIL = "Вы отправили письмо адресату %s\n";
    private static final String INCOMING_PATTERN = "Письмо от %s: %s";
    private static final String OUTGOING_PATTERN = "Письмо к %s: %s";
    private static final String CHAT_GREETING =
            "Вас приветствует OneGramChat!\nВведите команду что вы хотите сделать:";
    private static final String CHAT_GREETING_LOG_IN_USER =
            "Вы вошли в OneGramChat как %s!\nВведите команду что вы хотите сделать:\n";
    private static final String CHAT_EXIT_MESSAGE = "До встречи в OneGramChat";
    private static final String CHAT_COMMAND_LOG_IN = "войти";
    private static final String CHAT_COMMAND_CREATE_USER = "новый";
    private static final String CHAT_COMMAND_LOG_OUT = "выйти";
    private static final String CHAT_COMMAND_WRITE = "написать";
    private static final String CHAT_COMMAND_READ = "прочитать";
    private static final String CHAT_COMMAND_EXIT = "exit";
    private static final Scanner scanner = new Scanner(System.in);

    public ArrayList<User> usersList;
    private User currentUser;

    public OneGramChat() {
        usersList = new ArrayList<>();
    }

    // Запуск чата
    public void startingTheChat() {
        // Нет пользователей
        if (usersList.isEmpty()) {
            noUserChat();
            // Есть не авторизованный пользователь
        } else if (!usersList.isEmpty() && currentUser == null) {
            isNoAuthorizedUserChat();
            // Есть авторизованный пользователь
        } else if (!usersList.isEmpty() && currentUser != null) {
            isAuthorizedUserChat();
        }
        scanner.close(); // @Cleanup
    }

    // Команды чата при отсутствии пользователей
    private void noUserChat() {
        System.out.println(CHAT_GREETING);
        System.out.println(CHAT_COMMAND_CREATE_USER + " * " + CHAT_COMMAND_EXIT);
        String command = scanner.next();
        if (command.equals(CHAT_COMMAND_CREATE_USER)) {
            createUser();
        } else if (command.equals(CHAT_COMMAND_EXIT)) {
            System.out.println(CHAT_EXIT_MESSAGE);
            System.exit(0);
        }
    }

    // Команды чата при наличии не авторизованного пользователя
    private void isNoAuthorizedUserChat() {
        System.out.println(CHAT_GREETING);
        System.out.println(CHAT_COMMAND_CREATE_USER + " * " + CHAT_COMMAND_LOG_IN +
                " * " + CHAT_COMMAND_EXIT);
        String command = scanner.next();
        if (command.equals(CHAT_COMMAND_CREATE_USER)) {
            createUser();
        } else if (command.equals(CHAT_COMMAND_LOG_IN)) {
            logInUser();
        } else if (command.equals(CHAT_COMMAND_EXIT)) {
            System.out.println(CHAT_EXIT_MESSAGE);
            System.exit(0);
        }
    }

    // Команды чата при наличии авторизованного пользователя
    private void isAuthorizedUserChat() {
        System.out.printf(CHAT_GREETING_LOG_IN_USER, currentUser.getName());
        System.out.println(CHAT_COMMAND_WRITE + " * " + CHAT_COMMAND_READ + " * "
                + CHAT_COMMAND_LOG_OUT + " * " + CHAT_COMMAND_EXIT);
        String command = scanner.next();
        if (command.equals(CHAT_COMMAND_WRITE)) {
            writeLetter();
        } else if (command.equals(CHAT_COMMAND_READ)) {
            printIncomingMessage();
        } else if (command.equals(CHAT_COMMAND_LOG_OUT)) {
            logOutUser();
        } else if (command.equals(CHAT_COMMAND_EXIT)) {
            System.out.println(CHAT_EXIT_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Создание пользователя
     */
    public void createUser() {
        String name = askUserName();
        String password = askUserPassword();
        User user = new User(name, password);
        usersList.add(user);
        System.out.println(name + USER_CREATE);
        System.out.println("==========================");
        startingTheChat();
    }

    /**
     * Авторизация пользователя
     */
    public void logInUser() {
        String name = askUserName();
        String password = askUserPassword();
        usersList.stream()
                .filter(person -> person.getName().equals(name) &&
                        person.getPassword().equals(password))
                .findAny()
                .ifPresentOrElse(
                        person -> {
                            System.out.println(name + USER_LOGGED_IN);
                            currentUser = person;
                            System.out.println("==========================");
                            startingTheChat();
                        },
                        () -> {
                            throw new RuntimeException(USER_DO_NOT_EXIST_ERROR);
                        });
    }

    /**
     * Выход пользователя из системы
     */
    public void logOutUser() {
        System.out.printf(USER_LOG_OUT, currentUser.getName());
        System.out.println("==========================");
        currentUser = null;
        startingTheChat();
    }

    /**
     * Считывание ответа на запрос системой имени пользователя
     */
    private String askUserName() {
        System.out.println(ENTER_USER_NAME);
        return scanner.next();
    }

    /**
     * Считывание ответа на запрос системой пароля пользователя
     */
    private String askUserPassword() {
        System.out.println(ENTER_USER_PASSWORD);
        return scanner.next();
    }

    /**
     * Запрос у пользователя имени адресата
     */
    private String askNameUserForLetter() {
        System.out.println(RECIPIENT_USER);
        return scanner.next();
    }

    /**
     * Запрос у пользователя текста сообщения
     */
    private String askTextForLetter() {
        System.out.println(RECIPIENT_LETTER);
        scanner.nextLine();
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
        System.out.printf(USER_SENT_EMAIL, recipient);
        System.out.println("==========================");
        startingTheChat();
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
                        INCOMING_PATTERN.formatted(message.getSender().getName(),
                                message.getText())));
        System.out.println("==========================");
        startingTheChat();
    }

    /**
     * Печать в консоль всех исходящих сообщений пользователя
     */
    private void printOutgoingMessage() {
        currentUser
                .getMessages()
                .get(MessageType.OUTGOING)
                .forEach(message -> System.out.println(
                        OUTGOING_PATTERN.formatted(message.getSender().getName(),
                                message.getText())));
    }
}
