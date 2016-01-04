$(document).ready(function(){    
    $(".question-list").each(function(){
        
        // adds an answer submit button appended to each question <ul>
        var questionId = $(this).attr("id").split('-').pop();
        $(this).append($("<input type='submit' value='Tarkista' id='question-send-" + questionId +"'/>"));
        
        // adds a listener attached to each of these buttons
        $(this).children("input[type=submit]").click(function(event){
            event.preventDefault();
            var questionId = $(event.target).attr("id").split('-').pop();
            var answerOptionId = $("#question-" + questionId + " input[type=radio]:checked").val();
            sendAnswer(questionId, answerOptionId);
       });      
    });
    
   // queries results of possible previous answers and populates the answer selections
   // and result texts accordingly
   getPreviousResults(); 
});

function getPreviousResults(){
    var questionSeriesId = $("#questionSeriesId").val();
    $.ajax({
        url: "/vastaukset/topic/" + questionSeriesId + "/aiemmat",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        type: "get",
        success: function(result){
            populatePreviousResults(result)
        }
    });
}

function populatePreviousResults(result){
    $.each(result[0], function(key, answerResult){
        var answerOptionId = answerResult.AnswerOptionId;
        var questionId = answerResult.QuestionId;
        if(answerResult.ResultText != 'ei vastattu'){
            $("input:radio[value=" + answerOptionId + "]").prop("checked", true);
            populateResultText(questionId, answerResult.ResultText, answerResult.Comment);
        }
    });
    var achievementHtml = "";
    if(result[1]){
        $.each(result[1], function(key, achievement){
           achievementHtml += "<li>" + achievement.name + "</li>"; 
        });
    }
    
    $("#achievement-list").html(achievementHtml);
}

function sendAnswer(questionId, answerOptionId){
    // if nothing had been selected, just sets the message for user
    if(!answerOptionId){
        setResultText(questionId, { successvalue: 2, comment: '' });
        return;
    }    
    
    var payload = JSON.stringify({
       answerOptionId: answerOptionId,
       questionId: questionId
    });
    
    $.ajax({
       url: "/vastaukset",
       dataType: 'json',
       contentType: 'application/json; charset=utf-8',
       type: 'post',
       data: payload,
       success: function(result){
            setResultText(questionId, result);
       }
    });
}

function setResultText(questionId, results){
    var resultMessage;
    if(results[0].successValue == 0){
        resultMessage = "Väärin...";
    } else if (results[0].successValue == 1) {
        resultMessage = "Oikein!";
    } else if (results[0].successValue == 2) {
        resultMessage = "Valitse ensin vastaus.";
    }    
    populateResultText(questionId, resultMessage, results[0].comment);   
    
    var achievementHtml = "";
    if(results[1]){
        $.each(results[1], function(key, achievement){
           achievementHtml += "<li>" + achievement.Name + "</li>"; 
        });
    }
    
    $("#achievement-list").html(achievementHtml);
}

function populateResultText(questionId, resultMessage, comment){
    var msgPart = "<p>" + resultMessage + "</p>";
    var commentPart = "<p>" + comment + "</p>";
    $("#question-result-" +  questionId).children().remove();
    $("#question-result-" +  questionId)
            .append(msgPart)
            .append(commentPart);
}

/*
 * Disabled for now because of the meta tag in the template for some not working
 * with the tests (despite working otherwise).
 * 
 * The error is as follows:
 * org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.thymeleaf.exceptions.TemplateProcessingException: Exception evaluating SpringEL expression: "_csrf.token" (answer_questions:13)

// sets a CSRF token for ajax calls
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
});

*/