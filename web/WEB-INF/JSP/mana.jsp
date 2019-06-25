<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="CSS/mana.css" />
        <title>PollWeb - UNIVAQ</title>
        <link rel="shortcut icon" href="CSS/pictures/logo_univaq.png" />
    </head>
    
    <body>
        <header>
            <%@ include file="header.jsp"%>
        </header>
        
        <section>
            <a id="add_poll" href="PollCreate" name="addPoll">Add a poll</a>
            <a id="answers" href="Answers" name="addPoll">Answers</a>
            <div id="poll_list">
                <c:forEach items="${polls}" var="poll">
                    <a href="Example?pollID=${poll.key}">${poll.title}</a>
                </c:forEach>
            </div>
        </section>
            
        <footer>
            <%@ include file="footer.jsp"%>
        </footer>
    </body>
</html>