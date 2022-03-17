package com.javarush.details;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Crypto {

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

    private static int key;


    private static final List<Character> ALPHABET_LIST = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    private static final List<Character> PUNCTUATION_MARK = Arrays.asList('.',',','?',':',';','-');

    private static final List<String> ACTUAL_WORDS = Arrays.asList("и","в","не","на","я","быть","с","он","что","а","этот",
            "это","по","к","но","они","мы","она","как","то","и","у","в","за","от","так","или");

    private static Character crypt(char ch, int key) throws InvalidKeyCrypt {
        int indexOf = ALPHABET_LIST.indexOf(ch);
        if (indexOf != -1) {

            //Если key>словаря обработать эту ситуациию
            int delta = (indexOf + key) % ALPHABET_LIST.size();
//            int delta = (indexOf + key>ALPHABET_LIST.size()?key%ALPHABET_LIST.size():key) % ALPHABET_LIST.size();
            if (key==0) {
                throw new InvalidKeyCrypt();
            } else if (key<0 && delta<0) {
                delta = ALPHABET_LIST.size()+delta;
            }
            return ALPHABET_LIST.get(delta);
        }
        return ch;
    }

    private static String enCryptLine (String line, int key) throws InvalidKeyCrypt {
        char[] lineChar = line.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < lineChar.length; i++) {
            stringBuffer.append(crypt(lineChar[i],-key));
        }
        return stringBuffer.toString();
    }

    public static void cryptText (int p_key, String p_source_file, String p_distination_file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(p_source_file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(p_distination_file))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char[] strArr = line.toCharArray();
                for (int i = 0; i < strArr.length ; i++) {
                    bufferedWriter.append(crypt(strArr[i], p_key));
                }
                bufferedWriter.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyCrypt invalidKeyCrypt) {
            System.out.println("Задан не верный ключ шифрования: "+ p_key);
        }
    }

    public static void BruteForce (String p_filename) {
        String line;
        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> mapKey = new HashMap<>();
        // int j = 5;
        for (int i = 0, j=1; i < ALPHABET_LIST.size() ; i++, j++) {


            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(p_filename), Charset.defaultCharset())) {
                //bufferedReader.mark(1);
                while ((line = bufferedReader.readLine()) != null) {
                    //String newLine = line.replaceAll("[,.!?:«»]", "").toLowerCase();
                    String newLine2 = enCryptLine(line, j);

                    String[] words = newLine2.split(" ");

                    //wordSet.addAll(Arrays.asList(words));
                    for (String word: words) {
                        if (!word.matches(".*[.,-:?!].*")) {
                            wordSet.add(word);
                        }

                    }
                    if (wordSet.size()>9)
                        System.out.println(wordSet.toString());
                    //System.out.println(bufferedReader.markSupported());

                }

                mapKey.put(key, wordSet.size());
                System.out.printf("Ключ: %d, количество уникальных слов %d \n", j, wordSet.size());
                if (wordSet.size()>=18) {
                    System.out.println(wordSet.toString());
                }

                wordSet.clear();
                //bufferedReader.reset();
                //bufferedReader.mark(0);


            } catch (IOException | InvalidKeyCrypt e) {
                e.printStackTrace();
            }
        }


        //1. Удалить из слов все знаки препинания: ",.!?:«»"



        //2. Разбить текст на слова (по пробелу)
        //3. Привести текст к нижнему регистру
        //4. Подсчитать сумму повторяющихся слов, считаем слово если оно встретилось более 1 раза
        //   Хранить так: ключ, кол-во слов
        //5. Выбрать из Map по максимальному значению ключ, расшифровать текст. Записать в файл
    }


}
