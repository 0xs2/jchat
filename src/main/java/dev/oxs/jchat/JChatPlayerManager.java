package dev.oxs.jchat;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class JChatPlayerManager {

    public void InsertPlayerEntry(String user, String uuid) {
        JSONObject caa = new JSONObject();
        caa.put("user", user);
        caa.put("uuid", uuid);
        caa.put("isMuted", false);
        caa.put("timestamp", new Date().getTime());
        caa.put("lastSeen", new Date().getTime());
        appendToPlayerData(caa);
    }


    public static JSONArray loadPlayerData() {

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("plugins/JChat/Players.json")) {
            return (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            return new JSONArray();
        }
    }

    private static void appendToPlayerData(JSONObject playerEntry) {
        JSONArray p = loadPlayerData();
        p.add(playerEntry);
        writePlayerData(p);
    }

    private static void writePlayerData(JSONArray playerData) {
        try (FileWriter writer = new FileWriter("plugins/JChat/Players.json")) {
            writer.write(playerData.toJSONString());
        } catch (IOException e) {
            System.out.println("could not write to json.");
        }
    }


    public static JSONObject findUUIDObject(JSONArray jsonArray, String targetUser) {
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                String user = (String) jsonObject.get("uuid");
                if (targetUser.equals(user)) {
                    return jsonObject;
                }
            }
        }
        return null;
    }

    public static JSONObject findUsernameObject(JSONArray jsonArray, String targetUser) {
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                String user = (String) jsonObject.get("user");
                if (targetUser.equals(user)) {
                    return jsonObject;
                }
            }
        }
        return null;
    }
    public void UpdatePlayerData(JSONArray playerArray, String playerUUID, String theObject, Object newData) {
        for (Object obj : playerArray) {
            if (obj instanceof JSONObject) {
                JSONObject playerObject = (JSONObject) obj;
                if (playerUUID.equals(playerObject.get("uuid"))) {
                    if (newData instanceof Integer) {
                        int intValue = (Integer) newData;
                        playerObject.put(theObject, intValue);
                    } else if (newData instanceof String) {
                        String stringValue = (String) newData;
                        playerObject.put(theObject, stringValue);
                    }
                    else if (newData instanceof Long) {
                        Long longValue = (Long) newData;
                        playerObject.put(theObject, longValue);
                    }else {
                        System.out.println("Unsupported type: " + newData.getClass().getSimpleName());
                    }
                    break;
                }
            }
        }
        writePlayerData(playerArray);
    }

    public boolean isUsernameInArray(JSONArray playerArray, String playerUUID) {
        for (Object obj : playerArray) {
            if (obj instanceof JSONObject) {
                JSONObject playerObject = (JSONObject) obj;
                if (playerUUID.equals(playerObject.get("user"))) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isUUIDInArray(JSONArray playerArray, String playerUUID) {
        for (Object obj : playerArray) {
            if (obj instanceof JSONObject) {
                JSONObject playerObject = (JSONObject) obj;
                if (playerUUID.equals(playerObject.get("uuid"))) {
                    return true;
                }
            }
        }
        return false;
    }
}