import groovy.text.StreamingTemplateEngine
import groovy.text.markup.MarkupTemplateEngine

import javax.servlet.RequestDispatcher
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

HttpServletRequest httpreq = request
ServletRequest req = request
ServletResponse res = response
//res.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
//res.setHeader("Location", "${httpreq.getContextPath()}/index.jsp")

def username = req.getParameter("username")
def password = req.getParameter("password")

def html = """
<form method="POST">
username : <input name="username" value="${username}" />
<br/>
password : <input name="password" value="${password}" />
<button type="submit">login</button>
</form>
"""
def engine = new StreamingTemplateEngine()
def template = engine.createTemplate(html)
return template.make().toString()