<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="CSS/poll_designing.css" />
        <title>PollWeb - UNIVAQ</title>
        <link rel="shortcut icon" href="CSS/pictures/logo_univaq.png" />
    </head>
    
    <body>
        <header>
            <%@ include file="header.jsp"%>
        </header>
        
        <section>
            <form method="post" action="PollCreate">
                <input type="text" name="poll_title" id="poll_title" size="25" placeholder="Name of the Poll" maxlength="40" required/>
                
                <textarea name="opening_text" id="opening_text" class="text" placeholder="Opening text" maxlength="200" rows="3" cols="56" required></textarea>

                 <c:forEach var = "i" begin = "1" end = "10">
                    <div class="question" id="question_${i}">

                        <input type="text" name="question_name_${i}"  id="question_name_${i}" class="question_top" size="45" placeholder="Question" maxlength="40"/>
                        <div class="question_body"  id="question_body_${i}">
                            <textarea  name="question_note" id="question_note_${i}" class="note" placeholder="Explanatory note" maxlength="40" rows="2" cols="50"></textarea>
                            <div class="type_answer" id="type_answer_${i}"> <input type="text" size="40" placeholder="Answer" maxlength="40"></div>

                        </div>
                        <div class="button_question">
                                <input type="button" class="add_answer" id="add_answer_${i}" value="+"/>
                        </div>
                    </div>
                    <div class="question_choices" id="question_choices_${i}">
                        <label for="mandatory_yes_${i}">Mandatory : Yes</label><input type="radio" name="mandatory_${i}" value="yes" id="mandatory_yes_${i}"/>
                        <label for="mandatory_no_${i}">No</label><input type="radio" name="mandatory_${i}" value="no" id="mandatory_no_${i}" checked/><br/><br/>
                        <label for="question_type_${i}">Type of question : </label>
                        <select name="question_type_${i}" id="question_type_${i}">
                            <option value="short_text">Short text</option>
                            <option value="long_text">Long text</option>
                            <option value="number">Number</option>
                            <option value="date">Date</option>
                            <option value="single_choice">Single choice</option>
                            <option value="multiple_choice">Multiple choice</option>
                        </select>
                    </div>
                </c:forEach>
               
                <div id="buttons"><input type="button" id="add_question" value="+" />
                     <input type="button" id="res_question" value="-" /></div>

                <textarea name="closing_text" id="closing_text" class="text" placeholder="Closing text" maxlength="200" rows="3" cols="56" required></textarea>

                <div><label for="reserved_yes">Poll reserved : Yes</label><input type="radio" name="reserved" value="yes" id="reserved_yes" />
                     <label for="reserved_no">No</label><input type="radio" name="reserved" value="no" id="reserved_no" checked/></div>

                <input type="submit" id="poll_send" value="Ok" name="OK"/>                
            </form>
        </section>
            
        <footer>
            <%@ include file="footer.jsp"%>
        </footer>
        
        <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>-->
        <script src="JavaScript/jquery-3.4.1.min.js"></script>
        <script src="JavaScript/poll_designing.js"></script>

    </body>
</html>