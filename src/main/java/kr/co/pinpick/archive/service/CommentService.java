package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.CommentCollectResponse;
import kr.co.pinpick.archive.dto.CommentDetailResponse;
import kr.co.pinpick.archive.dto.CommentResponse;
import kr.co.pinpick.archive.dto.CreateCommentRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.user.entity.User;

public interface CommentService {
    CommentResponse create(User author, Archive archive, CreateCommentRequest request, ArchiveComment parent);

    CommentCollectResponse get(Archive archive);

    CommentDetailResponse find(ArchiveComment comment);
}
