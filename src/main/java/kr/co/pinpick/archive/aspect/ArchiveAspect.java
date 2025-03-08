package kr.co.pinpick.archive.aspect;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class ArchiveAspect {

    /**
     * 비공개 아카이브 다른사람이 접근했을때 404 반환
     */
    @Around("execution(* kr.co.pinpick.archive.controller.*.*(..))")
    public Object isOwnerIsPrivate(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var parameters = method.getParameters();

        Archive archive = null;
        User user = null;
        for (int i = 0; i < args.length; i++) {
            var arg = args[i];
            if (arg instanceof Archive a) {
                archive = a;
            } else if (arg instanceof User u && parameters[i].getAnnotation(AuthenticationPrincipal.class) != null) {
                // 로그인 된 유저
                user = u;
            }
        }

        if (archive == null) {
            return joinPoint.proceed();
        }

        if (archive.getIsPublic()) {
            return joinPoint.proceed();
        }

        if (user == null || !archive.getAuthor().getId().equals(user.getId())) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, String.format("archive (%s) is private", archive.getId()));
        }

        return joinPoint.proceed();
    }

    /**
     * 아카이브에 포함된 리소스인지
     */
    @Around("execution(* kr.co.pinpick.archive.controller.CommentController.*(..))")
    public Object isChildResource(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();

        Archive archive = null;
        ArchiveComment comment = null;
        for (Object arg : args) {
            if (arg instanceof Archive a) {
                archive = a;
            } else if (arg instanceof ArchiveComment c) {
                comment = c;
            }
        }

        if (comment == null || archive == null) {
            return joinPoint.proceed();
        }

        if (!Objects.equals(comment.getArchive().getId(), archive.getId())) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, comment.getId());
        }

        return joinPoint.proceed();
    }

}