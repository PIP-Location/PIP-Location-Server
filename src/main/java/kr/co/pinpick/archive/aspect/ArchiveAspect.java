package kr.co.pinpick.archive.aspect;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.lang.reflect.Parameter;
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

        ArchiveUserData data = extractArchiveAndUser(args, parameters);

        if (data.archive() == null || data.archive().getIsPublic()) {
            return joinPoint.proceed();
        }

        if (data.user() == null || !data.archive().getAuthor().getId().equals(data.user().getId())) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND,
                    String.format("archive (%s) is private", data.archive().getId()));
        }

        return joinPoint.proceed();
    }

    @Around("execution(* kr.co.pinpick.archive.controller.ArchiveController.*(..))")
    public Object hasAuthority(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var parameters = method.getParameters();

        ArchiveUserData data = extractArchiveAndUser(args, parameters);

        if (method.isAnnotationPresent(DeleteMapping.class) || method.isAnnotationPresent(PatchMapping.class)) {
            if (data.user() != null && data.archive() != null && !data.user().getId().equals(data.archive().getAuthor().getId())) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }
        }

        return joinPoint.proceed();
    }

    private ArchiveUserData extractArchiveAndUser(Object[] args, Parameter[] parameters) {
        Archive archive = null;
        User user = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Archive a) {
                archive = a;
            } else if (args[i] instanceof User u && parameters[i].getAnnotation(AuthenticationPrincipal.class) != null) {
                user = u;
            }
        }
        return new ArchiveUserData(archive, user);
    }

    private record ArchiveUserData(Archive archive, User user) {}

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