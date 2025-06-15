//package com.lld.filesystem.util;
//
//import com.lld.filesystem.model.Directory;
//
//import java.util.*;
//
//public class PathUtil {
//    public static List<String> splitPath(String path) {
//        String p = path.startsWith("/") ? path.substring(1) : path;
//        return new ArrayList<>(Arrays.asList(p.split("/")));
//    }
//
//    public static String getFullPath(Directory dir) {
//        if (dir.getParent() == null) return "/";
//        List<String> names = new ArrayList<>();
//        Directory current = dir;
//        while (current.getParent() != null) {
//            names.add(current.getName());
//            current = current.getParent();
//        }
//        Collections.reverse(names);
//        return "/" + String.join("/", names);
//    }
//
//    public static String resolveRelativePath(Directory current, String path) {
//        if (path.startsWith("/")) return path;
//        String currentPath = getFullPath(current);
//        return currentPath.endsWith("/") ? currentPath + path : currentPath + "/" + path;
//    }
//}