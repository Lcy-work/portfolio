package com.cqupt.health_insurance.handler;

import com.cqupt.health_insurance.utils.ResultVo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SignatureException;

@ControllerAdvice //监听controller处理器方法是否发生异常
public class GlobalExceptionHandler {
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ResultVo SignatureExceptionHandler(SignatureException e){
        return ResultVo.fail("token的非法操作，请自重","token_error");
    }
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseBody
    public ResultVo SignatureExceptionHandler(MalformedJwtException e){
        return ResultVo.fail("token解析异常","token_error");
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResultVo SignatureExceptionHandler(ExpiredJwtException e){
        return ResultVo.fail("登陆过时，请重新登录","token_error");
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVo SignatureExceptionHandler(Exception e){
        if(e.getMessage().contains("token")){
            return ResultVo.fail(e.getMessage(),"token_error");
        }
        return ResultVo.fail(e.getMessage());
    }
}
