package com.javarush.cryptoanalyser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Crypto {

    public final static String SOURCE_FILE = "c:/test/source.txt";
    //private final static String SOURCE_FILE = "/home/bulat/test/source.txt";
    public final static String DESTINATION_FILE = "c:/test/destination.txt";
    //private final static String DESTINATION_FILE = "/home/bulat/test/destination.txt";

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
    private static String sourceFile = SOURCE_FILE;
    private static String destinationFile = DESTINATION_FILE;


    private static final List<Character> ALPHABET_LIST = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    private static final List<Character> PUNCTUATION_MARK = Arrays.asList('.', ',', '?', ':', ';', '-');

    private static final List<String> ACTUAL_WORDS = Arrays.asList("и", "в", "не", "на", "я", "быть", "с", "он", "что", "а", "этот",
            "это", "по", "к", "но", "они", "мы", "она", "как", "то", "и", "у", "в", "за", "от", "так", "или");

    //Отображение текцщих параметров шифрования-дешифрования
    public static void showParams() {
        System.out.println("ПАРАМЕТРЫ");
        System.out.println("Файл для Шифрования: " + sourceFile);
        System.out.println("Файл для расшифровки: " + destinationFile);
        System.out.println("Файл для криптографический ключ:" + getKey());
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

    public static void setDestinationFileFromMenu() {
        //try
        Scanner scanner1 = new Scanner(System.in);//{
        System.out.print("Введите файл для шифрования: ");
        String filename = scanner1.nextLine();
        if (Files.notExists(Path.of(filename))) {
            System.out.println("Введен не существующий файл");
        } else {
            Crypto.setDestinationFile (filename);
        }
        //}
    }

    public static void setKeyFromMenu() {
        //try
        Scanner scanner1 = new Scanner(System.in);//{
        System.out.print("Введите криптографический ключ: ");
        int key = 0;
        try {
            key = scanner1.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
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


}
