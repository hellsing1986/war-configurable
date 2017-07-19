import vn.vnptnet.archetype.warconfigurable.App

import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import vn.vnptnet.archetype.warconfigurable.util.GroovyStreamTemplate

import javax.servlet.http.HttpServletResponse
import groovy.text.markup.*

//ServletRequest req = request
//ServletResponse res = response
HttpServletRequest httpReq = request
//HttpServletResponse httpRes = response
//ServletContext ctx = context
//ctx.getRequestDispatcher("/gs/token/validate_token.groovy").forward(request,response)
//httpRes.sendRedirect("/gs/token/validate_token")

def binding = [
    "httpReq":httpReq,
    "list":[1,2,3,4,5],
    "body":"hello/hello",
    "layout":"_layout",
    "title":"hello",
    "GroovyStreamTemplate":GroovyStreamTemplate.class
]

def f = GroovyStreamTemplate.getTemplateFile("../../gs/hello/hello.tpl")
def engine = new MarkupTemplateEngine()
def template = engine.createTemplate(f).make(binding)
println template.toString()

//println GroovyStreamTemplate.html(binding)
/*
println """
<html>
    <head>
        <title>Groovy Servlet</title>
    </head>
    <body>
        <p>
            remoteHost = ${httpReq.getRemoteHost()}
        </p>
        <p>
            remoteAddr = ${httpReq.getRemoteAddr()}
        </p>
        <p>
            Proxy-Client-IP = ${httpReq.getHeader("Proxy-Client-IP")}
        </p>
        <p>
            WL-Proxy-Client-IP = ${httpReq.getHeader("WL-Proxy-Client-IP")}
        </p>
        <p>
            HTTP_CLIENT_IP = ${httpReq.getHeader("HTTP_CLIENT_IP")}
        </p>
        <p>
            HTTP_X_FORWARDED_FOR = ${httpReq.getHeader("HTTP_X_FORWARDED_FOR")}
        </p>
    </body>
</html>
"""*/
