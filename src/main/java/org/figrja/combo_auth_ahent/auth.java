package org.figrja.combo_auth_ahent;

import com.google.gson.Gson;
import org.figrja.combo_auth_ahent.config.Config;
import org.figrja.combo_auth_ahent.config.configGson;
import org.figrja.combo_auth_ahent.config.debuglogger.Logger;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class auth {
    private static configGson config;
    private final Gson gson = new Gson();
    public static LoggerMain Logger = new Logger("combo_auth");

    public Config onInitializeServer() {
        Logger.info("loading config");
        File ConfFile = new File("config", "combo_auth.json");

        try {
            config = (configGson)this.gson.fromJson(new FileReader(ConfFile), configGson.class);
        } catch (FileNotFoundException var7) {
            try {
                this.defaultConf(ConfFile);
            } catch (IOException var6) {
                ConfFile.getParentFile().mkdir();

                try {
                    this.defaultConf(ConfFile);
                } catch (IOException var5) {
                    Logger.info("can't create new config");
                    throw new RuntimeException(var5);
                }
            }
        }


        return (Config) config;

    }

    public static configGson getConfig() {
        return config;
    }

    private void defaultConf(File ConfFile) throws IOException {
        Logger.info("create new config");
        Files.createFile(ConfFile.toPath());
        InputStream inputStream = org.figrja.combo_auth_ahent.auth.class.getClassLoader().getResourceAsStream("combo_auth.json");
        if (inputStream != null) {
            config = gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), configGson.class);
        } else {
            Logger.info("wtf inputStream of config in jar is null");
        }

        inputStream = org.figrja.combo_auth_ahent.auth.class.getClassLoader().getResourceAsStream("combo_auth.json");
        if (inputStream != null) {
            PrintWriter printWriter = new PrintWriter(ConfFile);
            Scanner scanner = new Scanner(inputStream);

            while(scanner.hasNextLine()) {
                printWriter.println(scanner.nextLine());
            }

            scanner.close();
            printWriter.flush();
            printWriter.close();
        } else {
            Logger.info("wtf inputStream of config in jar is null too");
        }

        inputStream.close();
    }
}
