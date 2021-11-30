package workbookIT;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.*;
import workbookIT.apiIT.*;
import workbookIT.dataproviderIT.ProductSystemManagerIT;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
@Listeners(workbookIT.apiIT.AssertListenerIT.class)
public class ProductSystemManagerTestIT extends Thread{
    private FileUtilIT fileUtilIT;
    private RestAssuredUtilIT restAssuredUtilIT;
    private ResultAssertUtilIT resultAssertUtilIT;
    private InterfaceDataBeanIT addProductMemberBean,queryProductMemberBean;
    private String productSequenceName1,productSequenceName2,productSequenceNotMustInputName;
    private String productLineName1,productLineName2,productLineId;
    private String technologyStageName1,technologyStageName2,technologyStageId;
    private String monomerName1,monomerName2,monomerId;
    private String monomerTypeName1,monomerTypeName2,monomerTypeId;
    @BeforeSuite
    public void init(){
        fileUtilIT = FileUtilIT.getSingleInstance();
        restAssuredUtilIT = RestAssuredUtilIT.getSingleInstance();
        resultAssertUtilIT = ResultAssertUtilIT.getSingleInstance();
    }

    @BeforeClass()
    public void objectAndParamsInit(){
        productSequenceName1 = "产品序列名称一"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        productSequenceName2 = "产品序列名称二"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        productSequenceNotMustInputName = "非必填项为空验证"+Util.getPatternDateTime("yyMMddhhmmssSS");
        productLineName1 = "产品线名称一"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        productLineName2 = "产品线名称二"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        technologyStageName1 = "工艺阶段名称一"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        technologyStageName2 = "工艺阶段名称二"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        monomerName1 = "单体名称一"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        monomerName2 = "单体名称二"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        monomerTypeName1 = "单体类型名称一"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        monomerTypeName2 = "单体类型名称二"+ Util.getPatternDateTime("yyMMddhhmmssSS");
        addProductMemberBean = fileUtilIT.loadInterfaceRequestTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerRequest.yaml","PostAddProductMember");
        queryProductMemberBean = fileUtilIT.loadInterfaceRequestTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerRequest.yaml","GetQueryProductMember");
    }
    @AfterMethod()
    public void getAimData(){
//
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列",groups = {"smoke"},dataProvider = "postAddProductSequenceParams",dataProviderClass = ProductSystemManagerIT.class)
    public void addProductSequenceTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        synchronized (this){
            Response addProductSequenceResponse = null;
            addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
            if (interfaceParamsBeanIT.getParamsBody().get("memberName").equals("productSequenceName1")){
                addProductMemberBean = fileUtilIT.getFinalBeanForBody(addProductMemberBean,"memberName",productSequenceName1);
                addProductSequenceResponse =  restAssuredUtilIT.doRequest(addProductMemberBean);
                fileUtilIT.saveTempData("productSequenceId",addProductSequenceResponse.getBody().path("result"));
            }
            if (interfaceParamsBeanIT.getParamsBody().get("memberName").equals("productSequenceName2")){
                addProductMemberBean = fileUtilIT.getFinalBeanForBody(addProductMemberBean,"memberName",productSequenceName2);
                addProductSequenceResponse =  restAssuredUtilIT.doRequest(addProductMemberBean);
                fileUtilIT.saveTempData("productSequenceId1",addProductSequenceResponse.getBody().path("result"));
            }
            resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
            resultAssertUtilIT.checkBodyContentCodeIs200(addProductSequenceResponse);
            resultAssertUtilIT.checkBodyContentMessageIsAddSuccess(addProductSequenceResponse);
            resultAssertUtilIT.checkBodyContentSuccessIsTrue(addProductSequenceResponse);
            resultAssertUtilIT.checkBodyContentResultNotNull(addProductSequenceResponse);

        }
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("查询产品体系成员")
    @Test(description = "查询添加的产品序列",groups = {"smoke"})
    public void queryProductSequenceTest(){
        queryProductMemberBean = fileUtilIT.getFinalBeanForParams(queryProductMemberBean,"id",fileUtilIT.getTempData("productSequenceId"));
        Response queryProductSequenceResponse = restAssuredUtilIT.doRequest(queryProductMemberBean);
        resultAssertUtilIT.checkStatusCodeIs200(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs200(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsQuerySuccess(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentSuccessIsTrue(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentResultNotNull(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContent(queryProductSequenceResponse,"result.memberName",productSequenceName1);
        resultAssertUtilIT.checkBodyContent(queryProductSequenceResponse,"result.id",fileUtilIT.getTempData("productSequenceId"));
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列名称超过最大边界值",dataProvider = "postAddProductSequenceNameSurpassBoundaryValue",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProductSequenceNameSurpassBoundaryValueTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsFalse(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsSystemError(addProductSequenceResponse);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs500(addProductSequenceResponse);
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列名称等于最大边界值",dataProvider = "postAddProductSequenceNameEqualBoundaryValue",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProductSequenceNameEqualBoundaryValueTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsTrue(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsAddSuccess(addProductSequenceResponse);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs200(addProductSequenceResponse);
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列名称同名新增",dataProvider = "postAddProductSequenceSameName",dataProviderClass = ProductSystemManagerIT.class,dependsOnMethods = "addProductSequenceTest")
    public void postAddProductSequenceAddSameNameTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        addProductMemberBean = fileUtilIT.getFinalBeanForBody(addProductMemberBean,"memberName",productSequenceName1);
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsFalse(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsNameRepetition(addProductSequenceResponse,productSequenceName1);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs500(addProductSequenceResponse);
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列名称为空或空格",dataProvider = "postAddProductSequenceNullOrSpaceName",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProductSequenceNullOrSpaceNameTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsFalse(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageNameNotBeNull(addProductSequenceResponse);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs500(addProductSequenceResponse);
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列父级ID或类型不存在",dataProvider = "postAddProSeqParTypeOrIdNotExist",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProSeqParTypeOrIdNotExistTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsFalse(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsSystemError(addProductSequenceResponse);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs500(addProductSequenceResponse);
    }

    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列父级类型为空",dataProvider = "postAddProSeqParTypeNotBeNull",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProSeqParTypeNotBeNullTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsFalse(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageParentTypeNotBeNull(addProductSequenceResponse);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs500(addProductSequenceResponse);
    }

    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列父级Id为空",dataProvider = "postAddProSeqParIdNotBeNull",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProSeqParIdNotBeNullTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        resultAssertUtilIT.checkBodyContentSuccessIsFalse(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageParentIdNotBeNull(addProductSequenceResponse);
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs500(addProductSequenceResponse);
    }

    @Epic("BIM-6 产品体系管理")
    @Feature("添加产品体系成员")
    @Test(description = "添加产品序列非必填为空验证",dataProvider = "postAddProSeqNotMustInput",dataProviderClass = ProductSystemManagerIT.class)
    public void postAddProSeqNotMustInputTest(InterfaceParamsBeanIT interfaceParamsBeanIT){
        addProductMemberBean.setBody(interfaceParamsBeanIT.getParamsBody());
        Response addProductSequenceResponse = restAssuredUtilIT.doRequest(addProductMemberBean);
        fileUtilIT.saveTempData("productSequenceIdNotMustId",addProductSequenceResponse.getBody().path("result"));
        resultAssertUtilIT.checkStatusCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs200(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsAddSuccess(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentSuccessIsTrue(addProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentResultNotNull(addProductSequenceResponse);
    }
    @Epic("BIM-6 产品体系管理")
    @Feature("查询产品体系成员")
    @Test(description = "查询非必填为空时添加的产品序列",dependsOnMethods = "postAddProSeqNotMustInputTest")
    public void queryProductSequenceNotMustInputTest(){
        queryProductMemberBean = fileUtilIT.getFinalBeanForParams(queryProductMemberBean,"id",fileUtilIT.getTempData("productSequenceIdNotMustId"));
        Response queryProductSequenceResponse = restAssuredUtilIT.doRequest(queryProductMemberBean);
        resultAssertUtilIT.checkStatusCodeIs200(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentCodeIs200(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentMessageIsQuerySuccess(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentSuccessIsTrue(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContentResultNotNull(queryProductSequenceResponse);
        resultAssertUtilIT.checkBodyContent(queryProductSequenceResponse,"result.memberName","非必填项为空验证");
        resultAssertUtilIT.checkBodyContent(queryProductSequenceResponse,"result.id",fileUtilIT.getTempData("productSequenceIdNotMustId"));
    }
}

