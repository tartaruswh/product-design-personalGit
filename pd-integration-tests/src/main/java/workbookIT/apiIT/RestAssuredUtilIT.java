package workbookIT.apiIT;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

/**
 * @author 王贺
 */
public class RestAssuredUtilIT {
    private static RestAssuredUtilIT restAssuredUtil = null;
    public static synchronized RestAssuredUtilIT getSingleInstance(){
        if(restAssuredUtil == null){
            restAssuredUtil = new RestAssuredUtilIT();
        }
        return restAssuredUtil;
    }
    private RestAssuredUtilIT(){

    }

    public Response doRequest(InterfaceDataBeanIT interfaceDataBean){
        if (interfaceDataBean.getMethod().equals("Get")){
            if (interfaceDataBean.getRequestAddressConnection()!=null){
                Response response = RestAssured.given()
                        .headers(interfaceDataBean.getHeaders()).log().all()
                        .when()
                        //获取body的id值进行拼接
                        .get(interfaceDataBean.getUrl()+"/"+interfaceDataBean.getRequestAddressConnection())
                        .then().extract().response();
                return response;
            }
            Response response = RestAssured.given()
                    .headers(interfaceDataBean.getHeaders())
                    .params(interfaceDataBean.getParams()).log().all()
                    .when()
                    .get(interfaceDataBean.getUrl())
                    .then().extract().response();
            return response;
        }
        if(interfaceDataBean.getMethod().equals("Post")){
            if(interfaceDataBean.getHeaders().get("Content-Type").equals("application/json")){
                Response response = RestAssured.given()
                        .headers(interfaceDataBean.getHeaders())
                        .params(interfaceDataBean.getParams())
                        .contentType(ContentType.JSON)
                        .body(interfaceDataBean.getBody()).log().all()
                        .when()
                        .post(interfaceDataBean.getUrl())
                        .then().extract().response();
                return response;
            }
            if (interfaceDataBean.getHeaders().get("Content-Type").equals("x-www-form-urlencoded")){
                Response response = RestAssured.given()
                        .headers(interfaceDataBean.getHeaders())
                        .params(interfaceDataBean.getParams())
                        .contentType(ContentType.URLENC)
                        .body(interfaceDataBean.getBody())
                        .when()
                        .post(interfaceDataBean.getUrl())
                        .then().extract().response();
                return response;
            }
            if (interfaceDataBean.getHeaders().get("Content-Type").equals("binary")){
                Response response = RestAssured.given()
                        .headers(interfaceDataBean.getHeaders())
                        .params(interfaceDataBean.getParams())
                        .contentType(ContentType.BINARY)
                        .body(interfaceDataBean.getBody())
                        .when()
                        .post(interfaceDataBean.getUrl())
                        .then().extract().response();
                return response;
            }
        }
        if (interfaceDataBean.getMethod().equals("Delete")){
            if (interfaceDataBean.getRequestAddressConnection()!=null){
                Response response = RestAssured.given()
                        .headers(interfaceDataBean.getHeaders()).log().all()
                        .when()
                        //获取body的id值进行拼接
                        .delete(interfaceDataBean.getUrl()+"/"+interfaceDataBean.getRequestAddressConnection())
                        .then().extract().response();
                return response;
            }
            if(interfaceDataBean.getHeaders().get("Content-Type").equals("x-www-form-urlencoded")){
                JSONObject jsonObject = new JSONObject((Map<String, Object>) interfaceDataBean.getBody());
                Response response = RestAssured.given()
                        .headers(interfaceDataBean.getHeaders())
                        .body(interfaceDataBean.getBody())
                        .when()
                        //获取body的id值进行拼接
                        .delete(interfaceDataBean.getUrl())
                        .then().extract().response();
                return response;
            }
//            Response response = RestAssured.given()
//                    .headers(interfaceDataBean.getHeaders())
//                    .params(interfaceDataBean.getParams())
//                    .body(interfaceDataBean.getBody())
//                    .when()
//                    .delete(interfaceDataBean.getUrl())
//                    .then().extract().response();
//            return response;
        }
        if(interfaceDataBean.getMethod().equals("Put")){
            Response response = RestAssured.given()
                    .headers(interfaceDataBean.getHeaders())
                    .params(interfaceDataBean.getParams())
                    .body((interfaceDataBean.getBody()))
                    .when()
                    .put(interfaceDataBean.getUrl())
                    .then().extract().response();
            return response;
        }
        if (interfaceDataBean.getMethod().equals("Patch")) {
            Response response = RestAssured.given()
                    .headers(interfaceDataBean.getHeaders())
                    .params(interfaceDataBean.getParams())
                    .body(interfaceDataBean.getBody())
                    .when()
                    .patch(interfaceDataBean.getUrl())
                    .then().extract().response();
            return response;
        }
        return null;
    }
}
