package com.github.kanon.common.exceptions.handler;

import com.github.kanon.common.base.model.vo.ResponseParam;
import com.github.kanon.common.constants.ResultMsgEnum;
import com.github.kanon.common.exceptions.AuthorityException;
import com.github.kanon.common.exceptions.ErrorMsgException;
import com.github.kanon.common.exceptions.InitialPasswordException;
import com.github.kanon.common.exceptions.LoginNotException;
import com.github.kanon.common.utils.spring.I18nUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @Author: PengCheng
 * @Description:    项目的统一异常处理器
 * @Date: 2018/5/20
 */
@ControllerAdvice
@ResponseBody
public class KanonExceptionHandler {

    @ExceptionHandler(ErrorMsgException.class)
    public ResponseParam handleErrorMsgException(ErrorMsgException e){
        String msg = I18nUtil.i18nParser(e.getMsg(),e.getText());
        if (e.getIsRefresh()){
            return new ResponseParam(ResultMsgEnum.VALIDATION_ERROR.getResultCode(),msg);
        }else {
            return new ResponseParam(ResultMsgEnum.VALIDATION_ERROR.getResultCode(),msg);
        }
    }

    @ExceptionHandler(LoginNotException.class)
    public ResponseParam handleLoginNotException(LoginNotException e){
        return new ResponseParam(ResultMsgEnum.UN_LOGIN);
    }

    @ExceptionHandler(InitialPasswordException.class)
    public ResponseParam handleInitialPasswordException(InitialPasswordException e){
        return new ResponseParam(ResultMsgEnum.INIT_PASS);
    }

    @ExceptionHandler(AuthorityException.class)
    public ResponseParam handleAuthorityException(AuthorityException e){
        return new ResponseParam(ResultMsgEnum.PERMISSION_DENIED);
    }

    /**
     * 处理SpringValidation的验证信息
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseParam handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<ObjectError> errors =e.getBindingResult().getAllErrors();
        StringBuffer errorMsg=new StringBuffer();
        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append("\r\n"));
        return new ResponseParam(ResultMsgEnum.VALIDATION_ERROR.getResultCode(),errorMsg.toString());
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseParam handleException(Exception e){
        return ResponseParam.systemError();
    }

}
