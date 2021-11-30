package com.bewg.pd.interf.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.bewg.pd.interf.functions.Function;

/**
 *
 *
 * @Title: FuncMapingClassUtil.java
 * @Description: 得到这个类名称
 * @author: maxiaokui
 * @date: 2021年5月15日 下午5:43:51
 * @version V1.0
 *
 *
 */
public class FuncMapingClassUtil {

    private static Map<String, Class<? extends Function>> funcmap = new HashMap<String, Class<? extends Function>>();

    static {
        try {
            // 1.得到这个对象
            Class<?> function = Class.forName("com.bewg.pd.interf.functions.Function");
            // 2.得到这个包名
            String pk = function.getPackage().getName();

            String pkpath = pk.replace(".", "/");
            // System.out.println(pk+"\t"+pkpath);
            // 先得到这个类加载器，然后得到含有pkpath路径的资源，再得到他的路径
            String classpath = function.getClassLoader().getResource(pkpath).getPath();
            // System.out.println("classpath: "+classpath);

            //
            File file = new File(classpath);
            File[] files = file.listFiles();
            for (File f : files) {
                String filename = f.getName();
                // 判读后缀名是不是class
                if (filename.endsWith(".class")) {

                    String className = pk + "." + filename.substring(0, filename.length() - 6);
                    Class<?> c = Class.forName(className);
                    // c类是function类的子类 我们才用
                    if (!function.equals(c) && function.isAssignableFrom(c)) {
                        Function funobject = (Function)c.newInstance();
                        String funname = funobject.getFunc();
                        funcmap.put(funname, funobject.getClass());
                    }
                }
            }
            // System.out.println(funcmap.get("Md5"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isFunc(String funname) {
        // 校验你传入的方法名称在不在map里
        return funcmap.containsKey(funname);
    }

    /**
     * 据函数名得到对应的类，并基于类对象调用方法
     * 
     * @param funname
     * @param args
     * @return
     * @throws Exception
     */
    public static String getvalue(String funname, String[] args) throws Exception {
        return funcmap.get(funname).newInstance().execparm(args);
    }

    public static void main(String[] args) throws Exception {

    }

}
