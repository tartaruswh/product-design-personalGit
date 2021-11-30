package workbookIT.dataproviderIT;

import org.testng.annotations.DataProvider;
import workbookIT.apiIT.FileUtilIT;
import workbookIT.apiIT.InterfaceDataBeanIT;
import workbookIT.apiIT.InterfaceParamsBeanIT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductSystemManagerIT {

    public static final FileUtilIT fileUtilIT = FileUtilIT.getSingleInstance();

    public ProductSystemManagerIT() {
    }

    @DataProvider(name = "postAddProductSequenceParams",parallel = true)
    public static Iterator<Object[]> postAddProductSequence() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductSequence");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }

    @DataProvider(name = "postAddProductLineParams",parallel = true)
    public static Iterator<Object[]> postAddProductLine() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductLine");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }

    @DataProvider(name = "postAddTechnologyStageParams",parallel = true)
    public static Iterator<Object[]> postAddTechnologyStage() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddTechnologyStage");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }

    @DataProvider(name = "postAddMonomerParams",parallel = true)
    public static Iterator<Object[]> postAddMonomer() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddMonomer");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddMonomerTypeParams",parallel = true)
    public static Iterator<Object[]> postAddMonomerType() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddMonomerType");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProductSequenceTimeOutParams",parallel = true)
    public static Iterator<Object[]> postAddProductSequenceTimeOut() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductSequenceTimeOut");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProductSequenceNameSurpassBoundaryValue",parallel = true)
    public static Iterator<Object[]> postAddProductSequenceNameSurpassBoundaryValue() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductSequenceSurpassMaxBoundaryValue");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProductSequenceNameEqualBoundaryValue",parallel = true)
    public static Iterator<Object[]> postAddProductSequenceNameEqualBoundaryValue() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductSequenceEqualMaxBoundaryValue");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProductSequenceSameName",parallel = true)
    public static Iterator<Object[]> postAddProductSequenceSameName() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductSequenceSameName");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProductSequenceNullOrSpaceName",parallel = true)
    public static Iterator<Object[]> postAddProductSequenceNullOrSpaceName() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProductSequenceNullOrSpaceName");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProSeqParTypeOrIdNotExist",parallel = true)
    public static Iterator<Object[]> postAddProductSequenceParentTypeOrIdNotExist() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProSeqParTypeOrIdNotExist");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProSeqParTypeNotBeNull",parallel = true)
    public static Iterator<Object[]> postAddProSeqParTypeNotBeNull() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProSeqParTypeNotBeNull");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
    @DataProvider(name = "postAddProSeqParIdNotBeNull",parallel = true)
    public static Iterator<Object[]> postAddProSeqParIdNotBeNull() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProSeqParIdNotBeNull");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }

    @DataProvider(name = "postAddProSeqNotMustInput",parallel = true)
    public static Iterator<Object[]> postAddProSeqMustInput() {
        List<InterfaceParamsBeanIT> currentDataList = fileUtilIT.loadInterfaceParamsTraversalYaml("/yamlDataIT/ProductMemberManager/ProductSystemManagerParams.yaml","PostAddProSeqNotMustInput");
        List<Object[]> objects = new ArrayList<Object[]>();
        for (InterfaceParamsBeanIT bean : currentDataList) {
            objects.add(new Object[]{bean});
        }
        return objects.iterator();
    }
}
