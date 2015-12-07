$(document).ready(function(){  
    // removes all radio buttons
    $(document).remove("input[type=radio]");
        
    $(".question-list").each(function(){
        // adds edit and delete buttons to each question <ul>
        var questionId = $(this).attr("id").split('-').pop();
        var buttonDiv = $("<div class=question-button-set></div>");
        buttonDiv.append($("<button id='question-edit-" + questionId +"' class='btn-edit btn-primary'>Muokkaa</button>"));
        buttonDiv.append($("<button id='question-delete-" + questionId +"' class='btn-delete btn-danger'>Poista</button>"));
        $(this).append(buttonDiv);
        
        // adds listeners attached to each of these buttons
        buttonDiv.children(".btn-edit").click(function(event){
            event.preventDefault();
            var questionId = $(event.target).attr("id").split('-').pop();
            editQuestion(questionId);
        });
        
        buttonDiv.children(".btn-delete").click(function(event){
            event.preventDefault();
            var questionId = $(event.target).attr("id").split('-').pop();
            deleteQuestion(questionId);
        });
    });
    
    $("#questionForm").submit(function(event){
        packageAnswerOptionData();
    });
    
    $("#addAnswerOption").click(function(event){
        // adds new AnswerOption into table showing all AnswerOptions for the Question
        var newOptionRow = '<tr><td class="answertext"><pre>' + $('#answerText').val()+ '</pre></td>'
            + '<td class="answercomment">' + $('#answerComment').val() + '</td>'
            + '<td><input type="radio" name ="correctOne"' + ($('#isCorrectAnswer').is(':checked') ? ' checked' : '') + '/></td></tr>';
        $('#answerOptionsTable').append(newOptionRow);
        
        $('#answerText').val('');
        $('#answerComment').val('');
        $('#isCorrectAnswer').is(':checked');
    });
});

function editQuestion(questionId){
    console.log("Muuta " + questionId);
}

function deleteQuestion(questionId){
    $.ajax({
       url: "/tehtavat/" + questionId + "/poista",
       dataType: 'json',
       contentType: 'application/json; charset=utf-8',
       type: 'post',
       success: function(result){
            window.location.reload();
       }
    });
}

function packageAnswerOptionData(){
    // AnswerOption data is packaged into JSON format and then added into a hidden field
    // so that it will be passed along other form data to controller
    var answerOptionData = [];
    $('#answerOptionsTable tr').each(function(){
        if($(this).find('.answertext').text() !== ''){
            var optionData = {
                answerText: $(this).find('.answertext').text(),
                answerComment: $(this).find('.answercomment').text(),
                isCorrectAnswer: $(this).find('td input:radio').is(":checked")
            }
            answerOptionData.push(optionData); 
        }
    });
    
    var answerOptionJSON = JSON.stringify(answerOptionData);    
    $('<input type="hidden" name="answerOptionSet" />').val(answerOptionJSON).appendTo("#questionForm");
}
