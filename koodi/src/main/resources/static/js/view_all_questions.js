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
