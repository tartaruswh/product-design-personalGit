package workbookIT.apiIT;

import java.util.Map;

/**
 * @author 王贺
 * 传输某个请求参数的类
 */
public class InterfaceParamsBeanIT {
    private String paramsMethod;
    private String paramsUrl;
    private Map<String,String> paramsHeaders;
    private Map<String,String> paramsParams;
    private Map<String,Object> paramsBody;
    private Object requestAddressConnection;

    public Object getRequestAddressConnection() {
        return requestAddressConnection;
    }

    public void setRequestAddressConnection(Object requestAddressConnection) {
        this.requestAddressConnection = requestAddressConnection;
    }

    public InterfaceParamsBeanIT() {
    }

    public InterfaceParamsBeanIT(String paramsMethod, String paramsUrl, Map<String, String> paramsHeaders, Map<String, String> paramsParams, Map<String,Object> paramsBody) {
        this.paramsMethod = paramsMethod;
        this.paramsUrl = paramsUrl;
        this.paramsHeaders = paramsHeaders;
        this.paramsParams = paramsParams;
        this.paramsBody = paramsBody;
    }

    public String getParamsMethod() {
        return paramsMethod;
    }

    public void setParamsMethod(String paramsMethod) {
        this.paramsMethod = paramsMethod;
    }

    public String getParamsUrl() {
        return paramsUrl;
    }

    public void setParamsUrl(String paramsUrl) {
        this.paramsUrl = paramsUrl;
    }

    public Map<String, String> getParamsHeaders() {
        return paramsHeaders;
    }

    public void setParamsHeaders(Map<String, String> paramsHeaders) {
        this.paramsHeaders = paramsHeaders;
    }

    public Map<String, String> getParamsParams() {
        return paramsParams;
    }

    public void setParamsParams(Map<String, String> paramsParams) {
        this.paramsParams = paramsParams;
    }

    public Map<String, Object> getParamsBody() {
        return paramsBody;
    }

    public void setParamsBody(Map<String,Object> paramsBody) {
        this.paramsBody = paramsBody;
    }
}
