package org.figrja.combo_auth;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import org.figrja.combo_auth.config.configGson;
import org.figrja.combo_auth.config.debuglogger.Debug;
import org.figrja.combo_auth.config.debuglogger.DebugAll;
import org.figrja.combo_auth.config.debuglogger.Logger;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;

public class auth {
    private static configGson config;
    private final Gson gson = new Gson();
    public static LoggerMain Logger = new Logger("combo_auth");

    public auth() {
    }

    public void onInitializeServer() {
        Logger.info("start loading config");
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

        if (config.getGebugStatus() != null) {
            Logger.info(config.getGebugStatus());
            if (config.getGebugStatus().equals("detail")) {
                Logger = new Debug("combo_auth");
            }

            if (config.getGebugStatus().equals("all")) {
                Logger = new DebugAll("combo_auth");
            }
        }

        Logger.info("combo_auth has been enabled!");
    }

    public static configGson getConfig() {
        return config;
    }

    private void defaultConf(File ConfFile) throws IOException {
        Logger.info("create new config");
        Files.createFile(ConfFile.toPath());
        InputStream inputStream = org.figrja.combo_auth.auth.class.getClassLoader().getResourceAsStream("combo_auth.json");
        if (inputStream != null) {
            config = (configGson)this.gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), configGson.class);
        } else {
            Logger.info("wtf inputStream of config in jar is null");
        }

        inputStream = org.figrja.combo_auth.auth.class.getClassLoader().getResourceAsStream("combo_auth.json");
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
