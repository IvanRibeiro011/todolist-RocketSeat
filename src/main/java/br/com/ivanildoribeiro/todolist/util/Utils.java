package br.com.ivanildoribeiro.todolist.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    private static String[] getPropertyNames(Object source){
        BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pprts = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pprts){
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null){
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyNonNullProperties(Object src,Object target){
        BeanUtils.copyProperties(src,target,getPropertyNames(src));
    }
}
