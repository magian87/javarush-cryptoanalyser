package com.javarush.cryptoanalyser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Crypto {

    //public final static String SOURCE_FILE = "c:/test/source.txt";
    private final static String SOURCE_FILE = "/home/bulat/test/source.txt";
    //public final static String DESTINATION_FILE = "c:/test/destination.txt";
    private final static String DESTINATION_FILE = "/home/bulat/test/destination.txt";

    private final static String ADDITIONAL_FILE = "/home/bulat/test/add.txt";



    public static void setDestinationFile(String destinationFile) {
        Crypto.destinationFile = destinationFile;
    }

    static class InvalidKeyCrypt extends Exception {

    }

    static class NumberFormatExceptionCrypt extends Exception {

    }

    public static int getKey() {
        return key;
    }

    public static void setKey(int key) {
        Crypto.key = key;
    }

    public static void setSourceFile(String sourceFile) {
        Crypto.sourceFile = sourceFile;
    }

    private static int key;

    public static String getSourceFile() {
        return sourceFile;
    }

    public static String getDestinationFile() {
        return destinationFile;
    }

    private static String sourceFile = SOURCE_FILE;
    private static String destinationFile = DESTINATION_FILE;

    public static String getAdditionalFile() {
        return additionalFile;
    }

    public static void setAdditionalFile(String additionalFile) {
        Crypto.additionalFile = additionalFile;
    }

    private static String additionalFile = ADDITIONAL_FILE;


    private static final List<Character> ALPHABET_LIST = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    private static final char[] GLASN_BUKV = {'а', 'у', 'о', 'ы', 'э', 'я', 'ю', 'ё', 'и', 'е'};
    private static final char[] SOGL_BUKV = {'б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'ф', 'х', 'ц', 'ч', 'ш', 'щ'};


    private static final List<Character> PUNCTUATION_MARK = Arrays.asList('.', ',', '?', ':', ';', '-');

    private static final List<String> ACTUAL_WORDS = Arrays.asList("и", "в", "не", "на", "я", "быть", "с", "он", "что", "а", "этот",
            "это", "по", "к", "но", "они", "мы", "она", "как", "то", "и", "у", "в", "за", "от", "так", "или");

    //Отображение текцщих параметров шифрования-дешифрования
    public static void showParams() {
        System.out.println("ПАРАМЕТРЫ");
        System.out.println("Файл для Шифрования: " + sourceFile);
        System.out.println("Файл для расшифровки: " + destinationFile);
        System.out.println("Дополнительный файл: " + additionalFile);
        System.out.println("Криптографический ключ: " + getKey());
    }

    public static void setSourceFileFromMenu() {
        //try
        Scanner scanner1 = new Scanner(System.in);//{
        System.out.print("Введите файл для шифрования: ");
        String filename = scanner1.nextLine();
        if (Files.notExists(Path.of(filename))) {
            System.out.println("Введен не существующий файл");
        } else {
            Crypto.setSourceFile(filename);
        }
        //}
    }

    public static void setAdditionFileFromMenu() {
        //try
        Scanner scanner1 = new Scanner(System.in);//{
        System.out.print("Введите дополнительный файл этого же автора: ");
        String filename = scanner1.nextLine();
        if (Files.notExists(Path.of(filename))) {
            System.out.println("Введен не существующий файл");
        } else {
            Crypto.setAdditionalFile(filename);
        }
        //}
    }


    public static void setDestinationFileFromMenu() {
        //try
        Scanner scanner1 = new Scanner(System.in);//{
        System.out.print("Введите файл для расшифровки: ");
        String filename = scanner1.nextLine();
        if (Files.notExists(Path.of(filename))) {
            System.out.println("Введен не существующий файл");
        } else {
            Crypto.setDestinationFile(filename);
        }
        //}
    }

    public static void setKeyFromMenu() {
//        Почему в процедурах setSourceFileFromMenu, setDestinationFileFromMenu,setKeyFromMenu
//        нельзя использовать try -with-resources, появляются ошибки, как будто закрывается
//        основной Scanner из модуля Main. Вот StackTrace. Что делать и как исправить?
//        Exception in thread "main" java.util.NoSuchElementException
//        at java.base/java.util.Scanner.throwFor(Scanner.java:937)
//        at java.base/java.util.Scanner.next(Scanner.java:1594)
//        at java.base/java.util.Scanner.nextInt(Scanner.java:2258)
//        at java.base/java.util.Scanner.nextInt(Scanner.java:2212)
//        at com.javarush.cryptoanalyser.Main.main(Main.java:41)
        //try (
        Scanner scanner1 = new Scanner(System.in);//) {
        System.out.print("Введите криптографический ключ: ");
        int key = 0;
        try {
            key = scanner1.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("ОШИБКА: криптографический ключ должен быть числом");
        }
        Crypto.setKey(key);
        //}
    }


    private static Character crypt(char ch, int key) throws InvalidKeyCrypt {
        int indexOf = ALPHABET_LIST.indexOf(ch);
        if (indexOf != -1) {

            //Если key>словаря обработать эту ситуациию
            int delta = (indexOf + key) % ALPHABET_LIST.size();
//            int delta = (indexOf + key>ALPHABET_LIST.size()?key%ALPHABET_LIST.size():key) % ALPHABET_LIST.size();
            if (key == 0) {
                throw new InvalidKeyCrypt();
            } else if (key < 0 && delta < 0) {
                delta = ALPHABET_LIST.size() + delta;
            }
            return ALPHABET_LIST.get(delta);
        }
        return ch;
    }

    private static String enCryptLine(String line, int key) throws InvalidKeyCrypt {
        char[] lineChar = line.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < lineChar.length; i++) {
            stringBuffer.append(crypt(lineChar[i], -key));
        }
        return stringBuffer.toString();
    }

    public static void cryptText(int p_key, String p_source_file, String p_distination_file) {
        if (Files.notExists(Path.of(p_source_file))) {
            System.out.println("Исходный файл не существует: " + p_source_file);
            return;
        }
        if (Files.notExists(Path.of(p_distination_file))) {
            System.out.println("Результирующий файл не существует: " + p_source_file);
            return;
        }


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(p_source_file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(p_distination_file))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char[] strArr = line.toCharArray();
                for (int i = 0; i < strArr.length; i++) {
                    bufferedWriter.append(crypt(strArr[i], p_key));
                }
                bufferedWriter.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyCrypt invalidKeyCrypt) {
            System.out.println("Задан не верный ключ шифрования: " + p_key);
        }
    }

    public static void BruteForce(String p_filename) {
        if (Files.notExists(Path.of(p_filename))) {
            System.out.println("Передан не существующий файл для расшифровки: " + p_filename);
            return;
        }
        String line;
        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> mapKey = new HashMap<>();
        // int j = 5;
        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {


            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(p_filename), Charset.defaultCharset())) {
                //bufferedReader.mark(1);
                while ((line = bufferedReader.readLine()) != null) {
                    //String newLine = line.replaceAll("[,.!?:«»]", "").toLowerCase();
                    String newLine2 = enCryptLine(line, j).toLowerCase(Locale.ROOT);

                    String[] words = newLine2.split(" ");

                    //wordSet.addAll(Arrays.asList(words));
                    for (String word : words) {
                        if (!word.matches(".*[.,-:?!].*")) { //Убираю из найденных слов те, в которых есть знаки препинания
                            wordSet.add(word);
                        }

                    }

                    //System.out.println(bufferedReader.markSupported());

                }

                mapKey.put(j, wordSet.size());
                System.out.printf("Ключ: %d, количество уникальных слов %d \n", j, wordSet.size());

                wordSet.clear();
                //bufferedReader.reset();
                //bufferedReader.mark(0);


            } catch (IOException | InvalidKeyCrypt e) {
                e.printStackTrace();
            }
        }

        int mapMaxValue = 0;
        int map_Key = 0;


        for (Map.Entry<Integer, Integer> kvv : mapKey.entrySet()) {
            if (kvv.getValue() > mapMaxValue) {
                mapMaxValue = kvv.getValue();
                map_Key = kvv.getKey();
            }
        }
        System.out.printf("key=%d, cnt words = %d \n", map_Key, mapMaxValue);

        Crypto.cryptText(-map_Key, DESTINATION_FILE, SOURCE_FILE);

        //1. Удалить из слов все знаки препинания: ",.!?:«»"


        //2. Разбить текст на слова (по пробелу)
        //3. Привести текст к нижнему регистру
        //4. Подсчитать сумму повторяющихся слов, считаем слово если оно встретилось более 1 раза
        //   Хранить так: ключ, кол-во слов
        //5. Выбрать из Map по максимальному значению ключ, расшифровать текст. Записать в файл
    }

    private static double relationshipLetter (String file, int offset) {
        if (Files.notExists(Path.of(file))) {
            System.out.println("Файл не существует " + file);
            return 0;
        }
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(file), Charset.defaultCharset())) {
            int cntGl = 0;
            int cntSogl = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String newLine2;
                if (offset!=0) {
                    newLine2 = enCryptLine(line, -offset);
                } else {
                    newLine2 = line;
                }
                char[] arr = newLine2.toLowerCase().toCharArray();

                for (int i = 0; i < arr.length; i++) {
                    for (int k = 0; k < SOGL_BUKV.length; k++) {
                        if (arr[i] == SOGL_BUKV[k]) {
                            cntSogl++;
                            break;
                        }
                    }

                    for (int j = 0; j < GLASN_BUKV.length; j++) {
                        if (arr[i] == GLASN_BUKV[j]) {
                            cntGl++;
                            break;
                        }
                    }
                }
            }
            float t = (float) cntGl / cntSogl;
            return t*t;
            //return (float) cntGl / cntSogl;

            //System.out.printf("Ключ %d, количество гласных букв: %d, количество согласных букв: %d, соотношение гласных и согласных: %f \n", m, cntGl, cntSogl, (float) cntGl / cntSogl);
        }
        catch (InvalidKeyCrypt invalidKeyCrypt) {
            invalidKeyCrypt.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Float.MAX_VALUE;
    }

    public static void staticAnaliz() {

/*
    1.Из не зашифрованного текста того же автора получить соотношение гласных и согласных букв
    2.Получить квадрат этого отношения
    3. отклонением назначить Float.Max
    2.В цикле по количеству элементов словаря
    2.1 Для каждого ключа найти соотношение гласнных букв к согласным, получить квадрат найденного значения
    2.2 Подсчитать отклонение для текущего элемента по модулю (квадрат исходного текста - квадрат найденного значения)
    Если отклонение меньше минимума, то присвоить новое значение минимума.

*/
        if (Files.notExists(Path.of(Crypto.additionalFile))) {
            System.out.println("Передан не существующий файл дополнительный файл: " + Crypto.additionalFile);
            return;
        }
        if (Files.notExists(Path.of(Crypto.getDestinationFile()))) {
            System.out.println("Передан не существующий файл для расшифровки: " + Crypto.getDestinationFile());
            return;
        }

        double minDeviation = 10; //Double.MAX_VALUE думаю избыточно
        double ishDeviation = Crypto.relationshipLetter(Crypto.additionalFile,0);
        double curDeviation = Crypto.relationshipLetter(Crypto.getDestinationFile(),0);

        System.out.printf("ish = %f, cur = %f", ishDeviation, curDeviation);
        double otkl = Math.abs(ishDeviation-curDeviation);
        //System.out.println(otkl);

        int key=0;

        for (int i = 0, j=1; i < ALPHABET_LIST.size(); i++, j++) {
            double curDeviation2 = Crypto.relationshipLetter(Crypto.getDestinationFile(),j);
            double delta = Math.abs(curDeviation2-otkl);
            //if (Math.abs((ishDeviation*ishDeviation)-(curDeviation*curDeviation)) <= 0.25)
            if (delta<0.15)
            System.out.printf("j=%d, delta=%f\n", j, delta);
            //System.out.printf("key =%d, ishD=%f currentD= %f, delta=%f \n", j, ishDeviation, curDeviation, Math.abs((ishDeviation*ishDeviation)-(curDeviation*curDeviation)));
        }
        System.out.println(key);


    }


}
