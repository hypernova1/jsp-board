package board.reflect;

import java.util.Arrays;

public class PathReflect<T> {


    public <T> T getController(String path) {

        T result = null;

        String rootPath = path.split("/")[1];
        String[] rootPathArr = rootPath.split("");
        rootPath = rootPath.replaceFirst(rootPathArr[0], rootPathArr[0].toUpperCase());

        try {
            result = (T) Class.forName("board.controller." + rootPath + "Controller").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }


}
