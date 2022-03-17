package com.javarush.details;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private final static String NOT_TRUTH_NUMBER_MENU = "Не верный пункт меню, введите корректное число";
    private final static String NOT_TRUTH_NUMBER_CRYPT = "Не верное число криптографического ключа";

    //private final static String SOURCE_FILE = "c:/test/source.txt";
    private final static String SOURCE_FILE = "/home/bulat/test/source.txt";
    //private final static String DESTINATION_FILE = "c:/test/destination.txt";
    private final static String DESTINATION_FILE = "/home/bulat/test/destination.txt";

    public static void main(String[] args) {
/*        String ss = ":9ъыч-";
        //String ss = "aaa";
        System.out.println(ss.matches(".*[,-].*"));
        //System.out.println(ss.replaceAll("[.,!?:;«»]$",""));
        System.exit(-1);*/

        String str;

        Crypto.setKey(5);



        Scanner scanner = new Scanner(System.in);
        int n = 0;
        do {
           /* System.out.println("1 посмотреть пути к файлам для шифрования\\расшифровки\\криптографический ключ");
            System.out.println("2 Задать файл для шифрования");
            System.out.println("3 Задать файл для расшифровки");*/
            System.out.println("4 Задать криптографический ключ");
            System.out.println("5 Зашифровать по ключу");
            System.out.println("6 Расшифровать по ключу");
            System.out.println("7 Взлом (Brute Force)");
            System.out.println("8 Взлом (Статистический анализ)");
            System.out.println("0 Выход");

            System.out.print("Введите пункт меню: ");
            str = scanner.nextLine();
            System.out.println();

            try {
                n = Integer.parseInt(str);
                if (!(1 <= n || n <= 7)) {
                    System.out.println(NOT_TRUTH_NUMBER_MENU);
                } else {
                    switch (n) {
                        case 4:

                            try {
                                Crypto.setKey(Integer.parseInt(scanner.nextLine()));
                            } catch (NumberFormatException e) {
                                throw new Crypto.NumberFormatExceptionCrypt();
                            }

                            break;
                        case 5:
                            System.out.println("Введите ключ шифрования: ");
                            Crypto.cryptText(Crypto.getKey(), SOURCE_FILE, DESTINATION_FILE);
                            break;
                        case 6:
                            Crypto.cryptText(-Crypto.getKey(), DESTINATION_FILE, SOURCE_FILE);
                            break;
                        case 7:
                            Crypto.BruteForce(DESTINATION_FILE);
                            //Crypto.BruteForce(SOURCE_FILE);
                            break;
                    }

                }
            }  catch (Crypto.NumberFormatExceptionCrypt numberFormatExceptionCrypt) {
                System.out.println(NOT_TRUTH_NUMBER_CRYPT);
            }
            catch (NumberFormatException e) {
                System.out.println(NOT_TRUTH_NUMBER_MENU);
            }


        } while (n != 8);
    }

}


