package com.practice.mainsvc.service;

import com.practice.mainsvc.controller.comment.VisibilityPatch;
import com.practice.mainsvc.exception.InvalidConditionException;
import com.practice.mainsvc.exception.NotFoundException;
import com.practice.mainsvc.exception.RequestInputException;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.model.PublicationState;
import com.practice.mainsvc.model.User;
import com.practice.mainsvc.repository.CommentRepository;
import com.practice.mainsvc.repository.EventRepository;
import com.practice.mainsvc.repository.ReportRepository;
import com.practice.mainsvc.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class CommentService {
    CommentRepository commentRepository;

    EventRepository eventRepository;
    UserRepository userRepository;
    ReportRepository reportRepository;

    public Page<Comment> findAllByEvent(int eventId, int from, int size, boolean showHidden) {
        findEventById(eventId);

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        if (showHidden) {
            return commentRepository.findAllByEvent_IdAndParentIsNullOrderByCreatedAtDesc(eventId, pageable);
        }

        return commentRepository.findAllByEvent_IdAndHiddenAndParentIsNullOrderByCreatedAtDesc(eventId, false, pageable);
    }

    public Comment findByIdWithRepliesAndParent(int commentId, boolean showHidden) {
        if (showHidden) {
            return commentRepository.findByIdWithAllReplies(commentId)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Comment with id=%d was not found", commentId)));
        }

        return commentRepository.findByIdWithoutHiddenReplies(commentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Comment with id=%d was not found", commentId)));
    }

    public Page<Comment> findRepliesByParentId(int parentId, int from,
                                               int size, boolean showHidden) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        if (showHidden) {
            return commentRepository.findAllByParent_IdOrderByCreatedAt(parentId, pageable);
        }

        return commentRepository.findAllByParent_IdAndHiddenOrderByCreatedAt(parentId, false, pageable);
    }

    public Comment createNew(Comment comment, Integer parentId, Integer userId, Integer eventId) {
        User commentAuthor = findUserById(userId);

        Event commentedEvent = findEventById(eventId);

        if (commentedEvent.getState() != PublicationState.PUBLISHED) {
            throw new InvalidConditionException("Commenting allowed only on published events.");
        }

        if (parentId != null) {
            Comment parent = findCommentWithParent(parentId);

            if (parent.getEvent().getId() != eventId) {
                throw new RequestInputException("Reply must be commented to the same event as parent!");
            }

            if (parent.getParent() != null) {
                comment.setParent(parent.getParent());
            } else {
                comment.setParent(parent);
            }
        }

        comment.setAuthor(commentAuthor);
        comment.setEvent(commentedEvent);

        //значения по умолчанию
        comment.setCreatedAt(LocalDateTime.now());
        comment.setHidden(false);

        commentRepository.save(comment);

        return comment;
    }

    public Comment updateById(Comment updateComment, int commentId, int userId) {
        findUserById(userId);

        Comment foundComment = findCommentById(commentId);

        if (foundComment.getAuthor().getId() != userId) {
            throw new NotFoundException(
                    String.format("Comment with id=%d was not found", commentId));
        }

        foundComment.setText(updateComment.getText());
        foundComment.setUpdatedAt(LocalDateTime.now());

        commentRepository.save(foundComment);

        return foundComment;
    }


    public void deleteByIdPrivateRequest(int commentId, int userId) {
        findUserById(userId);

        Comment foundComment = findCommentById(commentId);

        if (foundComment.getAuthor().getId() != userId) {
            throw new NotFoundException(
                    String.format("Comment with id=%d was not found", commentId));
        }

        reportRepository.deleteAllByComment_IdOrComment_Parent_Id(commentId, commentId);
        commentRepository.deleteByIdAndAllRelated(commentId);
    }

    public Comment updateVisibilityById(int commentId, VisibilityPatch visibility) {
        Comment foundComment = findCommentById(commentId);

        if (visibility == VisibilityPatch.HIDE) {
            foundComment.setHidden(true);
        } else if (visibility == VisibilityPatch.UNHIDE) {
            foundComment.setHidden(false);
        } else {
            return null;
        }

        commentRepository.save(foundComment);

        return foundComment;
    }

    public void deleteByIdAdminRequest(int commentId) {
        findCommentById(commentId);

        reportRepository.deleteAllByComment_IdOrComment_Parent_Id(commentId, commentId);
        commentRepository.deleteByIdAndAllRelated(commentId);
    }

    private Event findEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));
    }

    private Comment findCommentWithParent(int commentId) {
        return commentRepository.findByIdWithParent(commentId)
                .orElseThrow(() -> new NotFoundException(
                String.format("Comment with id=%d was not found", commentId)));
    }

    private Comment findCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Comment with id=%d was not found", commentId)));
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id=%d was not found", userId)));
    }
}
