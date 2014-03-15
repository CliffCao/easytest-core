package org.easetech.easytest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.easetech.easytest.loader.EmptyLoader;
import org.easetech.easytest.loader.Loader;
import org.easetech.easytest.loader.LoaderType;

/**
 * 
 * A method or class level annotation providing users with the ability to specify a data {@link Loader} strategy for their test class.
 * EasyTest supports CSV, EXCEL and XML based data loading. But it may not be sufficient in all the cases.
 * Also EasyTest's Data Loading Strategy may not suit every user. In such a case, a user can write his own Custom Loader and pass it to the Data Loader annotation
 * to supply your own custom Loader.<br>
 * 
 * For eg. this is how you can use it :<br>
 * <code>
 *   {@literal @}Test
 *   {@literal @}DataLoader(loader=MyCustomDataLoader.class)<br>
 *    public void testGetItems(........<br>
 * </code>
 *<br>
 *OR
 *<br><br>
 *<code>
 *   {@literal @}Test
 *   {@literal @}DataLoader(filePaths={testData.json} , loader=MyCustomDataLoader.class)<br>
 *    public void testGetItems(........<br>
 * </code>
 *<br>
 *
 *OR
 *
 *<br>
 *<code>
 *   {@literal @}Test
 *   {@literal @}DataLoader(filePaths={testData.csv})<br>
 *    public void testGetItems(........<br>
 * </code>
 *<br>
 *
 *OR
 *
 *<br>
 *<code>
 *   {@literal @}Test
 *   {@literal @}DataLoader(filePaths={testDataExcel.xls})<br>
 *    public void testGetItems(........<br>
 * </code>
 *<br>
 * Note that the custom Loader must implement the {@link Loader} interface and should have a no arg constructor.
 * <br>
 * The data loader annotation also supports the functionality for the user to specify whether the data should be written back to the test file or not
 * using the attribute {@link DataLoader#writeData()}. Its default value is {@link Boolean#TRUE}.
 * 
 * @author Anuj Kumar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.TYPE})
@Inherited
public @interface DataLoader {
    
    /** The list of files representing the input test data for the given test method. */
    String[] filePaths() default {};

    /** The type of file that contains the data. Defaults to "none"*/
    LoaderType loaderType() default LoaderType.NONE;

    
    /** The custom Loader class that will be used by EasyTest to load the test data*/
    Class<? extends Loader> loader() default EmptyLoader.class;
    
    /**
     * Boolean identifying whether the data should be written back to the file or not. 
     * Default behavior is that the data will be written back to the file.
     */
    boolean writeData() default true;
    
    /**
     * Boolean identifying whether data specified in two different files for the same method
     * should be appended or replaced. Default behavior is to replace the data present in one file from the other
     * @return whether data should be appended or not
     */
    boolean appendData() default false;

}
