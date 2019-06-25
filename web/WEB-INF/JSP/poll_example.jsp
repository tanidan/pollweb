<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="CSS/poll_example.css" />
        <title>PollWeb - UNIVAQ</title>
        <link rel="shortcut icon" href="CSS/pictures/logo_univaq.png" />
    </head>
    
    <body>
        <header>
            <%@ include file="header.jsp"%>
        </header>
        
        <section>
        <form method="post" action="Example">    
            <h3><c:out value="${poll.title}"/></h3>

            <p class="text" id="open_tag">${poll.openText}</p>
            

            <c:forEach items="${questions}" var="question">
                <div class="question">
                    <div class="fond_question"><c:out value="${question.text}"/></div>
                    <c:if test = "${question.note!=null}">
                        <p class="note"><c:out value = "${question.note}"/></p> <%-- TODO: make note class visible --%>
                    </c:if>
                    <div class="question_body">
                    
                        <c:choose>
                            <c:when test="${question.type == 'short_text'}"><input type="text" name="${question.position}"/></c:when>
                            <c:when test="${question.type == 'long_text'}"><textarea name="${question.position}"></textarea></c:when>
                            <c:when test="${question.type == 'number'}"><input type="number" name="${question.position}"/></c:when>
                            <c:when test="${question.type == 'date'}"><input type="date" name="${question.position}"/></c:when>
                            <c:when test="${question.type == 'single_choice'}">                                     
                                <c:forTokens items = "${question.answer}" delims = "," var = "choice">
                                    <div>
                                        <input type="radio" name="${question.position}"/><label>${choice}</label><br/>
                                    </div>
                                </c:forTokens>  
                            </c:when>
                            <c:when test="${question.type == 'multiple_choice'}">
                                 <c:forTokens items = "${question.answer}" delims = "," var = "choice">
                                    <div>
                                        <input type="checkbox" name="${question.position}"/><label>${choice}</label><br/>
                                    </div>
                                </c:forTokens>                               
                            </c:when>                                                    
                        </c:choose>   
                                    
                    </div>
                </div>        
            </c:forEach>

            

            <p class="text" id="closing_text">${poll.closeText}</p>

            <input type="submit" id="poll_send" value="Send" name="Send"/>
            
            <c:if test="${not empty save}">
                            <input type="submit" id="poll_send" value="Save" name="save"/>
            </c:if>

        </form>
        </section>
            
        <footer>
            <%@ include file="footer.jsp"%>
        </footer>

    </body>
</html>