package com.bewg.pd.interf.util;

import java.io.File;
import java.util.HashMap;

import com.bewg.pd.interf.functions.Function;

/**
 *
 *
 * @Title: FuncMapingClassUtil.java
 * @Description:
 * @author: maxiaokui
 * @date: 2021年5月15日 下午5:43:51
 * @version V1.0
 *
 *
 */
public class FuncMapingClassUtil2 {
    // key value 格式 map
    private static HashMap<String, Class<? extends Function>> funmap = new HashMap<String, Class<? extends Function>>();

    static {
        try {
            // 得到类对象
            Class<?> function = Class.forName("com.bewg.pd.interf.functions.Function");
            // 得到所在的包名称
            String pkname = function.getPackage().getName();

            // 小数点进行转换为\
            String newpk = pkname.replace(".", "/");

            // 得到含有刚才路径的资源类
            String classpath = function.getClassLoader().getResource(newpk).getPath();

            File file = new File(classpath);
            File[] files = file.listFiles();

            for (File f : files) {
                // 得到每个类的名称
                String filename = f.getName();
                // 判断后缀名称是.class文件
                /*
                 *  Function.class
                	Md5Funciton.class
                	RandomFunction.class
                	TimeFunction.class
                	UUIDFunction.class
                 */
                if (filename.endsWith(".class")) {
                    String funname = pkname + "." + filename.substring(0, filename.length() - 6);
                    Class<?> c = Class.forName(funname);
                    // 判断是否是子类
                    if (!function.equals(c) && function.isAssignableFrom(c)) {
                        Function funobject = (Function)c.newInstance();
                        String funcname = funobject.getFunc();
                        funmap.put(funcname, funobject.getClass());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isfun(String key) {
        return funmap.containsKey(key);
    }

    public static String exparm(String key, String[] args) throws Exception {
        return funmap.get(key).newInstance().execparm(args);
    }

    public static void main(String[] args) throws Exception {

    }

}
