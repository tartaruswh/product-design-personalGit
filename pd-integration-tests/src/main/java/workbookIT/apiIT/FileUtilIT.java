package workbookIT.apiIT;

import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;
import org.apache.tomcat.jni.Time;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 王贺
 */
public class FileUtilIT {
    private static FileUtilIT fileUtil = null;
//    private String baseUrl = "http://10.10.41.3";
    private String baseUrl = "http://10.10.41.3:8091";
    private HashMap<String,Object> tempDataMap = new HashMap();
    private FileUtilIT(){

    }
    public static synchronized FileUtilIT getSingleInstance(){
        if(fileUtil == null){
            fileUtil = new FileUtilIT();
        }
        return fileUtil;
    }

    /**
     * 获取yaml文件数据，通过setBeanItem将数据赋值到接口bean类
     * @param path 接口请求yaml文件路径
     * @param name 获取的yaml数据的key值，value值为具体的接口格式参数
     * @return 返回请求接口体对象
     */
    public InterfaceDataBeanIT loadInterfaceRequestYaml(String path, String name){
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass().getResourceAsStream(path);
        LinkedHashMap<String, Object> dataBeanMap = yaml.loadAs(inputStream, LinkedHashMap.class);
        InterfaceDataBeanIT interfaceDataBean = setBeanItem(dataBeanMap, name);
        return interfaceDataBean;
    }

    /**
     * 将获取到的键值对数据进行获取，将具体的值赋值给对象
     * @param linkedHashMap 传入的保存为key-value为bean对象的map数据接口，key为每一个yaml一级行名称
     * @param name 获取的yaml数据的key值，value值为具体的接口格式参数
     * @return 返回请求接口体对象
     */
    private InterfaceDataBeanIT setBeanItem(LinkedHashMap<String,Object> linkedHashMap, String name){
        InterfaceDataBeanIT interfaceDataBean = new InterfaceDataBeanIT();
        LinkedHashMap<String,Object> beanItem = (LinkedHashMap<String, Object>) linkedHashMap.get(name);
        if(beanItem != null){
            String method = (String) beanItem.get("Method");
            if(!method.equals("")){
                interfaceDataBean.setMethod(method);
            }
            String url = (String) beanItem.get("Url");
            if(!url.equals("")){
                interfaceDataBean.setUrl(baseUrl+url);
            }
            Map<String,String> headers = (Map<String, String>) beanItem.get("Headers");
            if(headers != null){
                interfaceDataBean.setHeaders(headers);
            }else {
                interfaceDataBean.setHeaders(new HashMap<String,String>());
            }
            Map<String, Object> params = (Map<String, Object>) beanItem.get("Params");
            if(params != null){
                interfaceDataBean.setParams(params);
            }else {
                interfaceDataBean.setParams(new HashMap<String, Object>());
            }
            Map<String,?> body = (Map<String, ?>) beanItem.get("Body");
            if(body != null){
                interfaceDataBean.setBody(body);
            }else {
                interfaceDataBean.setBody(new HashMap<String,Object>());
            }
        }
        return interfaceDataBean;
    }
//----------------------------------------------------------------------------------------------------
    public InterfaceDataBeanIT loadInterfaceRequestTraversalYaml(String path, String name){
        Yaml yaml = new Yaml();
        InputStream in = getClass().getResourceAsStream(path);
        LinkedHashMap<String, Object> dataBeanMap = yaml.loadAs(in, LinkedHashMap.class);
        List<InterfaceDataBeanIT> dataBeanList = null;
        if (dataBeanMap != null) {
            dataBeanList = new ArrayList<InterfaceDataBeanIT>();
            for (int index = 0; index < dataBeanMap.size(); index++) {
                InterfaceDataBeanIT bean = setBeanRequestsItem(dataBeanMap,name,index);
                //同一个yaml文件存在不同的name值时，以此读取时，非相同name保存为空，进行非空验证，保证添加的对象不为空
                if (bean!=null){
                    dataBeanList.add(bean);
                    return dataBeanList.get(0);
                }
            }
        }
        return dataBeanList.get(0);
    }
    private InterfaceDataBeanIT setBeanRequestsItem(LinkedHashMap<String, Object> dataBeanMap, String name, int index) {
        InterfaceDataBeanIT bean = new InterfaceDataBeanIT();
        LinkedHashMap<String, Object> beanItem = (LinkedHashMap<String, Object>) dataBeanMap.get(name + index);
        if (beanItem != null) {
            String paramsMethod = (String) beanItem.get("Method");
            if (paramsMethod != null) {
                bean.setMethod(paramsMethod);
            }
            String paramsUrl = (String) beanItem.get("Url");
            if (paramsUrl != null) {
                bean.setUrl(baseUrl + paramsUrl);
            }
            Map<String, String> parmasHeader = (Map<String, String>) beanItem.get("Headers");
            if (parmasHeader != null) {
                bean.setHeaders(parmasHeader);
            }
            Map<String,Object> paramsBody = (Map<String, Object>) beanItem.get("Body");
            if (paramsBody != null) {
                bean.setBody(paramsBody);
            }
            Map<String, String> paramsParams = (Map<String, String>) beanItem.get("Params");
            if (paramsParams != null) {
                bean.setParams(paramsParams);
            }
            return bean;
        }
        return null;
    }
//----------------------------------------------------------------------------------------------------
    /**
     * 获取接口参数配置
     *
     * @param path 接口参数yaml文件路径
     * @param name 获取的yaml数据的key值，value值为具体的接口格式参数，key值格式为通用名称+数值序号，value格式为map格式，如{body：{id：0}}
     * @return 返回接口参数对象的列表
     */
    public List<InterfaceParamsBeanIT> loadInterfaceParamsTraversalYaml(String path, String name){
        Yaml yaml = new Yaml();
        InputStream in = getClass().getResourceAsStream(path);
        LinkedHashMap<String, Object> dataBeanMap = yaml.loadAs(in, LinkedHashMap.class);
        List<InterfaceParamsBeanIT> dataBeanList = null;
        if (dataBeanMap != null) {
            dataBeanList = new ArrayList<InterfaceParamsBeanIT>();
            for (int index = 0; index < dataBeanMap.size(); index++) {
                InterfaceParamsBeanIT bean = setBeanParamsItem(dataBeanMap,name,index);
                //同一个yaml文件存在不同的name值时，以此读取时，非相同name保存为空，进行非空验证，保证添加的对象不为空
                if (bean!=null){
                    dataBeanList.add(bean);
                }
            }
        }
        return dataBeanList;
    }

    /**
     * 将参数配置赋值给参数对象类
     * @param dataBeanMap 传入的保存为key-value格式，value为bean对象的map数据接口，key为每一个yaml一级行名称+序号
     * @param name 一级行名称
     * @param index 一级行名称后面的需要，遍历得到
     * @return
     */
    private InterfaceParamsBeanIT setBeanParamsItem(LinkedHashMap<String, Object> dataBeanMap, String name, int index) {
        InterfaceParamsBeanIT bean = new InterfaceParamsBeanIT();
        LinkedHashMap<String, Object> beanItem = (LinkedHashMap<String, Object>) dataBeanMap.get(name + index);
        if (beanItem != null) {
            String paramsMethod = (String) beanItem.get("Method");
            if (paramsMethod != null) {
                bean.setParamsMethod(paramsMethod);
            }
            String paramsUrl = (String) beanItem.get("Url");
            if (paramsUrl != null) {
                bean.setParamsUrl(baseUrl + paramsUrl);
            }
            Map<String, String> parmasHeader = (Map<String, String>) beanItem.get("Headers");
            if (parmasHeader != null) {
                bean.setParamsHeaders(parmasHeader);
            }
            Map<String,Object> paramsBody = (Map<String, Object>) beanItem.get("Body");
//            paramsBody.put("memberName","test");
            if (paramsBody != null) {
                bean.setParamsBody(paramsBody);
//                System.out.println("////////"+paramsBody);
            }
            Map<String, String> paramsParams = (Map<String, String>) beanItem.get("Params");
            if (paramsParams != null) {
                bean.setParamsParams(paramsParams);
            }
            return bean;
        }
        return null;
    }

    /**
     * 如果路径有列表，path参数的最终路径只能到列表,然后添加列表中的后续目标路径
     * 通过已知的key-value数据同结构下的另一个key对应的值
     * @param response 获取接口返回的response对象
     * @param path 目标数据上一级路径地址，如果上一级路径数据格式是列表，只能添加到列表级别
     * @param listAfterPath 列表级别路径确定后，列表路径级别下的后续路径地址，到目标数据路径的上一级
     * @param aimKey 要获取的数据的目标key路径
     * @param assistFindKey 已知的数据的key
     * @param assistFindValue 已知的数据的value
     * @return response的body对应路径上的值
     */

    public <T> T getAimParams(Response response, String path, String listAfterPath, String aimKey, T assistFindValue, T assistFindKey){
        if(response.getBody().path(path) instanceof List){
            for(int index = 0;index < ((List<?>) response.getBody().path(path)).size();index++){
                if(response.getBody().path(path+"["+index+"]"+"."+listAfterPath+"."+assistFindKey).equals(assistFindValue)){
                    return response.getBody().path(path+"["+index+"]"+"."+listAfterPath+"."+aimKey);
                }
            }
        }
        if(!(response.getBody().path(path+"."+assistFindKey) instanceof List)){
            if(response.getBody().path(path+"."+assistFindKey).equals(assistFindValue)){
                return response.getBody().path(path+"."+aimKey);
            }
        }
        return null;
    }
    /**
     * 如果路径有列表，path参数的最终路径只能到列表,然后添加列表中的后续目标路径
     * 通过已知的key-value数据同结构下的另一个key对应的值
     * @param response 获取接口返回的response对象
     * @param path 目标数据上一级路径地址，如果上一级路径数据格式是列表，只能添加到列表级别
     * @param listAfterPath 列表级别路径确定后，列表路径级别下的后续路径地址，到目标数据路径的上一级
     * @param assistFindKey 已知的数据的key
     * @param assistFindValue 已知的数据的value
     * @return 获取
     */
    public <T> String getAimPath(Response response, String path, String listAfterPath,T assistFindValue, T assistFindKey){
        if(response.getBody().path(path) instanceof List){
            for(int index = 0;index < ((List<?>) response.getBody().path(path)).size();index++){
                if(response.getBody().path(path+"["+index+"]"+"."+listAfterPath+"."+assistFindKey).equals(assistFindValue)){
                    return path+"["+index+"]";
                }
            }
        }
        if(!(response.getBody().path(path+"."+assistFindKey) instanceof List)){
            if(response.getBody().path(path+"."+assistFindKey).equals(assistFindValue)){
                return path;
            }
        }
        return null;
    }

    /**
     * 获取到目标值后，赋值到类对象中
     * @param interfaceDataBean 要添加某个参数的接口对象
     * @param mapKey 要添加属性的map格式的key
     * @param mapValue 要添加属性的map格式的value
     * @return 添加属性后的接口对象
     */
    public InterfaceDataBeanIT getFinalBeanForBody(InterfaceDataBeanIT interfaceDataBean, String mapKey, Object mapValue){
        //将接口请求对象的body属性{a=b}的格式变为{a:b}的map集合形式
        JSONObject jsonObject=new JSONObject((Map<String, Object>) interfaceDataBean.getBody());
        Map<String, Object> userMap = new HashMap<String,Object>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            userMap.put(entry.getKey(), entry.getValue());
        }
        //将id参数添加到body属性的map集合中
        userMap.put(mapKey,mapValue);
        //将id赋值后，接口请求对象就为最终的请求数据，将对象返回
        interfaceDataBean.setBody(userMap);
        return interfaceDataBean;
    }

    /**
     * 获取到目标值后，赋值到类对象中
     * @param interfaceDataBean 要添加某个参数的接口对象
     * @param mapKey 要添加属性的map格式的key
     * @param mapValue 要添加属性的map格式的value
     * @return 添加属性后的接口对象
     */
    public InterfaceDataBeanIT getFinalBeanForParams(InterfaceDataBeanIT interfaceDataBean, String mapKey, Object mapValue){
        //将接口请求对象的params属性{a=b}的格式变为{a:b}的map集合形式
        JSONObject jsonObject=new JSONObject((Map<String, Object>) interfaceDataBean.getParams());
        Map<String, Object> userMap = new HashMap<String,Object>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            userMap.put(entry.getKey(), entry.getValue());
        }
        //将id参数添加到body属性的map集合中
        userMap.put(mapKey,mapValue);
        //将id赋值后，接口请求对象就为最终的请求数据，将对象返回
        interfaceDataBean.setParams(userMap);
        return interfaceDataBean;
    }

    /**
     * 将返回的响应结果指定数据，以map方式保存，并进行位置替换
     * 当前保存的是产品成员的id与memberOrder值
     * @param response
     * @return
     */
    public Map<String,Object> getChangeOrderMap(Response response){
        ArrayList arrayList = response.getBody().path("result.childProductMemberList");
        Map<String,Object> orderMap = new LinkedHashMap<String,Object>();
        //将所有的产品序列的id与memberOrder，进行map存储,并更换前两个产品成员的顺序
        for (int index = 0; index < arrayList.size(); index++) {
            String id;
            int memberOrder;
            if (index == 0){
                id = response.getBody().path("result.childProductMemberList"+"["+index+"]"+".productMemberVo.id");
                memberOrder = response.getBody().path("result.childProductMemberList"+"["+index+"]"+".productMemberVo.memberOrder");
                memberOrder = memberOrder + 1;
                System.out.println("firstmem"+memberOrder);
                orderMap.put(id,memberOrder);
            }
            if (index == 1) {
                id = response.getBody().path("result.childProductMemberList"+"["+index+"]"+".productMemberVo.id");
                memberOrder = response.getBody().path("result.childProductMemberList"+"["+index+"]"+".productMemberVo.memberOrder");
                memberOrder = memberOrder - 1;
                System.out.println("secondmem"+memberOrder);
                orderMap.put(id,memberOrder);
            }
            if (index != 0 && index != 1){
                id = response.getBody().path("result.childProductMemberList"+"["+index+"]"+".productMemberVo.id");
                memberOrder = response.getBody().path("result.childProductMemberList"+"["+index+"]"+".productMemberVo.memberOrder");
                orderMap.put(id,memberOrder);
            }
        }
        return orderMap;
    }

    public void saveTempData(String mapKey,Object mapValue){
        tempDataMap.put(mapKey,mapValue);
    }

    public Object getTempData(String mapKey) {
        return tempDataMap.get(mapKey);
    }
}
