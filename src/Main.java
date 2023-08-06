import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) {
        String path = "/Users/mikechirkov/Games/savegames/";
        unzipFile(path);
    }

    private static void unzipFile(String path) {
        try (FileInputStream fis = new FileInputStream(path + "save.zip");
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                FileOutputStream fout = new FileOutputStream(path + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fout.write(c);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();

                System.out.println(deserializableGP(path + name));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static GameProgress deserializableGP(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}