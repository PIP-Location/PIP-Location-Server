package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.response.CommentCollectResponse;
import kr.co.pinpick.archive.dto.response.CommentDetailResponse;
import kr.co.pinpick.archive.dto.response.CommentResponse;
import kr.co.pinpick.archive.dto.request.CreateCommentRequest;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.archive.repository.ArchiveCommentRepository;
import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.common.dto.response.PaginateResponse;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArchiveRepository archiveRepository;
    private final ArchiveCommentRepository archiveCommentRepository;

    @Transactional
    public CommentResponse create(User author, Long archiveId, CreateCommentRequest request, Long commendId) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var parent = archiveCommentRepository.findByIdOrElseThrow(commendId);
        var comment = ArchiveComment.builder()
                .author(author)
                .archive(archive)
                .parent(parent)
                .content(request.getContent())
                .build();

        return CommentResponse.fromEntity(archiveCommentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public CommentCollectResponse get(Long archiveId) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var comments = archiveCommentRepository.findByArchiveAndParentIsNull(archive);
        return CommentCollectResponse.builder()
                .collect(comments.stream().map(CommentResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(comments.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public CommentDetailResponse find(Long commentId) {
        var comment = archiveCommentRepository.findByIdOrElseThrow(commentId);
        return CommentDetailResponse.fromEntity(comment);
    }
}
