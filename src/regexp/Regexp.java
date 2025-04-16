package regexp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Regexp {
    public static Scanner input = new Scanner(System.in);
    static String RED = "\u001B[31m";
    static String RESET = "\u001B[0m";
    static String GREEN = "\u001B[32m";

    public static void checkPasswords(){
        System.out.println("Задача с проверкой пароля");
        String fileName = "passwordsTests.json";

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            JSONArray passwordsArray = (JSONArray) ((JSONObject) new JSONParser().parse(reader)).get("passwords");

            for(int i = 0; i < passwordsArray.size(); i++){
                JSONObject password = (JSONObject) passwordsArray.get(i);
                String passwordText = password.get("text").toString();
                
                boolean isValid = true;
                boolean hasRepeat = false, hasUnacceptableChar = false;
                int countLower = 0, countUpper = 0, countDigit = 0;
                Set<Character> specialChars = new HashSet<>();

                if(passwordText.length() < 8) isValid = false;   //проверка на длину пароля
                
                for(int k = 0; k < passwordText.length(); k++){
                    char currentChar = passwordText.charAt(k);

                    if(k != 0 && currentChar == passwordText.charAt(k-1)){      //проверка на два одинаковых символа подряд
                        isValid = false;
                        hasRepeat = true;
                    }

                    if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z')    //проверка на латинские буквы
                            || (currentChar >= '0' && currentChar <= '9')                                            // проверка на цифры
                            || "^$%@#&*!?".indexOf(currentChar) != -1)){                                             // проверка на спецсимволы
                        isValid = false;
                        hasUnacceptableChar = true;
                    }
                    else{
                        if(Character.isLowerCase(currentChar)) countLower++;      //считаем число маленьких латинских букв
                        else if(Character.isUpperCase(currentChar)) countUpper++; //считаем число больших латинских букв
                        else if(Character.isDigit(currentChar)) countDigit++;     //считаем число цифр в строке
                        else if("^$%@#&*!?".indexOf(currentChar) != -1) specialChars.add(currentChar);  //добавляем в массив встреченные символы
                    }
                }
                
                if(!(countLower > 0 && countUpper > 0 && countDigit > 0 && specialChars.size() > 1)) isValid = false;
                
                System.out.println(passwordText);
                if(!isValid){
                    String error = "Пароль должен быть от 8 символов, без повторяющихся символов подряд, включать заглавные и строчные латинские буквы, цифры, два разных спецсимвола (^$%@#&*!?).";
                    
                    if(hasUnacceptableChar) error = "Пароль должен содержать только латинские буквы, цифры и указанные спецсимволы (^$%@#&*!?).";
                    else{
                        if (passwordText.length() >= 8) error = error.replace(" от 8 символов,", "");
                                if (!hasRepeat) error = error.replace(" без повторяющихся символов подряд,", "");
                                if (countUpper > 0 && countLower == 0) error = error.replace(" заглавные и", "");
                                if (countLower > 0 && countUpper == 0) error = error.replace(" и строчные", "");
                                if (countUpper > 0 && countLower > 0) error = error.replace(" заглавные и строчные латинские буквы,", "");                                
                                if (countDigit > 0) error = error.replace(", цифры", "").replace(" цифры,", "");
                                if (specialChars.size() > 1) error = error.replace(", два разных спецсимвола (^$%@#&*!?)", "").replace(" два разных спецсимвола (^$%@#&*!?)", "");
                            }
                    
                    if(error.endsWith(", включать.")){
                        error = error.replace(", включать", "");
                    }
                    error = error.replace("быть включать", "включать");
                    
                    System.out.println(RED + error + RESET);
                }
                else System.out.println(GREEN + "Пароль корректен" + RESET);
                
                System.out.println("----------------------------------");
            }
        }
        catch(IOException | ParseException ex){}

        returnToMain();
    }

    public static void checkWebColors(){
        System.out.println("Задача с проверкой web-цвета");
        String fileName = "webColorsTests.json";
        
        String regex = ""
                        + "(rgb\\((((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d),\\s*){2}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)|((100|[1-9]\\d|\\d)%,\\s*){2}(100|[1-9]\\d|\\d)%)\\)"
                        + "|#([a-fA-F\\d]{3}|[a-fA-F\\d]{6})\\b"
                        + "|hsl\\((360|3[0-5]\\d|[1-2]\\d\\d|[1-9]\\d|\\d),\\s*(100|[1-9]\\d|\\d)%,\\s*(100|[1-9]\\d|\\d)%\\))";
        Pattern pattern = Pattern.compile(regex);

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            JSONArray colorsArray = (JSONArray) ((JSONObject) new JSONParser().parse(reader)).get("web-colors");

            for(int i = 0; i < colorsArray.size(); i++){
                JSONObject color = (JSONObject) colorsArray.get(i);
                String textOfColor = color.get("color").toString().trim();              
                Matcher matcher = pattern.matcher(textOfColor);

                boolean isValid =  matcher.matches();

                System.out.println(textOfColor);
                if(!isValid){
                    String error = "Формат указан неверно.\n";
                    
                    if(textOfColor.startsWith("rgb")) error += "rgb(r, g, b), где \"r, g, b\" задается тремя числами: либо целыми значениями от 0 до 255, либо процентными значениями от 0% до 100%, перечисленными через запятую.";
                    else if(textOfColor.startsWith("#")) error += "Hex-код цвета (#rrggbb) — шестнадцатеричное представление RGB, начинающееся с символа #, где каждая пара цифр отвечает за красный, зелёный и синий каналы. Также допускается сокращённая форма: #rgb.";
                    else if(textOfColor.startsWith("hsl")) error += "HSL (цвет в формате hsl(h, s, l)) записывается похожим образом на RGB. Тон (h) — целое число от 0 до 360, насыщенность (s) и светлота (l) — процентные значения от 0% до 100%.";
                    else error += "Строка не соответствует ни одному из допустимых вариантов форматов web-цвета(rgb(r, g, b), #rrggbb, hsl(h, s, l)).";
                    
                    System.out.println(RED + error + RESET);
                }
                else System.out.println(GREEN + "Web-цвет корректен" + RESET);
                
                System.out.println("----------------------------------");
            }
        }
        catch(IOException | ParseException ex){}

        returnToMain();
    }

    public static void tokenMathsExpression(){
        System.out.println("Задача с токенизацией математического выражения");
        String fileName = "tokenMathTests.json";
        
        String numberRegex = "\\b(\\d+(\\.\\d+)?)\\b";
        String constantRegex = "\\b(pi|e|sqrt2|ln2|ln10)\\b";
        String operatorRegex = "[\\^\\*\\/\\-\\+]";
        String leftRegex = "\\(";
        String rightRegex = "\\)";
        String functionRegex = "\\b(sin|cos|tg|ctg|tan|cot|sinh|cosh|th|cth|tanh|coth|ln|lg|log|exp|sqrt|cbrt|abs|sign)\\b";
        String variableRegex = "\\b[a-zA-z_]\\w*\\b";
        String regex = "(" + String.join("|", numberRegex, constantRegex, operatorRegex, leftRegex, rightRegex, functionRegex, variableRegex) + ")";
        Pattern pattern = Pattern.compile(regex);

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            JSONArray expressionArray = (JSONArray) ((JSONObject) new JSONParser().parse(reader)).get("math-expression");

            for(int i = 0; i < expressionArray.size(); i++){
                JSONObject expression = (JSONObject) expressionArray.get(i);
                String textOfExpression = expression.get("text").toString();                
                Matcher matcher = pattern.matcher(textOfExpression);

                System.out.println(textOfExpression);
                while(matcher.find()){
                    String token = matcher.group();
                    String type = "";
                    
                    int start = matcher.start();
                    int end = matcher.end();

                    if(token.matches(numberRegex)) type = "number";
                    else if(token.matches(constantRegex)) type = "constant";
                    else if(token.matches(operatorRegex)) type = "operator";                    
                    else if(token.matches(leftRegex)) type = "left_parenthesis";                    
                    else if(token.matches(rightRegex)) type = "right_parenthesis";                    
                    else if(token.matches(functionRegex)) type = "function";                    
                    else if(token.matches(variableRegex)) type = "variable";                    

                    System.out.println("{\"type\": \"" + type + "\", \"span\": [" + start + ", " + end + "]},");
                }
                
                System.out.println("----------------------------------");
            }
        }
        catch(IOException | ParseException ex){}

        returnToMain();
    }

    public static void checkDate(){
        System.out.println("Проверка корректности даты");
        String fileName = "datesTests.json";

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            JSONArray datesArray = (JSONArray) ((JSONObject) new JSONParser().parse(reader)).get("dates");

            for(int i = 0; i < datesArray.size(); i++){
                JSONObject date = (JSONObject) datesArray.get(i);
                String textOfDate = date.get("text").toString().trim();

                boolean isValid = true;
                String error = "";
                String format = detectFormat(textOfDate);

                switch (format) {
                    case "digit" -> {
                        String separator = detectSeparator(textOfDate);
                        error = validateDigitDate(textOfDate, separator);
                        isValid = error.isEmpty();
                    }
                    case "russian" -> {
                        error = validateRussianDate(textOfDate);
                        isValid = error.isEmpty();
                    }
                    case "english" -> {
                        error = validateEnglishDate(textOfDate);
                        isValid = error.isEmpty();
                    }
                    default -> {
                        isValid = false;
                        error = "Невозможно определить формат даты. Убедитесь, что введённая строка содержит корректную дату.";
                    }
                }
                    
                System.out.println(textOfDate);
                if(!isValid) System.out.println(RED + error + RESET);
                else System.out.println(GREEN + "Дата корректна" + RESET);
                
                System.out.println("----------------------------------");
            }
        }
        catch(IOException | ParseException ex){}

        returnToMain();
    }
    
    public static String detectFormat(String text) {
        boolean hasDigits = false, hasRussian = false, hasEnglish = false;

        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) hasDigits = true;
            else if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CYRILLIC) hasRussian = true;
            else if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.BASIC_LATIN && Character.isLetter(c)) hasEnglish = true;
        }

        if (hasRussian) return "russian";
        if (hasEnglish) return "english";
        if (hasDigits && !hasRussian && !hasEnglish) return "digit";

        return "unknown";
    }
    
    public static String detectSeparator(String text) {
        for (String sep : new String[]{".", "/", "-", " "}) {
            if (text.contains(sep)) return sep;
        }
        return "";
    }    

    public static String isValidDate(int day, int month, int year) {
        if (year <= 0) return "Год должен быть больше 0.";

        if (month < 1 || month > 12) return "Месяц должен быть в диапазоне от 1 до 12.";

        int[] days = new int[]{31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int maxDays = days[month - 1];

        if (day < 1 || day > maxDays) return "День должен быть в диапазоне от 1 до " + maxDays + " для месяца " + month + ".";

        return ""; // Если ошибок нет, возвращаем пустую строку
    }

    
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    
    public static String validateDigitDate(String textOfDate, String separator) {
        if (separator.equals(".")) separator = "\\.";
        String[] parts = textOfDate.split(separator);

        if (parts.length != 3 || !isNumeric(parts[0]) || !isNumeric(parts[1]) || !isNumeric(parts[2])) {
            separator = separator.replace("\\", "");
            return "Ожидается формат 'дд" + separator + "мм" + separator + "гггг' или 'гггг" + separator + "мм" + separator + "дд'.";
        }

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        String error = isValidDate(day, month, year);
        if (error.isEmpty()) return ""; // Первый вариант верный

        error = isValidDate(year, month, day);
        if (error.isEmpty()) return ""; // Второй вариант верный

        // Если оба варианта неверны, возвращаем первую ошибку
        return "Некорректная цифровая дата. " + error;
    }

    public static String validateRussianDate(String textOfDate) {
        List<String> rusMonths = Arrays.asList("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря");

        String[] parts = textOfDate.split(" ");
        if (parts.length != 3 || !isNumeric(parts[0]) || !isNumeric(parts[2])) {
            return "Ожидается формат 'день месяц_по_русски год' (например, 20 января 1806).";
        }

        int day = Integer.parseInt(parts[0]);
        int month = rusMonths.indexOf(parts[1].toLowerCase()) + 1;
        int year = Integer.parseInt(parts[2]);

        if (month == 0) return "Неверное название месяца: " + parts[1];

        String error = isValidDate(day, month, year);
        if (!error.isEmpty()) return "Некорректная русская дата. " + error;

        return ""; // Если ошибок нет
    }

    public static String validateEnglishDate(String textOfDate) {
        List<String> engMonths = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", 
                                              "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

        String[] parts = textOfDate.split(",");
        if (parts.length != 2) {
            return "Ожидается формат 'January 14, 2023' или '2023, January 14'.";
        }

        int day = -1, month = -1, year = -1;
        String[] firstPart = parts[0].trim().split(" ");
        String[] secondPart = parts[1].trim().split(" ");

        if (firstPart.length == 2 && isNumeric(firstPart[1]) && isNumeric(parts[1].trim())) {
            day = Integer.parseInt(firstPart[1]);
            month = engMonths.indexOf(firstPart[0]) % 12 + 1;
            if (month == 0) return "Неверное название месяца: " + firstPart[0];
            year = Integer.parseInt(parts[1].trim());
        } else if (secondPart.length == 2 && isNumeric(secondPart[1]) && isNumeric(parts[0].trim())) {
            day = Integer.parseInt(secondPart[1]);
            month = engMonths.indexOf(secondPart[0]) % 12 + 1;
            if (month == 0) return "Неверное название месяца: " + secondPart[0];
            year = Integer.parseInt(parts[0].trim());
        } else return "Ожидается формат 'January 14, 2023' или '2023, January 14'.";

        String error = isValidDate(day, month, year);
        if (!error.isEmpty()) return "Некорректная английская дата. " + error;

        return ""; // Если ошибок нет
    }


    public static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void returnToMain(){
        System.out.println("Вернуться в главное меню?(y/n)");
        String response = input.nextLine().trim().toLowerCase();
        if(!"y".equals(response)) System.exit(0);
    }

    public static void main(String[] args) {
        while(true){
            System.out.println("\nВыберите задачу:");
            System.out.println("1 - пароли");
            System.out.println("2 - корректность web-цвета");
            System.out.println("3 - токенизация математического выражения");
            System.out.println("4 - корректность даты");
            System.out.println("5 - выход из программы");

            String choise = input.nextLine().trim();

            switch(choise){
                    case "1" -> checkPasswords();
                    case "2" -> checkWebColors();
                    case "3" -> tokenMathsExpression();
                    case "4" -> checkDate();
                    case "5" -> System.exit(0);
                    default -> {
                        System.out.println("\nНеверный ввод");
                        break;
                    }
            }
        }
    }
}