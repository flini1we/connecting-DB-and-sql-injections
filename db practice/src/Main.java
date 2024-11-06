import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "123";
    private static final Scanner scanner = new Scanner(System.in);
    private static final Integer numberOfAddingUser = 6;

    public static void main(String[] args) throws SQLException {
        // Устанавливаем соединение с базой данных
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        // Создаем объект Statement для выполнения SQL-запросов
        Statement statement = connection.createStatement();

//        statement.executeUpdate("DELETE FROM driver;");

        // Выполняем запрос на выборку данных из таблицы driver
        ResultSet result = statement.executeQuery("SELECT * FROM driver ORDER BY name ASC");
        while (result.next()) {
            System.out.println(result.getInt("id") + " " + result.getString("name") + " " + result.getString("surname") + " " + result.getString("age"));
        }

        String constant = "";
        for (int i = 0; i < numberOfAddingUser; i++) {
            constant += "(?,?,?)";
            if (i != numberOfAddingUser - 1) {
                constant += ", ";
            }
        }
        String sqlInsertUsers = "INSERT INTO driver(name, surname, age) VALUES " + constant;

        // Создаем PreparedStatement с подготовленным SQL-запросом
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertUsers);

        for (int i = 0; i < numberOfAddingUser; i++) {
            System.out.println("Введите имя, фамилию и возраст для пользователя " + (i + 1));
            String firstName = scanner.nextLine();
            String secondName = scanner.nextLine();
            String age = scanner.nextLine();

            preparedStatement.setString(i * 3 + 1, firstName);
            preparedStatement.setString(i * 3 + 2, secondName);
            preparedStatement.setString(i * 3 + 3, age);
        }

        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("Было добавлено " + affectedRows + " строк(и).");

        System.out.println("Юзеры имя которых длинее 5 символов:");
        String usersWhoseNamesIsLongerThan5Symbols = "SELECT * FROM driver WHERE LENGTH(name) > 5;";;

        ResultSet resultOver25 = statement.executeQuery(usersWhoseNamesIsLongerThan5Symbols);
        while (resultOver25.next()) {
            System.out.println(resultOver25.getString("name") + " " + resultOver25.getString("surname") + " " + resultOver25.getInt("age"));
        }

        preparedStatement.close();
        statement.close();
        connection.close();
    }
}
