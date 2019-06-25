<%-- 
    Document   : poll_user
    Created on : 2019.06.15., 15:30:53
    Author     : HavaG
--%>


<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="CSS/poll_user.css" />
        <title>PollWeb - UNIVAQ</title>
        <link rel="shortcut icon" href="CSS/pictures/logo_univaq.png" />
    </head>

    <body>
        <header>
            <%@ include file="header.jsp"%>
        </header>

        <section>            
                <div class="add_user">
                    <form method="post" action="PollCreate">
                        <div class="top_add_user"><p>Add all the users' email addres. Divide with ',' caracters</p></div>
                        <div><div id="email">
                            <label for="emails">Emails :</label><br/>
                            <textarea name="emails" maxlength="200" rows="3" cols="50"/></textarea>
                        </div></div>
                        <div id="button"><button name="submit">Submit</button></div>
                    </form>
                </div>                                    
        </section>

        <footer>
            <%@ include file="footer.jsp"%>
        </footer>
    </body>
</html>