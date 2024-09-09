package com.example.domain.reviewDentist.business;

import annotation.Business;
import com.example.domain.reviewDentist.controller.model.ReviewRequest;
import com.example.domain.reviewDentist.controller.model.ReviewResponse;
import com.example.domain.reviewDentist.converter.ReviewConverter;
import com.example.domain.reviewDentist.service.ReviewService;
import com.example.domain.user.service.UserService;
import com.example.error.UserErrorCode;
import com.example.reviewDentist.Document.ReviewDoc;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Business
@RequiredArgsConstructor
@Transactional
public class CommentBusiness {

    private final ReviewService reviewService;
    private final ReviewConverter reviewConverter;
    private final UserService userService;

    /**
     * 대댓글 다는 로직
     */
    public String addReplyToComment(Long id, String reviewId, ReviewRequest reviewRequest) {

        ReviewDoc reviewDoc = reviewService.findReviewById(reviewId);
        if (reviewDoc == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "리뷰를 찾을 수 없습니다.");
        }

        // 로그인한 유저와, 리뷰를 적는 유저가 같은지 확인하는 로직
        if (!reviewRequest.getUserId().equals(id)){
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "로그인 한 유저와 추가하는 리뷰의 유저가 다릅니다!!!");
        }

        ReviewDoc replyDoc = reviewConverter.toReviewDoc(reviewRequest);
        replyDoc.setTag(reviewDoc.getTag());
        replyDoc.setDepth(reviewDoc.getDepth()+1);
        replyDoc.setHospitalId(reviewDoc.getHospitalId());
        replyDoc.setCommentReplys(new ArrayList<>());

        reviewService.saveReview(replyDoc);

        // 대댓글을 부모 댓글의 commentReplys 리스트에 추가
        reviewDoc.getCommentReplys().add(replyDoc.getId());
        reviewService.saveReview(reviewDoc);

        return reviewId + " 에" + replyDoc.getId() + "의 대댓글이 추가되었습니다";
    }
    /**
     * 대댓글 수정하는 로직
     * 대댓글을 작성한 본인만 대댓글을 수정할 수 있어야 한다
     */
    public ReviewResponse updateReplyComment(Long userId, String reviewId, ReviewRequest reviewRequest) {
        // 유저 인증 확인
        if (!reviewRequest.getUserId().equals(userId)) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "로그인 한 유저와 수정하려는 유저가 다릅니다!!!");
        }

        // 리뷰 존재 여부 및 작성자 확인
        ReviewDoc reviewDoc = reviewService.findReviewById(reviewId);

        if (reviewDoc == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "리뷰를 찾을 수 없습니다.");
        }
        if (!reviewRequest.getUserId().equals(reviewDoc.getUserId())) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자신이 작성한 리뷰만 수정할 수 있습니다!!!");
        }
        if (reviewDoc.getDepth()==1){
            throw new ApiException(ErrorCode.SERVER_ERROR, "대댓글만 수정하는 로직입니다!");
        }

        // 리뷰 내용 업데이트
        reviewDoc.setContent(reviewRequest.getContent());
        reviewService.saveReview(reviewDoc);

        // 닉네임 조회 및 ReviewResponse 생성
        String nickName = userService.getUserById(reviewDoc.getUserId()).getNickName();
        ReviewResponse reviewResponse = reviewConverter.toReviewResponse(reviewDoc);
        reviewResponse.setNickName(nickName);

        return reviewResponse;
    }
    /**
     * 대댓글 삭제하는 로직
     * 대댓글을 작성한 본인만 댓글을 삭제할 수 있어야 한다
     * 대댓글 삭제시, 그 상위 댓글의 리스트에서 대댓글을 삭제해줘야 한다
     */

    public String deleteReplyComment(Long userId, String commentId) {

        ReviewDoc reviewDoc = reviewService.findReviewById(commentId);

        if (reviewDoc == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "리뷰를 찾을 수 없습니다.");
        }
        if (!userId.equals(reviewDoc.getUserId())) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자신이 작성한 리뷰만 삭제할 수 있습니다!!!");
        }

        ReviewDoc parentComment = reviewService.findParentCommentById(commentId);

        boolean removed = parentComment.getCommentReplys().remove(new ObjectId(commentId));

        if (!removed) {
            throw new ApiException(ErrorCode.NULL_POINT, "댓글이 부모 댓글의 댓글 목록에 없습니다.");
        }
        reviewService.saveReview(parentComment);
        reviewService.deleteById(commentId);

        return commentId + " 의 댓글이 삭제되었습니다";
    }


    /**
     대댓글들을 가져오는 로직
     **/
    public List<ReviewResponse> getCommentsForReview(String reviewId) {
        // 리뷰를 찾고 null 체크
        ReviewDoc reviewDoc = reviewService.findReviewById(reviewId);
        if (reviewDoc == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "리뷰를 찾을 수 없습니다.");
        }

        // 댓글 ID 리스트를 가져오고 ObjectId로 변환
        List<ObjectId> commentIds = reviewDoc.getCommentReplys();

        // 댓글들을 조회하고 ReviewResponse로 변환
        return reviewService.findCommentsByIds(commentIds)
                .stream()
                .map(review -> {
                    ReviewResponse reviewResponse = reviewConverter.toReviewResponse(review);
                    // nickname을 조회하여 설정
                    String nickName = userService.getUserById(review.getUserId()).getNickName();
                    reviewResponse.setNickName(nickName);
                    return reviewResponse;
                })
                .collect(Collectors.toList());
    }

}
