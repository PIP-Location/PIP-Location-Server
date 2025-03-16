package kr.co.pinpick.common.aspect;

import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Aspect
@Component
public class FolderAspect {

    /**
     * 권한이 없는 아카이브를 다른사람이 수정 또는 삭제하려고 했을 때 401 반환
     */
    @Around("@annotation(checkFolderAuthorization)")
    public Object hasAuthority(ProceedingJoinPoint joinPoint, CheckFolderAuthorization checkFolderAuthorization) throws Throwable {
        var args = joinPoint.getArgs();
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var parameters = method.getParameters();

        var data = extractArchiveAndUser(args, parameters);

        if (data.user() != null && data.folder() != null && !data.user().getId().equals(data.folder().getUser().getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return joinPoint.proceed();
    }

    private ArchiveFolderData extractArchiveAndUser(Object[] args, Parameter[] parameters) {
        Folder folder = null;
        User user = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Folder a) {
                folder = a;
            } else if (args[i] instanceof User u && parameters[i].getAnnotation(AuthenticationPrincipal.class) != null) {
                user = u;
            }
        }
        return new ArchiveFolderData(folder, user);
    }

    private record ArchiveFolderData(Folder folder, User user) {}
}
