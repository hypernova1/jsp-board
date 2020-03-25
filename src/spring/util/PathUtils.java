package spring.util;

public class PathUtils {

    public static int getSeqFromPath(String path) {
        int seq;
        try {
            String seqStr = path.replace("/", "");
            seq = Integer.parseInt(seqStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            seq = -1;
        }
        return seq;
    }

}
