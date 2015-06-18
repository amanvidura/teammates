<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="teammates.common.util.Const" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor" prefix="ti" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/feedbackEdit" prefix="feedbackEdit" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/feedbacks" prefix="feedbacks" %>

<c:set var="jsIncludes">
        <link rel="stylesheet" href="/stylesheets/datepicker.css" type="text/css" media="screen">
        
        <script type="text/javascript" src="/js/datepicker.js"></script>
        <script type="text/javascript" src="/js/instructor.js"></script>
        <script type="text/javascript" src="/js/instructorFeedbacks.js"></script>
        <script type="text/javascript" src="/js/instructorFeedbackEdit.js"></script>
</c:set>

<ti:instructorPage pageTitle="TEAMMATES - Instructor" bodyTitle="Edit Feedback Session" jsIncludes="${jsIncludes}" bodyOnload="onload=\"readyFeedbackEditPage();
    bindUncommonSettingsEvents();
    updateUncommonSettingsInfo();
    hideUncommonPanels();\"">
    
    <feedbacks:feedbackSessionsForm fsForm="${data.fsForm}">
        <feedbacks:feedbackSessionsFormAdditionalSettings fsForm="${data.fsForm}" />
    </feedbacks:feedbackSessionsForm>
    <br>
    <t:statusMessage />
    <ti:copyModal />
    
    <c:if test="${empty data.qnForms}">
            <div class="align-center bold" id="empty_message">${data.emptyFsMsg}</div>
            <br><br>
    </c:if>
    <br>
    <c:forEach items="${data.qnForms}" var="question" varStatus="i">
        <feedbackEdit:questionEditForm fqForm="${question}" />
    </c:forEach>
    
    <feedbackEdit:newQuestionForm fqForm="${data.newQnForm}" />
    <feedbackEdit:copyQuestionModal copyQnForm="${data.copyQnForm}" />
    <feedbackEdit:previewSessionForm previewForm="${data.previewForm}" />
    
</ti:instructorPage>