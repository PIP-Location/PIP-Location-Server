package kr.co.pinpick.common.aspect;

import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserLinkAspect {

    /**
     * 이미 링크되어 있을때 에러 렌더링
     */
    @Around("execution(* kr.co.pinpick.common.service.IUserLinkService.link(..))")
    public Object link(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.USER_ALREADY_LINKED);
        }
    }

    /**
     * 링크 안되어 있을떄 있을때 에러 렌더링
     */
    @Around("execution(* kr.co.pinpick.common.service.IUserLinkService.unlink(..))")
    public Object unlink(ProceedingJoinPoint joinPoint) throws Throwable {
        var result = (int) joinPoint.proceed();
        if (result == 0) {
            throw new BusinessException(ErrorCode.USER_ALREADY_UNLINKED);
        }
        return result;
    }
}