import java.io.*;
import java.util.*;

public class Homework3 {

    public static List<String[]> readFile(File file) {
        List<String[]> lst = new ArrayList<>();
        try (FileReader fr = new FileReader(file);BufferedReader bf = new BufferedReader(fr)){
            String line;
            while ((line = bf.readLine()) != null) {
                lst.add(line.split(" "));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + file.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return lst;
    }

    public static void writeFile(List<String[]> lst, File file, User user) {
        try (FileWriter fw = new FileWriter(file);BufferedWriter bf = new BufferedWriter(fw)){
            for (String[] item : lst) {
                bf.write(Arrays.toString(item)
                        .replace('[', ' ')
                        .replace(']', ' ')
                        .replaceAll(", ", " ")
                        .trim());
                bf.newLine();
            }
            bf.write(user.toString());
            bf.newLine();
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + file.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean isNumeric(String val) {
        try {
            Long.parseLong(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String num;
        do {
            System.out.println("Введите количество пользователей (в числовом формате):");
            num = scan.nextLine();
        } while (!isNumeric(num));

        int numberOfUsers = Integer.parseInt(num);
        List <User> usersList = new ArrayList<>();

        int count = 0;
        while (count < numberOfUsers) {
            Integer[] requestVariants = {1, 2, 3, 4};
            List<Integer> requestOrder = Arrays.asList(requestVariants);
            Collections.shuffle(requestOrder);

            User user = new User();

            for (int variant : requestOrder) {
                switch (variant) {
                    case 1 -> {
                        System.out.println("Введите ФИО " + (count + 1) + "-го пользователя (через пробел):");
                        Scanner userInfoScan = new Scanner(System.in);
                        String fio = userInfoScan.nextLine();
                        String[] userFio = fio.split(" ");
                        if (userFio.length < 3) {
                            throw new RuntimeException("Введено недостаточно данных");
                        } else {
                            if (userFio.length > 3) {
                                throw new RuntimeException("Введено избыточное количество данных");
                            }
                        }
                        user.setSurname(userFio[0]);
                        user.setName(userFio[1]);
                        user.setPatronymic(userFio[2]);
                    }
                    case 2 -> {
                        System.out.println("Введите дату рождения " + (count + 1) + "-го пользователя (в формате dd.mm.yyyy):");
                        Scanner userInfoScan = new Scanner(System.in);
                        String birthFullDate = userInfoScan.nextLine();
                        String[] userBirthdate = birthFullDate.split("\\.");
                        if (userBirthdate.length != 3) {
                            throw new RuntimeException("Некорректный формат даты");
                        } else {
                            if (!isNumeric(userBirthdate[0]) || !isNumeric(userBirthdate[1]) || !isNumeric(userBirthdate[2])) {
                                throw new RuntimeException("Дата должна быть в числовом формате");
                            } else {
                                if (Integer.parseInt(userBirthdate[0]) < 1 || Integer.parseInt(userBirthdate[0]) > 31
                                        || Integer.parseInt(userBirthdate[1]) < 1 || Integer.parseInt(userBirthdate[1]) > 12
                                        || Integer.parseInt(userBirthdate[2]) < 1900 || Integer.parseInt(userBirthdate[2]) > 2023) {
                                    throw new RuntimeException("Выход за пределы возможных значений в дате");
                                }
                            }
                        }
                        user.setBirthDate(userBirthdate[0]);
                        user.setBirthMonth(userBirthdate[1]);
                        user.setBirthYear(userBirthdate[2]);
                    }
                    case 3 -> {
                        System.out.println("Введите номер телефона " + (count + 1) + "-го пользователя:");
                        boolean phoneNumberIsNumeric;
                        do {
                            phoneNumberIsNumeric = true;
                            try {
                                Scanner userInfoScan = new Scanner(System.in);
                                Long phoneNumber = userInfoScan.nextLong();
                                user.setPhoneNumber(phoneNumber);
                            } catch (InputMismatchException e) {
                                System.out.println("Номер телефона должен быть в числовом формате, повторите ввод");
                                phoneNumberIsNumeric = false;
                            }
                        } while (!phoneNumberIsNumeric);
                    }
                    default -> {
                        System.out.println("Введите пол " + (count + 1) + "-го пользователя (f или m):");
                        boolean genderIsCorrect;
                        do {
                            genderIsCorrect = true;
                            Scanner userInfoScan = new Scanner(System.in);
                            String gender = userInfoScan.nextLine();
                            if (!gender.equals("f") && !gender.equals("m")) {
                                System.out.println("Пол указан некорректно, повторите ввод");
                                genderIsCorrect = false;
                            } else {
                                user.setGender(gender);
                            }
                        } while (!genderIsCorrect);
                    }
                }
            }
            usersList.add(user);
            count++;
        }

        for (User nextUser : usersList) {
            File file = new File(nextUser.getSurname());
            if (!file.exists()) {
                file.createNewFile();
            }
            List <String[]> fileContent = readFile(file);
            writeFile(fileContent, file, nextUser);
        }
    }
}
