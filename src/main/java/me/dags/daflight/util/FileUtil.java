package me.dags.daflight.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 * @author dags_ <dags@dags.me>
 */

public class FileUtil {

    private static final Logger logger = LogManager.getLogger("DaFlight-IO");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static File createFile(File fileIn) {
        if (!fileIn.exists()) {
            try {
                if (fileIn.createNewFile()) {
                    logger.info("Creating new file: " + fileIn);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileIn;
    }

    public static File createFolder(File parent, String... subDirs) {
        for (String dir : subDirs) {
            parent = new File(parent, dir);
            if (parent.mkdirs()) {
                logger.info("Creating new folder: " + parent);
            }
        }
        return parent;
    }

    public static File getFile(File folder, String name) {
        if (!folder.exists() && folder.mkdirs()) {
            logger.info("Creating folder: " + folder);
        }

        File target = new File(folder, name);

        return createFile(target);
    }

    public static boolean serialize(Object in, File toFile) {
        if (in != null && toFile != null) {
            try (FileWriter fw = new FileWriter(createFile(toFile))) {
                fw.write(gson.toJson(in));
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static <T> Optional<T> deserialize(File in, Class<T> type) {
        if (in.exists()) {
            try (FileReader fr = new FileReader(in)) {
                T t = gson.fromJson(fr, type);
                return Optional.ofNullable(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
