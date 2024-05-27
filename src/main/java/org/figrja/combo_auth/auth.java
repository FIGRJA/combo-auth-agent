package org.figrja.combo_auth;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.figrja.combo_auth.config.configGson;
import org.figrja.combo_auth.config.debuglogger.Debug;
import org.figrja.combo_auth.config.debuglogger.DebugAll;
import org.figrja.combo_auth.config.debuglogger.Logger;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class auth {

    private static configGson config;
    private static final Gson gson = new Gson();

    public static LoggerMain Logger;

    @Override
    public void onInitializeServer() {
        Logger = new Logger("combo_auth");
        Logger.info("start loading config");

        File ConfFile = FabricLoader.getInstance().getConfigDir().resolve( "combo_auth.json" ).toFile();

        try {
            config = gson.fromJson(new JsonReader(new FileReader(ConfFile)),configGson.class);
        } catch (FileNotFoundException e) {
            try {
                Logger.info("create new config");
                Files.createFile(ConfFile.toPath());
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("combo_auth.json");
                if (inputStream != null) {
                    config = gson.fromJson(new JsonReader(new BufferedReader(new InputStreamReader(inputStream))),configGson.class);
                }else {
                    Logger.info("wtf inputStream of config in jar is null");
                }
                inputStream = this.getClass().getClassLoader().getResourceAsStream("combo_auth.json");
                if (inputStream != null) {
                    PrintWriter printWriter = new PrintWriter(ConfFile);
                    Scanner scanner = new Scanner(inputStream);
                    while (scanner.hasNextLine()) {
                        printWriter.println(scanner.nextLine());
                    }
                    scanner.close();

                printWriter.flush();
                printWriter.close();
                }else {
                    Logger.info("wtf inputStream of config in jar is null too");
                }
                inputStream.close();
            } catch (IOException ex) {
                Logger.info("can't create new config");
                throw new RuntimeException(ex);
            }
        }

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
            throw null;
        }

    }

    public static configGson getConfig() {
        return config;
    }
}
