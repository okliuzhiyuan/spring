package cn.liu.spring.annotion;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

/**
 * Created by LIUC022 on 2016/9/9.
 */
public class ValidateArgumentResolver implements HandlerMethodArgumentResolver {
    @Override public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ValidationParam.class) !=null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //获取参数名称
        String parameter = methodParameter.getParameterName();

        //获取参数类型
        String parameterType = methodParameter.getParameterType().getName();

        //获取参数值
        String value = webRequest.getParameter(parameter);

        //获取参数注解
        ValidationParam validation = methodParameter.getParameterAnnotation(ValidationParam.class);

        //验证参数合法性，并且返回参数真实类型
        return this.validate(validation, parameter, parameterType, value);
     }
    private Object validate(ValidationParam validation,
        String parameter,
        String parameterType,
        String value) throws IllegalArgumentException{
        /**
         * 验证参数是否可为空
         * 如果不为空，则必须传递该参数，并且参数的值不可为NUll
         */
        if(validation.notNull() && StringUtils.isEmpty(value)){
            throw new IllegalArgumentException(
                String.format("Required parameter '%s' is not present", parameter)
            );
        }

        /**
         * 获取参数真实类型的值
         */
        Object realValue = value;
        if(parameterType.equals("java.lang.Integer")){
            realValue = Integer.valueOf(value);
        }

        /**
         * 验证固定的Integer值
         */
        if(ArrayUtils.isNotEmpty(validation.fixedInteger())){
            if(Arrays.binarySearch(validation.fixedInteger(), (Integer) realValue) <0){
                throw new IllegalArgumentException(
                    String.format("Parameter '%s' must in %s", parameter,
                        Arrays.toString(validation.fixedInteger()))
                );
            }
        }

        /**
         * 验证最小值
         */
        if(validation.min() != -1&& (Integer) realValue < validation.min()){
            throw new IllegalArgumentException(
                String.format("Parameter '%s' must be equal or greater than %s",
                    parameter, validation.min())
            );
        }

        /**
         * 验证最大值
         */
        if(validation.max() != -1&& (Integer) realValue > validation.max()){
            throw new IllegalArgumentException(
                String.format("Parameter '%s' must be equal or less than %s",
                    parameter, validation.max())
            );
        }

        return realValue;
    }
}
