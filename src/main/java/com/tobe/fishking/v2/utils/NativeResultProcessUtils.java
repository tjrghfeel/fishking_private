package com.tobe.fishking.v2.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

import javax.persistence.Tuple;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class NativeResultProcessUtils {

    /**
     * tuple to entity object
     * @param source tuple object
     * @param targetClass target entity class
     * @param <T> target entity type
     * @return target entity
     */
    public static <T> T processResult(Tuple source, Class<T> targetClass) {
        Object instantiate = BeanUtils.instantiateClass(targetClass);
        convertTupleToBean(source,instantiate,null);
        return (T) instantiate;
    }

    /**
     *
     * tuple to entity object
     * @param source tuple object
     * @param targetClass target entity class
     * @param <T> target entity type
     * @param ignoreProperties properties to ignore
     * @return target entity
     */
    public static <T> T processResult(Tuple source,Class<T> targetClass,String... ignoreProperties) {
        Object instantiate = BeanUtils.instantiateClass(targetClass);
        convertTupleToBean(source,instantiate,ignoreProperties);
        return (T) instantiate;
    }

    /**
     * Copy the same value of the attribute name in the tuple to the entity
     * @param source tuple object
     * @param target target object instance
     */
    public static void convertTupleToBean(Tuple source,Object target){
        convertTupleToBean(source,target,null);
    }

    /**
     * Copy the same value of the attribute name in the tuple to the entity
     * @param source tuple object
     * @param target target object instance
     * @param ignoreProperties properties to ignore
     */
    public static void convertTupleToBean(Tuple source,Object target, String... ignoreProperties){
        //target class
        Class<?> actualEditable = target.getClass();
                         // Get the attribute information of the target class
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        // ignore list
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

                         // Traverse attribute node information
        for (PropertyDescriptor targetPd : targetPds) {
                                 // Get the set method
            Method writeMethod = targetPd.getWriteMethod();
                                 // Determine whether the field can be set
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                                         // Get the attribute corresponding to the source node
                String propertyName = targetPd.getName();
                Object value = source.get(propertyName);
                if(value!=null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], value.getClass())) {
                    try {
                                                         // Determine whether the target attribute is private
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                                                         // write target
                        writeMethod.invoke(target, value);
                    }
                    catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                    }
                }
            }
        }
    }

}