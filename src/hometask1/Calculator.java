package hometask1;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Calculator {

    /*
     * Строковые константы, которые содержат вспомогательную информацию.
     */

    private static final String WELCOME_MESSAGE = "Добро пожаловать в Калькулятор.";
    private static final String INFO_MESSAGE =
            "Формат принимаемых выражений: последовательность чисел и операций, которые могут быть разделенны пробелами.\n" +
                    "Доступные операции: сложение (+), вычитание (-), умножение (*), деление(/).\n" +
                    "Пример валидного выражения: \"1 + 2 - 3 * 4 / 5\".";
    private static final String START_MESSAGE = "Введите выражение: ";
    private static final String WRONG_OPERAND_MESSAGE = "Введенное выражение содержит недопустимую операцию.";
    private static final String WRONG_DIGIT_MESSAGE = "Введенное выражение содержит недопустимый символ.";
    private static final String ANSWER_MESSAGE = "Ответ: %s%n";
    private static final String CONTINUE_MESSAGE = "Вы хотите продолжить вычисления?\n" +
            "Введите \"нет\", чтобы остановаить работу Калькулятора и любой другой символ чтобы продолжить: ";
    private static final String FINISH_MESSAGE = "Спасибо за использование Калькулятора.";

    private static final String OPERANDS = "[+*/-]";

    /*
     * Статический движок, который отвечает за обработку строки и вычисление результата.
     */

    private static final ScriptEngine scriptEngine;

    static {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
    }

    private BufferedReader bufferedReader;
    private String expression;
    private Object answer;
    private boolean isAlive;

    /*
    * Метод work() - основной метод в работе экземляра класса.
    * В нем осуществляется вызов вспомогательных методов и отработка логики Калькулятора.
    * Доступен из других классов пакета, например Main, для демонстрации работы Калькулятора.
    */

    void work() throws ScriptException, IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        isAlive = true;
        printWelcome();
        printInfo();
        while(isAlive) {
            printStart();
            setExpression();
            if (!isValidOperands(expression)) {
                System.out.println(WRONG_OPERAND_MESSAGE);
            } else if (!isValidNumbers(expression)) {
                System.out.println(WRONG_DIGIT_MESSAGE);
            } else {
                calculate(expression);
                printAnswer(answer);
            }
            askToContinue();
        }
    }

    /*
     * Методы setExpression(), calculate(String), finish() и askToContinue() отвечают за логику работы калькулятора.
     * Метод calculate(String) использует возможности класса javax.script.ScriptEngine для обработки строки и вычисления результата.
     */

    private void setExpression() throws IOException {
        expression = bufferedReader.readLine();
    }

    private void calculate(String expression) throws ScriptException {
        answer = scriptEngine.eval(expression);
    }

    private void finish() throws IOException {
        bufferedReader.close();
        isAlive = false;
        printFinish();
    }

    private void askToContinue() throws IOException {
        System.out.print(CONTINUE_MESSAGE);
        String toContinue = bufferedReader.readLine();
        if (toContinue.equals("нет")) {
            finish();
        }
    }

    /*
     * Методы isValidOperand(String) и isValidNumbers(String) отвечают за проверку выражения на валидность.
     */

    private boolean isValidOperands(String expression) {
        List<String> operands = Arrays.stream(expression.split("[0-9]"))
                .map(String::trim)
                .filter(operand -> !operand.isEmpty())
                .collect(Collectors.toList());
        for (String operand: operands) {
            if (!operand.matches(OPERANDS)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidNumbers(String expression) {
        try {
            Arrays.stream(expression.split(OPERANDS))
                    .map(String::trim)
                    .map(Integer::parseInt);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /*
    * Статические методы, которые выводят вспомогательную информацию на экран.
    */

    private static void printWelcome() {
        System.out.println(WELCOME_MESSAGE);
    }

    private static void printFinish() {
        System.out.println(FINISH_MESSAGE);
    }

    private static void printInfo() {
        System.out.println(INFO_MESSAGE);
    }

    private static void printStart() {
        System.out.print(START_MESSAGE);
    }

    private static void printAnswer(Object answer) {
        System.out.printf(ANSWER_MESSAGE, answer);
    }
}
