package com.javarush.cryptoanalyser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private final static String NOT_TRUTH_NUMBER_MENU = "Не верный пункт меню, введите корректное число";
    private final static String NOT_TRUTH_NUMBER_CRYPT = "Не верное число криптографического ключа";




    public static void main(String[] args) {
   /*     String ss = ",";
        //String ss = "aaa";
        System.out.println(ss.matches(".*[,-].*"));
        //System.out.println(ss.replaceAll("[.,!?:;«»]$",""));
        System.exit(-1);*/

        String str;

        Crypto.setKey(15);



        Scanner scanner = new Scanner(System.in);
        int n = 0;
        do {
            System.out.println("1 посмотреть пути к файлам для шифрования\\расшифровки\\криптографический ключ");
            System.out.println("2 Задать файл для шифрования");
            System.out.println("3 Задать файл для расшифровки");
            System.out.println("4 Задать криптографический ключ");
            System.out.println("5 Зашифровать по ключу");
            System.out.println("6 Расшифровать по ключу");
            System.out.println("7 Взлом (Brute Force)");
            System.out.println("8 Взлом (Статистический анализ)");
            System.out.println("0 Выход");

            System.out.print("Введите пункт меню: ");
            //str = scanner.nextInt();
            n = scanner.nextInt();
            System.out.println();

            try {
                //n = Integer.parseInt(str);
                if (!(0 <= n || n <= 8)) {
                    System.out.println(NOT_TRUTH_NUMBER_MENU);
                } else {
                    switch (n) {
                        case 1:
                            Crypto.showParams();
                            break;
                        case 2:
                            Crypto.setSourceFileFromMenu();
                            break;
                        case 3:
                            Crypto.setDestinationFileFromMenu();
                            break;
//                            break;
                        case 4:
                            Crypto.setKeyFromMenu();

//                            try {
//                                Crypto.setKey(Integer.parseInt(scanner.nextLine()));
//                            } catch (NumberFormatException e) {
//                                throw new Crypto.NumberFormatExceptionCrypt();
//                            }

                            break;
                        case 5:
                            System.out.println("Введите ключ шифрования: ");
                            Crypto.cryptText(Crypto.getKey(), Crypto.SOURCE_FILE, Crypto.DESTINATION_FILE);
                            break;
                        case 6:
                            Crypto.cryptText(-Crypto.getKey(), Crypto.DESTINATION_FILE, Crypto.SOURCE_FILE);
                            break;
                        case 7:
                            Crypto.BruteForce(Crypto.DESTINATION_FILE);
                            //Crypto.BruteForce(Crypto.SOURCE_FILE);
                            break;
                    }

                }
            }  catch (Crypto.NumberFormatExceptionCrypt numberFormatExceptionCrypt) {
                System.out.println(NOT_TRUTH_NUMBER_CRYPT);
            }
            catch (NumberFormatException e) {
                System.out.println(NOT_TRUTH_NUMBER_MENU);
            }


        } while (n != 0);
    }

}


