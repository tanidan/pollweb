<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="CSS/login.css" />
        <title>PollWeb - UNIVAQ</title>
        <link rel="shortcut icon" href="CSS/pictures/logo_univaq.png" />
    </head>
    
    <body>
        <header>
            <%@ include file="header.jsp"%>
        </header>
        
        <section>
            <div class="login">
                <form method="post" action="Login">
                    <div class="top_login"><p>Login :</p></div>
                    <div><div id="email">
                        <label for="email">Mail Adress :</label>
                        <input type="email" name="email" placeholder="Ex : john.smith@gmail.com" size="30" maxlength="40"/>
                    </div>
                    <div id="password">
                        <label for="password">Password :</label>
                        <input type="password" name="password" placeholder="Ex : C~&;3H:t5*dD" size="20" maxlength="20"/>
                    </div></div>
                    <button name="login">Login</button>
                </form>
            </div>
                    <c:if test="${not empty message}">
                        <div id="error">
                            <p>Error message: ${message}</p>
                        </div>
                    </c:if>
        </section>
            
        <footer>
            <%@ include file="footer.jsp"%>
        </footer>
    </body>
</html>
