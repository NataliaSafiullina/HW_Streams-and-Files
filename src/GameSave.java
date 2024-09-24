import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Задача 2 - Сохранение
 * 1. Создать три экземпляра класса GameProgress.
 * 2. Сохранить сериализованные объекты GameProgress в папку savegames из предыдущей задачи.
 * 3. Созданные файлы сохранений из папки savegames запаковать в один архив zip.
 * 4. Удалить файлы сохранений, лежащие вне архива.
 */
public class GameSave {

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
                if (!file.delete()) {
                    System.out.println("Ошибка удаления файла " + file.getName());
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
