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

def f = GroovyStreamTemplate.getTemplateFile("../../gs/admin/nav-tree/nav-tree.tpl")
def engine = new MarkupTemplateEngine()
def template = engine.createTemplate(f).make(binding)
println template.toString()
