<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="CSS/answers.css" />
        <title>PollWeb - UNIVAQ</title>
        <link rel="shortcut icon" href="CSS/pictures/logo_univaq.png" />
    </head>
    
    <body>
        <header>
            <%@ include file="header.jsp"%>
        </header>
        
        <section>
                <table>
                    <tr>
                        <th class="side">Poll</th>
                        <th class="center">User</th>
                        <th class="side">File</th>
                    </tr>

                    <c:forEach items="${results}" var="result">  
                        <td class="side">${result.poll}</td>
                        <td class="center">${result.user.email}</td>
                        <td class="side"><a href="Example?pollID=${poll.key}">${result.file}</a><td>
                    </c:forEach>
                    
                    <tr>
                        <td class="side">Poll on Hobbies</td>
                        <td class="center">Michel</td>
                        <td class="side"><a href="Example">Example</a></td>
                    </tr>
                    <tr>
                        <td class="side">Poll on Food</td>
                        <td class="center">Alex</td>
                        <td class="side"><a href="Example">Example</a></td>
                    </tr>                    
                    
                </table>
        </section>
            
        <footer>
            <%@ include file="footer.jsp"%>
        </footer>

    </body>
</html>
