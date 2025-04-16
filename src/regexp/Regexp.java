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
        System.out.println("������ � ��������� ������");
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

                if(passwordText.length() < 8) isValid = false;   //�������� �� ����� ������
                
                for(int k = 0; k < passwordText.length(); k++){
                    char currentChar = passwordText.charAt(k);

                    if(k != 0 && currentChar == passwordText.charAt(k-1)){      //�������� �� ��� ���������� ������� ������
                        isValid = false;
                        hasRepeat = true;
                    }

                    if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z')    //�������� �� ��������� �����
                            || (currentChar >= '0' && currentChar <= '9')                                            // �������� �� �����
                            || "^$%@#&*!?".indexOf(currentChar) != -1)){                                             // �������� �� �����������
                        isValid = false;
                        hasUnacceptableChar = true;
                    }
                    else{
                        if(Character.isLowerCase(currentChar)) countLower++;      //������� ����� ��������� ��������� ����
                        else if(Character.isUpperCase(currentChar)) countUpper++; //������� ����� ������� ��������� ����
                        else if(Character.isDigit(currentChar)) countDigit++;     //������� ����� ���� � ������
                        else if("^$%@#&*!?".indexOf(currentChar) != -1) specialChars.add(currentChar);  //��������� � ������ ����������� �������
                    }
                }
                
                if(!(countLower > 0 && countUpper > 0 && countDigit > 0 && specialChars.size() > 1)) isValid = false;
                
                System.out.println(passwordText);
                if(!isValid){
                    String error = "������ ������ ���� �� 8 ��������, ��� ������������� �������� ������, �������� ��������� � �������� ��������� �����, �����, ��� ������ ����������� (^$%@#&*!?).";
                    
                    if(hasUnacceptableChar) error = "������ ������ ��������� ������ ��������� �����, ����� � ��������� ����������� (^$%@#&*!?).";
                    else{
                        if (passwordText.length() >= 8) error = error.replace(" �� 8 ��������,", "");
                                if (!hasRepeat) error = error.replace(" ��� ������������� �������� ������,", "");
                                if (countUpper > 0 && countLower == 0) error = error.replace(" ��������� �", "");
                                if (countLower > 0 && countUpper == 0) error = error.replace(" � ��������", "");
                                if (countUpper > 0 && countLower > 0) error = error.replace(" ��������� � �������� ��������� �����,", "");                                
                                if (countDigit > 0) error = error.replace(", �����", "").replace(" �����,", "");
                                if (specialChars.size() > 1) error = error.replace(", ��� ������ ����������� (^$%@#&*!?)", "").replace(" ��� ������ ����������� (^$%@#&*!?)", "");
                            }
                    
                    if(error.endsWith(", ��������.")){
                        error = error.replace(", ��������", "");
                    }
                    error = error.replace("���� ��������", "��������");
                    
                    System.out.println(RED + error + RESET);
                }
                else System.out.println(GREEN + "������ ���������" + RESET);
                
                System.out.println("----------------------------------");
            }
        }
        catch(IOException | ParseException ex){}

        returnToMain();
    }

    public static void checkWebColors(){
        System.out.println("������ � ��������� web-�����");
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
                    String error = "������ ������ �������.\n";
                    
                    if(textOfColor.startsWith("rgb")) error += "rgb(r, g, b), ��� \"r, g, b\" �������� ����� �������: ���� ������ ���������� �� 0 �� 255, ���� ����������� ���������� �� 0% �� 100%, �������������� ����� �������.";
                    else if(textOfColor.startsWith("#")) error += "Hex-��� ����� (#rrggbb) � ����������������� ������������� RGB, ������������ � ������� #, ��� ������ ���� ���� �������� �� �������, ������ � ����� ������. ����� ����������� ����������� �����: #rgb.";
                    else if(textOfColor.startsWith("hsl")) error += "HSL (���� � ������� hsl(h, s, l)) ������������ ������� ������� �� RGB. ��� (h) � ����� ����� �� 0 �� 360, ������������ (s) � �������� (l) � ���������� �������� �� 0% �� 100%.";
                    else error += "������ �� ������������� �� ������ �� ���������� ��������� �������� web-�����(rgb(r, g, b), #rrggbb, hsl(h, s, l)).";
                    
                    System.out.println(RED + error + RESET);
                }
                else System.out.println(GREEN + "Web-���� ���������" + RESET);
                
                System.out.println("----------------------------------");
            }
        }
        catch(IOException | ParseException ex){}

        returnToMain();
    }

    public static void tokenMathsExpression(){
        System.out.println("������ � ������������ ��������������� ���������");
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
        System.out.println("�������� ������������ ����");
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
                        error = "���������� ���������� ������ ����. ���������, ��� �������� ������ �������� ���������� ����.";
                    }
                }
                    
                System.out.println(textOfDate);
                if(!isValid) System.out.println(RED + error + RESET);
                else System.out.println(GREEN + "���� ���������" + RESET);
                
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
        if (year <= 0) return "��� ������ ���� ������ 0.";

        if (month < 1 || month > 12) return "����� ������ ���� � ��������� �� 1 �� 12.";

        int[] days = new int[]{31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int maxDays = days[month - 1];

        if (day < 1 || day > maxDays) return "���� ������ ���� � ��������� �� 1 �� " + maxDays + " ��� ������ " + month + ".";

        return ""; // ���� ������ ���, ���������� ������ ������
    }

    
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    
    public static String validateDigitDate(String textOfDate, String separator) {
        if (separator.equals(".")) separator = "\\.";
        String[] parts = textOfDate.split(separator);

        if (parts.length != 3 || !isNumeric(parts[0]) || !isNumeric(parts[1]) || !isNumeric(parts[2])) {
            separator = separator.replace("\\", "");
            return "��������� ������ '��" + separator + "��" + separator + "����' ��� '����" + separator + "��" + separator + "��'.";
        }

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        String error = isValidDate(day, month, year);
        if (error.isEmpty()) return ""; // ������ ������� ������

        error = isValidDate(year, month, day);
        if (error.isEmpty()) return ""; // ������ ������� ������

        // ���� ��� �������� �������, ���������� ������ ������
        return "������������ �������� ����. " + error;
    }

    public static String validateRussianDate(String textOfDate) {
        List<String> rusMonths = Arrays.asList("������", "�������", "�����", "������", "���", "����", "����", "�������", "��������", "�������", "������", "�������");

        String[] parts = textOfDate.split(" ");
        if (parts.length != 3 || !isNumeric(parts[0]) || !isNumeric(parts[2])) {
            return "��������� ������ '���� �����_��_������ ���' (��������, 20 ������ 1806).";
        }

        int day = Integer.parseInt(parts[0]);
        int month = rusMonths.indexOf(parts[1].toLowerCase()) + 1;
        int year = Integer.parseInt(parts[2]);

        if (month == 0) return "�������� �������� ������: " + parts[1];

        String error = isValidDate(day, month, year);
        if (!error.isEmpty()) return "������������ ������� ����. " + error;

        return ""; // ���� ������ ���
    }

    public static String validateEnglishDate(String textOfDate) {
        List<String> engMonths = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", 
                                              "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

        String[] parts = textOfDate.split(",");
        if (parts.length != 2) {
            return "��������� ������ 'January 14, 2023' ��� '2023, January 14'.";
        }

        int day = -1, month = -1, year = -1;
        String[] firstPart = parts[0].trim().split(" ");
        String[] secondPart = parts[1].trim().split(" ");

        if (firstPart.length == 2 && isNumeric(firstPart[1]) && isNumeric(parts[1].trim())) {
            day = Integer.parseInt(firstPart[1]);
            month = engMonths.indexOf(firstPart[0]) % 12 + 1;
            if (month == 0) return "�������� �������� ������: " + firstPart[0];
            year = Integer.parseInt(parts[1].trim());
        } else if (secondPart.length == 2 && isNumeric(secondPart[1]) && isNumeric(parts[0].trim())) {
            day = Integer.parseInt(secondPart[1]);
            month = engMonths.indexOf(secondPart[0]) % 12 + 1;
            if (month == 0) return "�������� �������� ������: " + secondPart[0];
            year = Integer.parseInt(parts[0].trim());
        } else return "��������� ������ 'January 14, 2023' ��� '2023, January 14'.";

        String error = isValidDate(day, month, year);
        if (!error.isEmpty()) return "������������ ���������� ����. " + error;

        return ""; // ���� ������ ���
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
        System.out.println("��������� � ������� ����?(y/n)");
        String response = input.nextLine().trim().toLowerCase();
        if(!"y".equals(response)) System.exit(0);
    }

    public static void main(String[] args) {
        while(true){
            System.out.println("\n�������� ������:");
            System.out.println("1 - ������");
            System.out.println("2 - ������������ web-�����");
            System.out.println("3 - ����������� ��������������� ���������");
            System.out.println("4 - ������������ ����");
            System.out.println("5 - ����� �� ���������");

            String choise = input.nextLine().trim();

            switch(choise){
                    case "1" -> checkPasswords();
                    case "2" -> checkWebColors();
                    case "3" -> tokenMathsExpression();
                    case "4" -> checkDate();
                    case "5" -> System.exit(0);
                    default -> {
                        System.out.println("\n�������� ����");
                        break;
                    }
            }
        }
    }
}