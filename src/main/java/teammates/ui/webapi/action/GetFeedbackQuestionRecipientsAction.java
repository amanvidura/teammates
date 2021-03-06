package teammates.ui.webapi.action;

import java.util.Map;

import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.EntityNotFoundException;
import teammates.common.exception.InvalidHttpParameterException;
import teammates.common.util.Const;
import teammates.ui.webapi.output.FeedbackQuestionRecipientsData;
import teammates.ui.webapi.request.Intent;

/**
 * Get the recipients of a feedback question.
 *
 * @see FeedbackQuestionRecipientsData for output format
 */
public class GetFeedbackQuestionRecipientsAction extends BasicFeedbackSubmissionAction {

    @Override
    protected AuthType getMinAuthLevel() {
        return AuthType.PUBLIC;
    }

    @Override
    public void checkSpecificAccessControl() {
        String feedbackQuestionId = getNonNullRequestParamValue(Const.ParamsNames.FEEDBACK_QUESTION_ID);
        FeedbackQuestionAttributes feedbackQuestion = logic.getFeedbackQuestion(feedbackQuestionId);
        if (feedbackQuestion == null) {
            throw new EntityNotFoundException(new EntityDoesNotExistException("The feedback question does not exist."));
        }

        verifyInstructorCanSeeQuestionIfInModeration(feedbackQuestion);

        Intent intent = Intent.valueOf(getNonNullRequestParamValue(Const.ParamsNames.INTENT));
        FeedbackSessionAttributes feedbackSession =
                logic.getFeedbackSession(feedbackQuestion.getFeedbackSessionName(), feedbackQuestion.getCourseId());
        switch (intent) {
        case STUDENT_SUBMISSION:
            gateKeeper.verifyAnswerableForStudent(feedbackQuestion);
            StudentAttributes studentAttributes = getStudentOfCourseFromRequest(feedbackSession.getCourseId());
            checkAccessControlForStudentFeedbackSubmission(studentAttributes, feedbackSession);
            break;
        case INSTRUCTOR_SUBMISSION:
            gateKeeper.verifyAnswerableForInstructor(feedbackQuestion);
            InstructorAttributes instructorAttributes = getInstructorOfCourseFromRequest(feedbackSession.getCourseId());
            checkAccessControlForInstructorFeedbackSubmission(instructorAttributes, feedbackSession);
            break;
        default:
            throw new InvalidHttpParameterException("Unknown intent " + intent);
        }
    }

    @Override
    public ActionResult execute() {
        String feedbackQuestionId = getNonNullRequestParamValue(Const.ParamsNames.FEEDBACK_QUESTION_ID);
        Intent intent = Intent.valueOf(getNonNullRequestParamValue(Const.ParamsNames.INTENT));
        FeedbackQuestionAttributes question = logic.getFeedbackQuestion(feedbackQuestionId);

        Map<String, String> recipient;

        switch (intent) {
        case STUDENT_SUBMISSION:
            StudentAttributes studentAttributes = getStudentOfCourseFromRequest(question.getCourseId());

            recipient = logic.getRecipientsOfQuestion(question, null, studentAttributes);
            break;
        case INSTRUCTOR_SUBMISSION:
            InstructorAttributes instructorAttributes = getInstructorOfCourseFromRequest(question.getCourseId());

            recipient = logic.getRecipientsOfQuestion(question, instructorAttributes, null);
            break;
        default:
            throw new InvalidHttpParameterException("Unknown intent " + intent);
        }
        return new JsonResult(new FeedbackQuestionRecipientsData(recipient));
    }

}
