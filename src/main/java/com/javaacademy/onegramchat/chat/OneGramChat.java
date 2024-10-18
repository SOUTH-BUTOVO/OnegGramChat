package com.javaacademy.onegramchat.chat;

import com.javaacademy.onegramchat.message.Message;
import com.javaacademy.onegramchat.message.MessageType;
import com.javaacademy.onegramchat.user.User;
import com.javaacademy.onegramchat.user.UserErrorHandlerException;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.javaacademy.onegramchat.chat.UtilMessages.*;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OneGramChat {
    static final String CHAT_COMMAND_LOG_IN = "войти";
    static final String CHAT_COMMAND_CREATE_USER = "новый";
    static final String CHAT_COMMAND_LOG_OUT = "выйти";
    static final String CHAT_COMMAND_WRITE = "написать";
    static final String CHAT_COMMAND_READ = "прочитать";
    static final String CHAT_COMMAND_EXIT = "exit";

    @NonNull
    List<User> usersList;
    User currentUser;

    /**
     * Основной метод
     */
    public static void start() {
        val oneGramChat = new OneGramChat(new ArrayList<>());
        oneGramChat.startingTheChat();
    }

    /**
     * Запуск чата
     */
    private void startingTheChat() {
        @Cleanup
        val scanner = new Scanner(System.in);
        String command;
        do {
            checkUser();
            System.out.println(AVAILABLE_COMMANDS);
            command = scanner.nextLine();
            try {
                doAction(command, scanner);
            } catch (UserErrorHandlerException e) {
                System.out.println(ERROR_WORD + e.getMessage() + "\n" + DELIMITER);
                startingTheChat();
            }
        } while (!isCorrectCommand(command));
    }

    /**
     * Проверка на авторизованного пользователя
     */
    private void checkUser() {
        if (currentUser != null) {
            System.out.printf(CHAT_GREETING_LOG_IN_USER, currentUser.getName());
        } else {
            System.out.println(CHAT_GREETING);
        }
    }

    /**
     * Действия по командам
     */
    private void doAction(String command, Scanner scanner) {
        switch (command) {
            case CHAT_COMMAND_CREATE_USER -> createUser(scanner);
            case CHAT_COMMAND_LOG_IN -> logInUser(scanner);
            case CHAT_COMMAND_WRITE -> writeLetter(scanner);
            case CHAT_COMMAND_READ -> readLetters();
            case CHAT_COMMAND_LOG_OUT -> logOutUser();
            case CHAT_COMMAND_EXIT -> {
                System.out.println(CHAT_EXIT_MESSAGE);
                System.exit(0);
            }
            default -> {
                System.out.println(WRONG_COMMAND + "\n" + DELIMITER);
                startingTheChat();
            }
        }
    }

    /**
     * Проверка на возможные команды чата
     */
    private boolean isCorrectCommand(String command) {
        return (command.equals(CHAT_COMMAND_CREATE_USER) || command.equals(CHAT_COMMAND_LOG_IN)
                || command.equals(CHAT_COMMAND_WRITE) || command.equals(CHAT_COMMAND_READ)
                || command.equals(CHAT_COMMAND_LOG_OUT) || command.equals(CHAT_COMMAND_EXIT));
    }

    /**
     * Создание пользователя
     */
    private void createUser(Scanner scanner) {
        val name = askUserName(scanner);
        val password = askUserPassword(scanner);
        if (name.isEmpty() || password.isEmpty()) {
            throw new UserErrorHandlerException(USER_FIELD_NULL);
        }
        HashMap<MessageType, ArrayList<Message>> messages = new HashMap<>();
        messages.put(MessageType.OUTGOING, new ArrayList<>());
        messages.put(MessageType.INCOMING, new ArrayList<>());
        val user = new User(name, password, messages);
        usersList.add(user);
        System.out.println(user.getName() + USER_CREATE + "\n" + DELIMITER);
        startingTheChat();
    }

    /**
     * Авторизация пользователя
     */
    private void logInUser(Scanner scanner) {
        val name = askUserName(scanner);
        val password = askUserPassword(scanner);
        usersList.stream()
                .filter(person -> person.getName().equals(name)
                        && person.getPassword().equals(password))
                .findAny()
                .ifPresentOrElse(
                        person -> {
                            System.out.println(name + USER_LOGGED_IN + "\n" + DELIMITER);
                            currentUser = person;
                            startingTheChat();
                        },
                        () -> {
                            throw new UserErrorHandlerException(USER_DO_NOT_EXIST_ERROR);
                        }
                );
    }

    /**
     * Выход пользователя из системы
     */
    private void logOutUser() {
        if (currentUser == null) {
            throw new UserErrorHandlerException(USER_IS_NOT_LOGGED_IN);
        }
        System.out.printf(USER_LOG_OUT, currentUser.getName());
        System.out.println(DELIMITER);
        currentUser = null;
        startingTheChat();
    }

    /**
     * Считывание ответа на запрос системой имени пользователя
     */
    private String askUserName(Scanner scanner) {
        System.out.println(ENTER_USER_NAME);
        return scanner.nextLine();
    }

    /**
     * Считывание ответа на запрос системой пароля пользователя
     */
    private String askUserPassword(Scanner scanner) {
        System.out.println(ENTER_USER_PASSWORD);
        return scanner.nextLine();
    }

    /**
     * Запрос у пользователя имени адресата
     */
    private String askNameUserForLetter(Scanner scanner) {
        System.out.println(RECIPIENT_USER);
        return scanner.nextLine();
    }

    /**
     * Запрос у пользователя текста сообщения
     */
    private String askTextForLetter(Scanner scanner) {
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
    private User findRecipientUser(String name) throws UserErrorHandlerException {
        return usersList
                .stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new UserErrorHandlerException(USER_DO_NOT_EXIST_ERROR));
    }

    /**
     * Запись сообщения получателю
     */
    private void writeMessageToRecipientUser(String nameUser, Message message) throws UserErrorHandlerException {
        findRecipientUser(nameUser)
                .getMessages()
                .get(MessageType.INCOMING)
                .add(message);
    }

    /**
     * Запись полученных имени адресата и письма в исходящие и исходящие сообщения
     */
    private void writeLetter(Scanner scanner) {
        if (currentUser == null) {
            throw new UserErrorHandlerException(USER_IS_NOT_LOGGED_IN);
        }
        val recipient = askNameUserForLetter(scanner);
        val letter = askTextForLetter(scanner);
        val message = new Message(letter, currentUser, findRecipientUser(recipient));
        writeMessageToCurrentUser(message);
        writeMessageToRecipientUser(recipient, message);
        System.out.printf(USER_SENT_EMAIL, recipient);
        System.out.println(DELIMITER);
        startingTheChat();
    }

    /**
     * Чтение и вывод в консоль всех сообщений пользователя
     */
    private void readLetters() throws UserErrorHandlerException {
        if (currentUser == null) {
            throw new UserErrorHandlerException(USER_IS_NOT_LOGGED_IN);
        }
        printIncomingMessage();
        printOutgoingMessage();
        System.out.println(DELIMITER);
        startingTheChat();
    }

    /**
     * Печать в консоль всех входящих сообщений пользователя
     */
    private void printIncomingMessage() {
        val messages = currentUser.getMessages().get(MessageType.INCOMING);
        if (messages.isEmpty()) {
            System.out.println(USER_HAS_NO_INCOMING_EMAIL);
        } else {
            messages.forEach(message -> System.out.printf(
                    INCOMING_PATTERN, message.getSender().getName(), message.getText()));
        }
    }

    /**
     * Печать в консоль всех исходящих сообщений пользователя
     */
    private void printOutgoingMessage() {
        ArrayList<Message> messages = currentUser.getMessages().get(MessageType.OUTGOING);
        if (messages.isEmpty()) {
            System.out.println(USER_HAS_NO_OUTGOING_EMAIL);
        } else {
            messages.forEach(message -> System.out.printf(
                    OUTGOING_PATTERN, message.getRecipient().getName(), message.getText()));
        }
    }
}
