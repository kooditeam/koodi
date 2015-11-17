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
});

function sendAnswer(questionId, answerOptionId){
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

function setResultText(questionId, result){
    var resultMessage;
    if(result.result == 0){
        resultMessage = "Väärin...";
    } else if (result.result == 1) {
        resultMessage = "Oikein!";
    } else if (result.result == 2) {
        resultMessage = "Valitse ensin vastaus.";
    }
    
    $("#question-result-" +  questionId).text(resultMessage);
}