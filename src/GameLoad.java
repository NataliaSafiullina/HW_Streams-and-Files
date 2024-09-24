import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Задача 3 - Загрузка
 * 1. Произвести распаковку архива в папке savegames.
 * 2. Произвести считывание и десериализацию одного из разархивированных файлов save.dat.
 * 3. Вывести в консоль состояние сохранненой игры.
 */

public class GameLoad {

    public static final String prefixName = "\\unzipped_";
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
