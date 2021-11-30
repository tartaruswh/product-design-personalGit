package workbookIT.apiIT;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王贺
 */
public class ResultAssertUtilIT {

    public static boolean flag = true;
    public static int statusCode200 = 200;
    public static int statusCode500 = 500;
    public static String messageAddSuccess = "新增成功";
    public static List<Error> errors = new ArrayList<Error>();
    private static ResultAssertUtilIT resultAssertUtil;

    private ResultAssertUtilIT(){}

    public static ResultAssertUtilIT getSingleInstance(){
        if (resultAssertUtil == null){
            resultAssertUtil = new ResultAssertUtilIT();
        }
        return resultAssertUtil;
    }
    public void checkStatusCodeIs200(Response response){
        try {
            Assert.assertEquals(response.getStatusCode(),statusCode200,"验证statusCode是否为200");
        }catch (Error e){
            errors.add(e);
            flag = false;
        }
    }
    public void checkStatusCodeIs500(Response response){
        try {
            Assert.assertEquals(response.getStatusCode(),statusCode500,"验证statusCode是否为200");
        }catch (Error e){
            errors.add(e);
            flag = false;
        }
    }
    public void checkStatusCode(Response response,int statusCode){
        try {
            Assert.assertEquals(response.getStatusCode(),statusCode,"验证statusCode一致");
        }catch (Error e){
            errors.add(e);
            flag = false;
        }
    }

    public void checkStatusCodeNot(Response response,int statusCode){
        try {
            Assert.assertNotEquals(response.getStatusCode(),statusCode,"验证statusCode不相等");
        }catch (Error e){
            errors.add(e);
            flag = false;
        }

    }

    public void checkContentType(Response response,String contentType){
        try {
            Assert.assertEquals(response.getContentType(),contentType,"验证contentType一致");
        }catch (Error e){
            errors.add(e);
            flag = false;
        }

    }

    public void checkContentTypeNot(Response response,String contentType){
        try {
            Assert.assertNotEquals(response.getContentType(),contentType,"验证congtentType不相等");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }

    }
    public void checkBodyContentMessageIsAddSuccess(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"新增成功","验证bodyContent-message为新增成功");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageIsRequestTimeOut(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"请求超时","验证bodyContent-message为请求超时");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageIsSystemError(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"系统错误","验证bodyContent-message为系统错误");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageIsNameRepetition(Response response,String name){
        try {
            Assert.assertEquals(response.getBody().path("message"),"名称重复，请修改:"+name,"验证bodyContent-message为名称重复提示");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageNameNotBeNull(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"名称不能为空","验证bodyContent-message为名称不能为空提示");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageParentTypeNotBeNull(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"父级类型不能为空","验证bodyContent-message父级类型不能为空提示");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageParentIdNotBeNull(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"父级ID不能为空","验证bodyContent-message父级ID不能为空提示");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentSuccessIsTrue(Response response){
        try {
            Assert.assertEquals(response.getBody().path("success"),true,"验证bodyContent-sucdess为true");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentSuccessIsFalse(Response response){
        try {
            Assert.assertEquals(response.getBody().path("success"),false,"验证bodyContent-sucdess为false");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentResultNotNull(Response response){
        try {
            Assert.assertNotEquals(response.getBody().path("result"),null,"验证bodyContent-id不为null");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentCodeIs200(Response response){
        try {
            Assert.assertEquals(response.getBody().path("code"),200,"验证bodyContent-code为200");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentCodeIs500(Response response){
        try {
            Assert.assertEquals(response.getBody().path("code"),500,"验证bodyContent-code为500");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContent(Response response,String bodyPath,Object bodyContent){
        try {
            Assert.assertEquals(response.getBody().path(bodyPath),bodyContent,"验证bodyContent相等");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentMessageIsQuerySuccess(Response response){
        try {
            Assert.assertEquals(response.getBody().path("message"),"查询成功!","验证bodyContent-message为查询成功");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }
    }
    public void checkBodyContentNot(Response response,String bodyPath,String bodyContent){
        try {
            Assert.assertNotEquals(response.getBody().path(bodyPath),bodyContent,"验证bodyContent不相等");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }

    }

    public void checkHeadersContent(Response response,String headerKey,String headerValue){
        try {
            Assert.assertEquals(response.getHeader(headerKey),headerValue,"验证header某个值一致");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }

    }
    public void checkHeadersContentNot(Response response,String headerKey,String headerValue){
        try {
            Assert.assertNotEquals(response.getHeader(headerKey),headerValue,"验证header某个值与目标value不相等");
        } catch (Error e) {
            errors.add(e);
            flag = false;
        }

    }

    public <T> T getAimData(){
        return null;
    }
}
