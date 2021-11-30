package workbookIT.apiIT;

import java.util.Map;

/**
 * @author 王贺
 * 传输整个接口请求数据的类
 */
public class InterfaceDataBeanIT {
    private String method;
    private String url;
    private Map<String,String> headers;
    private Map<String,?> params;
    private Map<String,?> body;
    private Object requestAddressConnection;

    public Object getRequestAddressConnection() {
        return requestAddressConnection;
    }

    public void setRequestAddressConnection(Object requestAddressConnection) {
        this.requestAddressConnection = requestAddressConnection;
    }
    public InterfaceDataBeanIT() {
    }

    public InterfaceDataBeanIT(String method, String url, Map<String, String> headers, Map<String, String> params, Map<String,?> body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, ?> getParams() {
        return params;
    }

    public void setParams(Map<String, ?> params) {
        this.params = params;
    }

    public Map<String,?> getBody() {
        return body;
    }

    public void setBody(Map<String,?> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "\""+body+"\"";
    }
}
