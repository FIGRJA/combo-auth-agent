package org.figrja.combo_auth;

import com.google.gson.Gson;
import org.figrja.combo_auth.config.configGson;
import org.figrja.combo_auth.config.debuglogger.Debug;
import org.figrja.combo_auth.config.debuglogger.DebugAll;
import org.figrja.combo_auth.config.debuglogger.Logger;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth_ahent.Premain;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;


public class auth {

    private static configGson config;
    private static final Gson gson = new Gson();

    public static LoggerMain Logger;


    public void onInitializeServer() {
        Logger = new Logger("combo_auth");
        Logger.info("start loading config");


        File ConfFile = new File("config","combo_auth.json");
        Logger.info(ConfFile.getAbsolutePath());


        try {
            Logger.info("+kek");
            config = gson.fromJson(new FileReader(ConfFile),configGson.class);
            Logger.info("kek");
        } catch (FileNotFoundException e) {
            try {
                defaultConf(ConfFile);
            } catch (IOException ex) {
                ConfFile.getParentFile().mkdir();
                try {
                    defaultConf(ConfFile);
                } catch (IOException exc) {
                    Logger.info("can't create new config");
                    throw new RuntimeException(exc);
                }
            }
        }
        Logger.info("lol");

        if (config.getGebugStatus() != null){
            Logger.info(config.getGebugStatus());
            if (config.getGebugStatus().equals("detail")){
                Logger = new Debug("combo_auth");
            }if (config.getGebugStatus().equals("all")){
                Logger = new DebugAll("combo_auth");
            }
        }

        if (config != null) {
            Logger.info("combo_auth has been enabled!");
        }else{
            Logger.info("combo_auth has not been enabled!");
        }

    }

    public static configGson getConfig() {
        return config;
    }

    private void defaultConf(File ConfFile) throws IOException {
        Logger.info("create new config");
        Files.createFile(ConfFile.toPath());
        InputStream inputStream = Premain.class.getClassLoader().getResourceAsStream("combo_auth.json");
        if (inputStream != null) {
            config = gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)),configGson.class);
        }else {
            Logger.info("wtf inputStream of config in jar is null");
        }
        inputStream = Premain.class.getClassLoader().getResourceAsStream("combo_auth.json");
        if (inputStream != null) {
            PrintWriter printWriter = new PrintWriter(ConfFile);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                printWriter.println(scanner.nextLine());
            }
            scanner.close();

            printWriter.flush();
            printWriter.close();
            Logger.info("ok");
        }else {
            Logger.info("wtf inputStream of config in jar is null too");
        }
        inputStream.close();
    }
}
