package kz.greetgo.sandbox.controller.util;


import java.util.Base64;

public class FileUtil {
  public static byte[] bytesToBase64(String base64) {
    return Base64.getDecoder().decode(base64);
  }

  public static String bytesToBase64(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }
}
