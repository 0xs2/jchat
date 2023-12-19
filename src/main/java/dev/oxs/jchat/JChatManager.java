package dev.oxs.jchat;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Base64;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class JChatManager {

    public void InsertChatEntry(String user, String uuid,String content, Boolean isCommand) {
        JSONObject caa = new JSONObject();
        caa.put("user", user);
        caa.put("uuid", uuid);
        caa.put("content", encodeBase64(content));
        caa.put("isCommand", isCommand);
        caa.put("timestamp", new Date().getTime());
        appendToChatLog(caa);
    }

    public static JSONArray loadChatLog() {



        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("plugins/JChat/Chat.json")) {
            return (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            return new JSONArray();
        }
    }

    private static void appendToChatLog(JSONObject chatLogEntry) {
        JSONArray chatLog = loadChatLog();
        chatLog.add(chatLogEntry);
        writeChatLog(chatLog);
    }

    private static void writeChatLog(JSONArray chatLog) {
        try (FileWriter writer = new FileWriter("plugins/JChat/Chat.json")) {
            writer.write(chatLog.toJSONString());
        } catch (IOException e) {
            System.out.println("could not write to json.");
        }
    }

    private static String encodeBase64(String originalData) {
        byte[] originalBytes = originalData.getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(originalBytes);
        return new String(encodedBytes);
    }

}