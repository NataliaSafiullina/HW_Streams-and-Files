import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static final String prefixName = "\\unzipped_";

    public static void main(String[] args) throws IOException {

        // Задача 1 - Установка

        String dir = "C://Games";
        StringBuilder log = new StringBuilder();

        // В папке Games будет несколько директорий: src, res, savegames, temp.
        File srcDir = new File(dir + "//src");
        File resDir = new File(dir + "//res");
        File savegamesDir = new File(dir + "//savegames");
        File tempDir = new File(dir + "//temp");

        // В каталоге src создайте две директории: main, test.
        File mainDir = new File(srcDir + "//mail");
        File testDir = new File(srcDir + "//test");

        // В подкаталоге main создайте два файла: Main.java, Utils.java.
        File mainFile = new File(mainDir, "Main.java");
        File utilsFile = new File(mainDir, "Utils.java");

        // В каталог res создайте три директории: drawables, vectors, icons.
        File drawablesDir = new File(resDir + "//drawables");
        File vectorsDir = new File(resDir + "//vectors");
        File iconsDir = new File(resDir + "//icons");


        // Создаем коталоги в корневой папке
        log.append((srcDir.mkdir()) ? "Success - dir src is created \n" : "Fail - dir src isn't created \n");
        log.append((resDir.mkdir()) ? "Success - dir res is created \n" : "Fail - dir res isn't created \n");
        log.append((savegamesDir.mkdir()) ? "Success - dir savegames is created \n" : "Fail - dir savegames isn't created \n");
        log.append((tempDir.mkdir()) ? "Success - dir temp is created \n" : "Fail - dir temp isn't created \n");

        // Если папка src успешно создана создадим в ней папки
        if (srcDir.exists()) {
            log.append((mainDir.mkdir()) ? "Success - dir main is created \n" : "Fail - dir main isn't created \n");
            log.append((testDir.mkdir()) ? "Success - dir test is created \n" : "Fail - dir test isn't created \n");
        }

        // Если папка main успешно создана создадим в ней файлы
        if (mainDir.exists()) {
            try {
                if (mainFile.createNewFile()) {
                    log.append("Success - file Main.java is created \n");
                }
            } catch (IOException e) {
                log.append("Fail - file Main.java isn't created \n");
            }

            try {
                if (utilsFile.createNewFile()) {
                    log.append("Success - file Utils.java is created \n");
                }
            } catch (IOException e) {
                log.append("Fail - file Utils.java isn't created \n");
            }
        }

        // Если папки res созадана успешно созаддим в ней папки
        if (resDir.exists()) {
            log.append((drawablesDir.mkdir()) ? "Success - dir drawables is created \n" : "Fail - dir drawables isn't created \n");
            log.append((vectorsDir.mkdir()) ? "Success - dir vectors is created \n" : "Fail - dir vectors isn't created \n");
            log.append((iconsDir.mkdir()) ? "Success - dir icons is created \n" : "Fail - dir icons isn't created \n");
        }


        // В директории temp создайте файл temp.txt
        FileWriter tempFile = new FileWriter(new File(tempDir, "temp.txt"));
        // Запись результатов в файл
        try (BufferedWriter bw = new BufferedWriter(tempFile)) {
            bw.write(String.valueOf(log));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Задача 2 - Сохранение
        // 1. Создать три экземпляра класса GameProgress.
        // 2. Сохранить сериализованные объекты GameProgress в папку savegames из предыдущей задачи.
        // 3. Созданные файлы сохранений из папки savegames запаковать в один архив zip.
        // 4. Удалить файлы сохранений, лежащие вне архива.

        // Список запаковываемых объектов в виде списка строчек String полного пути к файлу
        List<String> files = new ArrayList<>();

        GameProgress gameProgress1 = new GameProgress(100, 30, 10, 1.0);
        String file1 = savegamesDir + "\\save1.dat";
        saveGame(file1, gameProgress1);
        files.add(file1);

        GameProgress gameProgress2 = new GameProgress(99, 29, 9, 0.9);
        String file2 = savegamesDir + "\\save2.dat";
        saveGame(file2, gameProgress2);
        files.add(file2);

        GameProgress gameProgress3 = new GameProgress(98, 28, 8, 0.8);
        String file3 = savegamesDir + "\\save3.dat";
        saveGame(file3, gameProgress3);
        files.add(file3);

        String zipFileName = savegamesDir + "//zip.zip";
        zipFiles(zipFileName, files);

        // Задача 3 - Загрузка
        // 1. Произвести распаковку архива в папке savegames.
        // 2. Произвести считывание и десериализацию одного из разархивированных файлов save.dat.
        // 3. Вывести в консоль состояние сохранненой игры.
        openZip(zipFileName, savegamesDir.getPath());
        String anyFile = savegamesDir.getPath() + prefixName + "save2.dat";
        System.out.println(openProgress(anyFile));

    }


    /**
     * Метод сохранения игры
     * <p>
     * String path - путь к файлу куда сохранить
     * gameProgress - объект, который нужно сохранит
     */
    public static void saveGame(String path, GameProgress gameProgress) {


        // Создаем выходной поток и сериализуем объект
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // Запишем объект в файл
            oos.writeObject(gameProgress);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Метод, который архивирует указанный список файлов
     * <p>
     * pathArchive - путь и имя архива
     * listFiles - список имён файлов, включая полный путь
     */
    public static void zipFiles(String pathArchive, List<String> listFiles) {

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathArchive))) {

            for (String path : listFiles) {

                // создадим имя файла для zip-сущности
                File file = new File(path);
                String fileName = file.getName();

                // создаем входящий поток
                try (FileInputStream fis = new FileInputStream(path)) {
                    // создаем объект ZipEntry
                    ZipEntry entry = new ZipEntry(fileName);
                    zout.putNextEntry(entry);

                    // считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    if (fis.read(buffer) > -1) {
                        // добавляем содержимое к архиву
                        zout.write(buffer);
                    }

                    // закроем zip-сущность
                    zout.closeEntry();

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                // Удалим файл
                if(!file.delete()){
                    System.out.println("Ошибка удаления файла " + file.getName());
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Метод по разархивации архива и десериализации файла сохраненной игры в Java класс.
     * <p>
     * String pathArch - путь к архиву
     * String pathDestination - целевой путь, куда распоковать
     */
    public static void openZip(String pathArch, String pathDestination) {

        // открываем поток для архива и считываем архив по указанному пути
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathArch))) {
            ZipEntry entry;
            String name;

            while ((entry = zin.getNextEntry()) != null) {
                // получим имя файла и немного преобразуем его, чтобы отличать от исходного
                name = pathDestination + prefixName + entry.getName();

                // распаковка
                try (FileOutputStream fout = new FileOutputStream(name)) {
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    // запись всех данных, скопившихся в буфере в файл
                    fout.flush();
                    // закрываем текущую zip-сущность
                    zin.closeEntry();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Метод извлечения данных из файла
     * и запись состояния игры в объект
     */
    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;

        // откроем входной поток на чтение
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // читаем файл и записываем в объект нашего класса
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }

}

