package org.easetech.easytest.interceptor;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

import org.easetech.easytest.annotation.Duration;

/**
 * A common class for both {@link InternalInterceptor} as well as {@link InternalInvocationhandler}
 * 
 * @author Anuj Kumar
 *
 */
public class CommonProxyInterceptor {
    
    /** Logger implementation*/
    protected static final Logger LOG = LoggerFactory.getLogger(CommonProxyInterceptor.class);
    
    /**
     * User provided {@link MethodIntercepter}
     */
    private MethodIntercepter userIntercepter;
    
    /**
     * The instance on which to call teh Method
     */
    private Object targetInstance;
    
    /**
     * The expected Run time of the method as specified in the {@link Duration} annotation
     */
    private Long expectedRunTime;
    
    /**
     * @return the userIntercepter
     */
    public MethodIntercepter getUserIntercepter() {
        return userIntercepter;
    }

    /**
     * @param userIntercepter the userIntercepter to set
     */
    public void setUserIntercepter(MethodIntercepter userIntercepter) {
        this.userIntercepter = userIntercepter;
    }

    /**
     * @return the targetInstance
     */
    public Object getTargetInstance() {
        return targetInstance;
    }

    /**
     * @param targetInstance the targetInstance to set
     */
    public void setTargetInstance(Object targetInstance) {
        this.targetInstance = targetInstance;
    }

    /**
     * @return the expectedRunTime
     */
    public Long getExpectedRunTime() {
        return expectedRunTime;
    }

    /**
     * @param expectedRunTime the expectedRunTime to set
     */
    public void setExpectedRunTime(Long expectedRunTime) {
        this.expectedRunTime = expectedRunTime;
    }
    
    /**
     * Get the expected time in Nano seconds
     * @param timeInMillis the given time in millis
     * @return the time in Nanos
     */
    public Long getExpectedTimeInNano(Long timeInMillis) {
        Long result = null;
        Long nanoBase = (long)(1000 * 1000) ;
        if(timeInMillis != null && timeInMillis != 0 && timeInMillis != Long.MAX_VALUE) {
            if (nanoBase > Long.MAX_VALUE / timeInMillis) {
                Assert.fail("EasyTest tries to convert the time (specified in Millisecond using Duration annotation) into nano seconds for precise comparisons." +
                		"But in this particular case, you specified a value that would ultimately overflow. " +
                		"The value is : " + timeInMillis + "(ms). Please specify a smaller time unit.");
            } else {
               result = timeInMillis * nanoBase; 
            }
        }
        return result;
        
    }
    
    /**
     * Compare the time taken by the method to the expected runtime.
     * Fail if the time taken is more, else log the time taken.
     * @param timeTakenInNanos time taken by the method under test
     * @param methodName Name of the method
     */
    public void compareTime(Long timeTakenInNanos , String methodName) {
        Long expectedTimeInNano = getExpectedTimeInNano(getExpectedRunTime());
        Long expectedTimeInMillis, timeTakenInMillis, expectedTimeinMicros, timeTakenInMicros;
        timeTakenInMicros = timeTakenInNanos / 1000 ;
        timeTakenInMillis = (timeTakenInNanos / 1000)/1000;
        if(expectedTimeInNano!= null && timeTakenInNanos > expectedTimeInNano) {
            
            expectedTimeInMillis = (expectedTimeInNano / 1000)/1000;
            expectedTimeinMicros = expectedTimeInNano / 1000 ;
            
            Assert.fail("Total time taken by method " + methodName+" ("+ timeTakenInNanos + " nanosec/"+ timeTakenInMicros +" microsec/"+ timeTakenInMillis+" millisec) is greater than the " +
            		"expected time("+expectedTimeInNano+" nenosec/" +expectedTimeinMicros+" microsec/"+expectedTimeInMillis+" millisec)");
        } else {
            System.out.println("Method " + methodName + " on " + getTargetInstance().getClass()+ " took " + timeTakenInNanos + " nanosec/"+ timeTakenInMicros +" microsec/"+ timeTakenInMillis+" millisec" );
            LOG.debug("Method {} on class {} took " + timeTakenInNanos +" ms", methodName, getTargetInstance().getClass());
        }
    }
    
    /**
     * A common Interceptor method for both {@link InternalInvocationhandler} and
     * {@link InternalInterceptor} that handles delegation to a user defined interceptor
     * and also compares the time taken by the method to the time a user expects a method 
     * to be completed
     * @param method the method to invoke
     * @param args the arguments to the method
     * @return result of method execution
     * @throws Throwable if any exception occurs
     */
    public Object intercept(Method method , Object[] args) throws Throwable {
        Long startTime = System.nanoTime();
        Object result = getUserIntercepter().intercept(method, getTargetInstance(), args);
        Long timeTaken = System.nanoTime() - startTime;
        compareTime(timeTaken , method.getName());
        return result;
    }

}
