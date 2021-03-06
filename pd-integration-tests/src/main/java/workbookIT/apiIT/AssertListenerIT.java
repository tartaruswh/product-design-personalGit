package workbookIT.apiIT;


import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王贺
 * 继承listener类，对ResultAssert结果判断进行异常报错响应处理，保证出现异常时不中断测试
 */
public class AssertListenerIT extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult result) {
        ResultAssertUtilIT.flag = true;
        ResultAssertUtilIT.errors.clear();
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        this.handleAssertion(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        this.handleAssertion(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        this.handleAssertion(tr);
    }

    private int index = 0;

    private void handleAssertion(ITestResult tr){
        if(!ResultAssertUtilIT.flag){
            Throwable throwable = tr.getThrowable();
            if(throwable==null){
                throwable = new Throwable();
            }
            StackTraceElement[] traces = throwable.getStackTrace();
            StackTraceElement[] alltrace = new StackTraceElement[0];
            for (Error e : ResultAssertUtilIT.errors) {
                StackTraceElement[] errorTraces = e.getStackTrace();
                StackTraceElement[] et = this.getKeyStackTrace(tr, errorTraces);
                StackTraceElement[] message = new StackTraceElement[]{new StackTraceElement("message : "+e.getMessage()+" in method : ", tr.getMethod().getMethodName(), tr.getTestClass().getRealClass().getSimpleName(), index)};
                index = 0;
                alltrace = this.merge(alltrace, message);
                alltrace = this.merge(alltrace, et);
            }
            if(traces!=null){
                traces = this.getKeyStackTrace(tr, traces);
                alltrace = this.merge(alltrace, traces);
            }
            throwable.setStackTrace(alltrace);
            tr.setThrowable(throwable);
            ResultAssertUtilIT.flag = true;
            ResultAssertUtilIT.errors.clear();
            tr.setStatus(ITestResult.FAILURE);
        }
    }

    private StackTraceElement[] getKeyStackTrace(ITestResult tr, StackTraceElement[] stackTraceElements){
        List<StackTraceElement> ets = new ArrayList<StackTraceElement>();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if(stackTraceElement.getClassName().equals(tr.getTestClass().getName())){
                ets.add(stackTraceElement);
                index = stackTraceElement.getLineNumber();
            }
        }
        StackTraceElement[] et = new StackTraceElement[ets.size()];
        for (int i = 0; i < et.length; i++) {
            et[i] = ets.get(i);
        }
        return et;
    }

    private StackTraceElement[] merge(StackTraceElement[] traces1, StackTraceElement[] traces2){
        StackTraceElement[] ste = new StackTraceElement[traces1.length+traces2.length];
        for (int i = 0; i < traces1.length; i++) {
            ste[i] = traces1[i];
        }
        for (int i = 0; i < traces2.length; i++) {
            ste[traces1.length+i] = traces2[i];
        }
        return ste;
    }
}

