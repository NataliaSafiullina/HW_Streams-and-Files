import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Как сделать без константы?
    public static String SAVEGAMES = "C://Games//savegames";

    public static void main(String[] args) throws IOException {

        // Задача 1 - Установка
        // В папке Games создайте несколько директорий: src, res, savegames, temp.
        // В каталоге src создайте две директории: main, test.
        // В подкаталоге main создайте два файла: Main.java, Utils.java.
        // В каталог res создайте три директории: drawables, vectors, icons.
        // В директории temp создайте файл temp.txt

        // Создаем список папок
        List<String> dirs = getDirs();
        // Создаем список файлов
        List<String> files = getFiles();
        // Создаем строку лога
        StringBuilder logger = new StringBuilder();

        // Создадим сами папки
        for (String dir : dirs) {
            makeDir(dir, logger);
        }
        // Создадим файлы
        for (String file : files) {
            makeFile(file, logger);
        }

        // В директории temp создайте файл temp.txt
        FileWriter tempFile = new FileWriter(files.get(2));
        // Запись результатов в файл
        try (BufferedWriter bw = new BufferedWriter(tempFile)) {
            bw.write(String.valueOf(logger));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Список запаковываемых объектов в виде списка строчек String полного пути к файлу
        List<String> filesProgress = new ArrayList<>();

        // Запишем несколько прогрессов игры
        GameProgress[] gameProgresses = {
                new GameProgress(100, 30, 10, 1.0),
                new GameProgress(99, 29, 9, 0.9),
                new GameProgress(98, 28, 8, 0.8)
        };
        // Сохраним каждый прогресс в файл
        for (int i = 0; i < gameProgresses.length; i++) {
            GameProgress gameProgress = gameProgresses[i];
            String file = SAVEGAMES + "\\save" + i + ".dat";
            GameSave.saveGame(file, gameProgress);
            filesProgress.add(file);
        }
        // Запакуем файлы прогрессов в архив
        String zipFileName = SAVEGAMES + "//zip.zip";
        GameSave.zipFiles(zipFileName, filesProgress);

        // Распакуем файлы прогрессов из архива
        GameLoad.openZip(zipFileName, SAVEGAMES);
        // Произвольный файл прогресса десериализуем данные
        String anyFile = SAVEGAMES + GameLoad.prefixName + "save1.dat";
        System.out.println(GameLoad.openProgress(anyFile));

    }

    private static List<String> getDirs() {
        List<String> dirs = new ArrayList<>();
        dirs.add("C://Games//src");
        dirs.add("C://Games//src//mail");
        dirs.add("C://Games//src//test");
        dirs.add("C://Games//res");
        dirs.add("C://Games//res//drawables");
        dirs.add("C://Games//res//vectors");
        dirs.add("C://Games//res//icons");
        dirs.add(SAVEGAMES);
        dirs.add("C://Games//temp");
        return dirs;
    }

    public static List<String> getFiles() {
        List<String> files = new ArrayList<>();
        files.add("C://Games//src//mail//Main.java");
        files.add("C://Games//src//mail//Utils.java");
        files.add("C://Games//temp//temp.txt");
        return files;
    }


    private static void makeFile(String filePath, StringBuilder logger) {
        File currentFile = new File(filePath);
        String fileName = currentFile.getName();
        String dirName = currentFile.getParent();

        // Если папка существует, создадим в ней файл
        File dir = new File(dirName);
        File file = new File(dir, fileName);
        try {
            if (file.createNewFile()) {
                logger.append(String.format("Success - file %s is created \n", fileName));
            }
        } catch (IOException e) {
            logger.append(String.format("Fail - file %s is not created \n", fileName));
        }
    }


    private static void makeDir(String dir, StringBuilder logger) {
        File currentDir = new File(dir);
        if (currentDir.mkdir()) {
            logger.append(String.format("Success - directory %s is created \n", dir));
        } else {
            logger.append(String.format("Fail - directory %s is not created \n", dir));
        }
    }

}

